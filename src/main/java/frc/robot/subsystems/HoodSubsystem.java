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
import frc.robot.Constants.HardwareID.HoodIds;
import frc.robot.Constants.TuningValues.HoodValues;

public class HoodSubsystem extends SubsystemBase {
    
    TalonFX hoodMotor;
    PositionVoltage targetPosition;

    public HoodSubsystem() {
        hoodMotor = new TalonFX(HoodIds.hoodCanId);
        hoodMotor.setNeutralMode(NeutralModeValue.Brake);

        var hoodConfig = new Slot0Configs();
        hoodConfig.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        hoodConfig.kS = HoodValues.kS;
        hoodConfig.kG = HoodValues.kG;
        hoodConfig.kP = HoodValues.kP;
        hoodConfig.kI = HoodValues.kI;
        hoodConfig.kD = HoodValues.kD;

        hoodMotor.getConfigurator().apply(hoodConfig);
        targetPosition = new PositionVoltage(0).withPosition(0);
    }

    public void runHood(double power) {
        hoodMotor.set(power);
    } 

    public void enableHood() {
        hoodMotor.setControl(targetPosition);
    }

    public void setTargetPosition(double position) {
        targetPosition = new PositionVoltage(0).withPosition(position);
    }

    public double getCurrent() {
        return hoodMotor.getStatorCurrent().getValueAsDouble();
    }

    public void zeroHood() {
        hoodMotor.setPosition(0);
    }
}

