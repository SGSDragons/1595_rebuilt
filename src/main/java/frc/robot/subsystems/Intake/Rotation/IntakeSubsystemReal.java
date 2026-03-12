package frc.robot.subsystems.Intake.Rotation;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.HardwareID.IntakeIds;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.Constants.CurrentLimits.IntakeLimits;

public class IntakeSubsystemReal extends IntakeSubsystem {
    
    PositionVoltage targetPosition;
    TalonFX rotationMotor;

    public IntakeSubsystemReal() {

        rotationMotor = new TalonFX(IntakeIds.rotationCanId);
        rotationMotor.setNeutralMode(NeutralModeValue.Brake);

        var rotationConfig = new TalonFXConfiguration();

        rotationConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rotationConfig.CurrentLimits.StatorCurrentLimit = IntakeLimits.maxLimit;
        rotationConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        rotationConfig.Slot0.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        rotationConfig.Slot0.kS = IntakeValues.kS;
        rotationConfig.Slot0.kG = IntakeValues.kG;
        rotationConfig.Slot0.kP = IntakeValues.kP;
        rotationConfig.Slot0.kI = IntakeValues.kI;
        rotationConfig.Slot0.kD = IntakeValues.kD;

        rotationMotor.getConfigurator().apply(rotationConfig);
        state = IntakeStates.RETRACTED;
        targetPosition = new PositionVoltage(0).withPosition(IntakeValues.retracted);
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

    public void setTargetPosition(IntakeStates position) {
        if (position == IntakeStates.RETRACTED) {
            targetPosition = new PositionVoltage(0).withPosition(IntakeValues.retracted);
        } else {
            targetPosition = new PositionVoltage(0).withPosition(IntakeValues.extended);
        }
    }

    public double getPosition() {
        return rotationMotor.getPosition().getValueAsDouble();
    }

    public IntakeStates getTargetPosition() {
        if (targetPosition.Position == IntakeValues.retracted) {
            return IntakeStates.RETRACTED;
        } else {
            return IntakeStates.EXTENDED;
        }
    }

    public IntakeStates getState() {
        return state;
    }

    public void setState(IntakeStates newState) {
        state = newState;
    }

    public boolean isExtended() {
        // return (Math.abs(getPosition() - IntakeValues.extended) < IntakeValues.tolerance);
        return (getPosition() > IntakeValues.extended-1);
    }

    public boolean isRetracted() {
        // return (Math.abs(getPosition() - IntakeValues.retracted) < IntakeValues.tolerance);
        return (getPosition() < IntakeValues.retracted+0.5);
    }
 
    public double getCurrent() {
        return rotationMotor.getStatorCurrent().getValueAsDouble();
    }

    public void zeroRotation() {
        rotationMotor.setPosition(0);
    }

    @Override
    public void periodic() {
        if (isExtended()) {
            state = IntakeStates.EXTENDED;
        } 
        else if (isRetracted()) {
            state = IntakeStates.RETRACTED;
        }
        telemetry();
    }

    public void telemetry() {
        SmartDashboard.putNumber("Rotation Target", targetPosition.Position);
        SmartDashboard.putNumber("Rotation Position", getPosition());
        SmartDashboard.putNumber("Rotation Current", getCurrent());
    }
}
