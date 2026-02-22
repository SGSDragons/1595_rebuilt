package frc.robot.subsystems.Intake;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareID.IntakeIds;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.Constants.CurrentLimits.IntakeLimits;

public class IntakeSubsystemReal extends IntakeSubsystem {
    
    TalonFX rotationMotor;
    PositionVoltage targetPosition;

    public IntakeSubsystemReal() {

        rotationMotor = new TalonFX(IntakeIds.rotationCanId);
        rotationMotor.setNeutralMode(NeutralModeValue.Brake);

        var rotationConfig = new TalonFXConfiguration();

        rotationConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rotationConfig.CurrentLimits.StatorCurrentLimit = IntakeLimits.maxLimit;

        rotationConfig.Slot0.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        rotationConfig.Slot0.kS = IntakeValues.kS;
        rotationConfig.Slot0.kG = IntakeValues.kG;
        rotationConfig.Slot0.kP = IntakeValues.kP;
        rotationConfig.Slot0.kI = IntakeValues.kI;
        rotationConfig.Slot0.kD = IntakeValues.kD;

        rotationMotor.getConfigurator().apply(rotationConfig);
        targetPosition = new PositionVoltage(0).withPosition(0);
    }

    public void runRotation(double power) {
        rotationMotor.set(power);
    } 

    public void stopRotation() {
        rotationMotor.stopMotor();
    } 

    public void gotoPosition() {
        rotationMotor.setControl(targetPosition);
    }

    public void setTargetPosition(double position) {
        targetPosition = new PositionVoltage(0).withPosition(position);
    }

    public double getPosition() {
        return rotationMotor.getPosition().getValueAsDouble();
    }

    public boolean isExtended() {
        return (Math.abs(getPosition() - IntakeValues.extended) < IntakeValues.tolerance);
    }

    public boolean isRetracted() {
        return (Math.abs(getPosition() - IntakeValues.retracted) < IntakeValues.tolerance);
    }
 
    public double getCurrent() {
        return rotationMotor.getStatorCurrent().getValueAsDouble();
    }

    public void zeroRotation() {
        rotationMotor.setPosition(0);
    }

    @Override
    public void periodic() {

    }

    public void telemetry() {
        
    }
}
