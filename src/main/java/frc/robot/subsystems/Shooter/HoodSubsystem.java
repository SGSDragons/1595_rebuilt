package frc.robot.subsystems.Shooter;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj.simulation.ElevatorSim;
import edu.wpi.first.wpilibj.smartdashboard.Mechanism2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareID.HoodIds;
import frc.robot.Constants.TuningValues.HoodValues;
import frc.robot.Constants.CurrentLimits.HoodLimits;

public class HoodSubsystem extends SubsystemBase {
    
    public TalonFX hoodMotor;
    PositionVoltage targetPosition;

    public HoodSubsystem() {
        hoodMotor = new TalonFX(HoodIds.hoodCanId);
        hoodMotor.setNeutralMode(NeutralModeValue.Brake);

        var hoodConfig = new TalonFXConfiguration();

        hoodConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        hoodConfig.CurrentLimits.StatorCurrentLimit = HoodLimits.maxLimit;

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

    public void telemetry() {
        SmartDashboard.putNumber("Target Position", targetPosition.Position);
        SmartDashboard.putNumber("Hood Position", getPosition());
    }
}

