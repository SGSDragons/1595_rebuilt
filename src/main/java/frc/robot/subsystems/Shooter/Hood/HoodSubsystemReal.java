package frc.robot.subsystems.Shooter.Hood;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.HardwareID.HoodIds;
import frc.robot.Constants.TuningValues.HoodValues;
import frc.robot.Constants.CurrentLimits.HoodLimits;

public class HoodSubsystemReal extends HoodSubsystem {
    
    public TalonFX hoodMotor;
    private PositionVoltage targetPosition;

    public HoodSubsystemReal() {
        hoodMotor = new TalonFX(HoodIds.hoodCanId);
        hoodMotor.setNeutralMode(NeutralModeValue.Brake);

        var hoodConfig = new TalonFXConfiguration();

        hoodConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        hoodConfig.CurrentLimits.StatorCurrentLimit = HoodLimits.statorLimit;
        hoodConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        hoodConfig.Slot0.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        hoodConfig.Slot0.kS = HoodValues.kS;
        hoodConfig.Slot0.kG = HoodValues.kG;
        hoodConfig.Slot0.kP = HoodValues.kP;
        hoodConfig.Slot0.kI = HoodValues.kI;
        hoodConfig.Slot0.kD = HoodValues.kD;

        hoodMotor.getConfigurator().apply(hoodConfig);
        targetPosition = new PositionVoltage(0).withPosition(0);

        // Mechanism2d arm = new Mechanism2d(2, 3);
    }

    @Override
    public void runHood(double power) {
        hoodMotor.set(power);
    } 

    @Override
    public void enableHood() {
        hoodMotor.setControl(targetPosition);
    }

    @Override
    public void setTargetPosition(double position) {
        position = Math.max(HoodValues.min, Math.min(position, HoodValues.max));
        targetPosition = new PositionVoltage(0).withPosition(position);
    }

    @Override
    public boolean atTargetPosition() {
        return (Math.abs(getPosition() - targetPosition.Position) < HoodValues.tolerance);
    }

    @Override
    public double getSupplyCurrent() {
        return hoodMotor.getSupplyCurrent().getValueAsDouble();
    }

    @Override
    public double getStatorCurrent() {
        return hoodMotor.getStatorCurrent().getValueAsDouble();
    }

    @Override
    public double getPosition() {
        return hoodMotor.getPosition().getValueAsDouble();
    }

    public void zeroHood() {
        hoodMotor.setPosition(0);
    }

    @Override
    public void periodic() {
        telemetry();
    }

    @Override
    public void telemetry() {
        SmartDashboard.putNumber("Hood Target", targetPosition.Position);
        SmartDashboard.putNumber("Hood Position", getPosition());
        SmartDashboard.putNumber("Hood Supply Current", getSupplyCurrent());
        SmartDashboard.putNumber("Hood Stator Current", getStatorCurrent());
    }
}

