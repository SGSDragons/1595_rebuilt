package frc.robot.subsystems.Shooter.Shooter;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareID.ShooterIds;
import frc.robot.Constants.TuningValues.ShooterValues;
import frc.robot.subsystems.GoalAim;
import frc.robot.Constants.Aiming;
import frc.robot.Constants.CurrentLimits.ShooterLimits;


public class ShooterSubsystemReal extends ShooterSubsystem {
    
    TalonFX leftShooter;
    TalonFX rightShooter;

    VelocityVoltage targetVelocity;

    boolean shooting = false;
    GoalAim aiming;

    public ShooterSubsystemReal(GoalAim aiming) {
        this.aiming = aiming;

        // Make leftShooter control both left and right shooters
        leftShooter = new TalonFX(ShooterIds.leftShooterCanId);
        rightShooter = new TalonFX(ShooterIds.rightShooterCanId);

        leftShooter.setNeutralMode(NeutralModeValue.Coast);
        rightShooter.setNeutralMode(NeutralModeValue.Coast);

        var leftShooterConfig = new TalonFXConfiguration();

        leftShooterConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        leftShooterConfig.CurrentLimits.StatorCurrentLimit = ShooterLimits.maxLimit;
        leftShooterConfig.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

        leftShooterConfig.Slot0.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        leftShooterConfig.Slot0.kS = ShooterValues.kS;
        leftShooterConfig.Slot0.kV = ShooterValues.kV;
        leftShooterConfig.Slot0.kP = ShooterValues.kP;
        leftShooterConfig.Slot0.kI = ShooterValues.kI;
        leftShooterConfig.Slot0.kD = ShooterValues.kD;

        leftShooter.getConfigurator().apply(leftShooterConfig);
        

        var rightShooterConfig = new TalonFXConfiguration();

        rightShooterConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rightShooterConfig.CurrentLimits.StatorCurrentLimit = ShooterLimits.maxLimit;
        rightShooterConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        rightShooterConfig.Slot0.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        rightShooterConfig.Slot0.kS = ShooterValues.kS;
        rightShooterConfig.Slot0.kV = ShooterValues.kV;
        rightShooterConfig.Slot0.kP = ShooterValues.kP;
        rightShooterConfig.Slot0.kI = ShooterValues.kI;
        rightShooterConfig.Slot0.kD = ShooterValues.kD;

        rightShooter.getConfigurator().apply(rightShooterConfig);

        targetVelocity = new VelocityVoltage(0).withVelocity(ShooterValues.runSpeed);
    }

    public void enableShooting() {
        this.shooting = true;
    }
    public void disableShooting() {
        this.shooting = false;
    }

    public double getVelocity() {
        return (leftShooter.getVelocity().getValueAsDouble() + leftShooter.getVelocity().getValueAsDouble()) / 2;
    }
    
    @Override
    public void periodic() {
        if (shooting) {
            targetVelocity = new VelocityVoltage(aiming.getAdjustedDistance());
        } else {
            targetVelocity =  new VelocityVoltage(ShooterValues.runSpeed);
        }
        telemetry();
    }

    public void telemetry() {
        SmartDashboard.putNumber("Shooter Target", targetVelocity.Velocity);
        SmartDashboard.putNumber("Shooter Velocity", getVelocity());
        SmartDashboard.putNumber("Shooter Current", leftShooter.getStatorCurrent().getValueAsDouble());
    }
}
