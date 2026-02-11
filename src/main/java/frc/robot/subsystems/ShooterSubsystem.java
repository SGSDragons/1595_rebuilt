package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareID.ShooterIds;
import frc.robot.Constants.TuningValues.ShooterValues;
import frc.robot.Constants.TuningValues.HoodValues;

public class ShooterSubsystem extends SubsystemBase {
    
    TalonFX leftShooter;
    TalonFX rightShooter;
    TalonFX shooterMotor;
    TalonFX hoodMotor;

    VelocityVoltage targetVelocity;
    PositionVoltage targetPosition;

    public ShooterSubsystem() {
        // Make shooterMotor control both left and right shooters
        shooterMotor = new TalonFX(ShooterIds.leftShooterCanId);
        rightShooter = new TalonFX(ShooterIds.rightShooterCanId);
        rightShooter.setControl(new Follower(leftShooter.getDeviceID(), MotorAlignmentValue.Opposed));

        rightShooter.setNeutralMode(NeutralModeValue.Brake);
        shooterMotor.setNeutralMode(NeutralModeValue.Brake);

        var shooterConfig = new Slot0Configs();
        shooterConfig.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        shooterConfig.kS = ShooterValues.kS;
        shooterConfig.kG = ShooterValues.kV;
        shooterConfig.kP = ShooterValues.kP;
        shooterConfig.kI = ShooterValues.kI;
        shooterConfig.kD = ShooterValues.kD;

        leftShooter.getConfigurator().apply(shooterConfig);
        targetVelocity = new VelocityVoltage(0).withVelocity(ShooterValues.runSpeed);


        hoodMotor = new TalonFX(ShooterIds.hoodCanId);
        hoodMotor.setNeutralMode(NeutralModeValue.Brake);

        var hoodConfig = new Slot0Configs();
        hoodConfig.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        hoodConfig.kS = HoodValues.kS;
        hoodConfig.kG = HoodValues.kG;
        hoodConfig.kP = HoodValues.kP;
        hoodConfig.kI = HoodValues.kI;
        hoodConfig.kD = HoodValues.kD;

        hoodMotor.getConfigurator().apply(hoodConfig);
        targetPosition = new PositionVoltage(0).withPosition(HoodValues.minLimit);

    }

    public void runShooter() {
        shooterMotor.setControl(targetVelocity);
    }

    public void runHood(double power) {
        hoodMotor.set(power);
    } 

    public void enableHood() {
        hoodMotor.setControl(targetPosition);
    }

    public void disableHood() {
        setTargetPosition(HoodValues.minLimit);
        hoodMotor.setControl(targetPosition);
    }

    public void setTargetPosition(double position) {
        targetPosition = new PositionVoltage(0).withPosition(position);
    }

    public double getHoodCurrent() {
        return hoodMotor.getStatorCurrent().getValueAsDouble();
    }

    public void zeroHood() {
        hoodMotor.setPosition(0);
    }
}
