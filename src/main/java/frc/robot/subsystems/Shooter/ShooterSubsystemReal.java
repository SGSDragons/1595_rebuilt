package frc.robot.subsystems.Shooter;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareID.ShooterIds;
import frc.robot.Constants.TuningValues.ShooterValues;
import frc.robot.Constants.CurrentLimits.ShooterLimits;


public class ShooterSubsystemReal extends ShooterSubsystem {
    
    TalonFX leftShooter;
    TalonFX rightShooter;
    TalonFX shooterMotor;

    VelocityVoltage targetVelocity;

    public ShooterSubsystemReal() {
        // Make shooterMotor control both left and right shooters
        shooterMotor = new TalonFX(ShooterIds.leftShooterCanId);
        rightShooter = new TalonFX(ShooterIds.rightShooterCanId);
        rightShooter.setControl(new Follower(shooterMotor.getDeviceID(), MotorAlignmentValue.Opposed));

        rightShooter.setNeutralMode(NeutralModeValue.Brake);
        shooterMotor.setNeutralMode(NeutralModeValue.Brake);

        var shooterConfig = new TalonFXConfiguration();

        shooterConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        shooterConfig.CurrentLimits.StatorCurrentLimit = ShooterLimits.maxLimit;

        shooterConfig.Slot0.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        shooterConfig.Slot0.kS = ShooterValues.kS;
        shooterConfig.Slot0.kG = ShooterValues.kV;
        shooterConfig.Slot0.kP = ShooterValues.kP;
        shooterConfig.Slot0.kI = ShooterValues.kI;
        shooterConfig.Slot0.kD = ShooterValues.kD;

        shooterMotor.getConfigurator().apply(shooterConfig);
        targetVelocity = new VelocityVoltage(0).withVelocity(ShooterValues.slowSpeed);
    }

    public void runShooter() {
        shooterMotor.setControl(targetVelocity);
    }

    public void setTargetVelocity(double velocity) {
        targetVelocity = new VelocityVoltage(0).withVelocity(velocity);
    }

    public double getVelocity() {
        return shooterMotor.getVelocity().getValueAsDouble();
    }
    
    @Override
    public void periodic() {

    }

    public void telemetry() {
        
    }
}
