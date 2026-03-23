package frc.robot.subsystems.Shooter.Shooter;


import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareID.ShooterIds;
import frc.robot.Constants.TuningValues.ShooterValues;
import frc.robot.Constants.CurrentLimits.ShooterLimits;


public class ShooterSubsystemReal extends ShooterSubsystem {
    
    TalonFX leftShooter;
    TalonFX rightShooter;
    BooleanSupplier addFF;

    private VelocityVoltage targetVelocity;

    public ShooterSubsystemReal(BooleanSupplier addFF) {
        this.addFF = addFF;

        // Make leftShooter control both left and right shooters
        leftShooter = new TalonFX(ShooterIds.leftShooterCanId);
        rightShooter = new TalonFX(ShooterIds.rightShooterCanId);

        leftShooter.setNeutralMode(NeutralModeValue.Coast);
        rightShooter.setNeutralMode(NeutralModeValue.Coast);


        var leftShooterConfig = new TalonFXConfiguration();

        leftShooterConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        leftShooterConfig.CurrentLimits.SupplyCurrentLimit = ShooterLimits.supplyLimit;
        leftShooterConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

        leftShooterConfig.Slot0.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        leftShooterConfig.Slot0.kS = ShooterValues.kS;
        leftShooterConfig.Slot0.kA = ShooterValues.kA;
        leftShooterConfig.Slot0.kV = ShooterValues.kV;
        leftShooterConfig.Slot0.kP = ShooterValues.kP;
        leftShooterConfig.Slot0.kI = ShooterValues.kI;
        leftShooterConfig.Slot0.kD = ShooterValues.kD;

        leftShooter.getConfigurator().apply(leftShooterConfig);
        

        var rightShooterConfig = new TalonFXConfiguration();

        rightShooterConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        rightShooterConfig.CurrentLimits.SupplyCurrentLimit = ShooterLimits.supplyLimit;
        rightShooterConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        rightShooterConfig.Slot0.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        rightShooterConfig.Slot0.kS = ShooterValues.kS;
        rightShooterConfig.Slot0.kA = ShooterValues.kA;
        rightShooterConfig.Slot0.kV = ShooterValues.kV;
        rightShooterConfig.Slot0.kP = ShooterValues.kP;
        rightShooterConfig.Slot0.kI = ShooterValues.kI;
        rightShooterConfig.Slot0.kD = ShooterValues.kD;

        rightShooter.getConfigurator().apply(rightShooterConfig);

        targetVelocity = new VelocityVoltage(0).withVelocity(ShooterValues.runSpeed);
    }

    @Override
    public void runAtPower(double power) {
        leftShooter.set(power);
        rightShooter.set(power);
    }

    @Override
    public void runShooter() {
        leftShooter.setControl(targetVelocity);
        rightShooter.setControl(targetVelocity);
    }

    @Override
    public void setTargetVelocity(double velocity) {
        double value = Math.min(velocity, ShooterValues.maxShooterSpeed);
        targetVelocity = new VelocityVoltage(0).withVelocity(value);
        if (this.addFF.getAsBoolean()) {
            targetVelocity = targetVelocity.withFeedForward(ShooterValues.kickVoltage);
        }
    }

    // @Override
    // public void FFkick(double volts, double seconds) {
        
    //     var kicked = targetVelocity.withFeedForward(volts);
    //     leftShooter.setControl(kicked);
    //     rightShooter.setControl(kicked);

    //     Timer.delay(seconds);
    //     leftShooter.setControl(targetVelocity);
    //     rightShooter.setControl(targetVelocity);

    // }

    @Override
    public boolean nearTargetSpeed() {
        return (getAverageVelocity() > targetVelocity.Velocity-ShooterValues.tolerance);
    }

    @Override
    public double getLeftVelocity() {
        return leftShooter.getVelocity().getValueAsDouble();
    }

    @Override
    public double getRightVelocity() {
        return rightShooter.getVelocity().getValueAsDouble();    
    }

    @Override
    public double getAverageVelocity() {
        return (rightShooter.getVelocity().getValueAsDouble() + leftShooter.getVelocity().getValueAsDouble()) / 2;    
    }

    @Override
    public double getSupplyCurrent() {
        return (rightShooter.getSupplyCurrent().getValueAsDouble() + leftShooter.getSupplyCurrent().getValueAsDouble()) / 2;
    }

    @Override
    public double getStatorCurrent() {
        return (rightShooter.getStatorCurrent().getValueAsDouble() + leftShooter.getStatorCurrent().getValueAsDouble()) / 2;
    }
    
    @Override
    public void periodic() {
        telemetry();
    }

    @Override
    public void telemetry() {
        SmartDashboard.putNumber("Shooter Target", targetVelocity.Velocity);

        SmartDashboard.putNumber("LeftShooter Velocity", getLeftVelocity());
        SmartDashboard.putNumber("RightShooter Velocity", getRightVelocity());
        SmartDashboard.putNumber("ShooterAverage Velocity", getAverageVelocity());

        SmartDashboard.putNumber("Shooter Stator Current", leftShooter.getStatorCurrent().getValueAsDouble());
        SmartDashboard.putNumber("Shooter Supply Current", leftShooter.getSupplyCurrent().getValueAsDouble());
    }
}
