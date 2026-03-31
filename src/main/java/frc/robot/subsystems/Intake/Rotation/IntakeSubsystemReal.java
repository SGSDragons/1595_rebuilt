package frc.robot.subsystems.Intake.Rotation;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.HardwareID.IntakeIds;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.Constants.CurrentLimits.HoodLimits;
import frc.robot.Constants.CurrentLimits.IntakeLimits;

public class IntakeSubsystemReal extends IntakeSubsystem {
    
    PositionVoltage targetPosition;
    TalonFX rotationMotor;

    private Debouncer spikeTime;
    private boolean isZeroed;

    public IntakeSubsystemReal() {

        rotationMotor = new TalonFX(IntakeIds.rotationCanId);
        rotationMotor.setNeutralMode(NeutralModeValue.Brake);

        var rotationConfig = new TalonFXConfiguration();

        rotationConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rotationConfig.CurrentLimits.StatorCurrentLimit = IntakeLimits.statorLimit;
        rotationConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        rotationConfig.Slot0.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        rotationConfig.Slot0.kS = IntakeValues.kS;
        rotationConfig.Slot0.kG = IntakeValues.kG;
        rotationConfig.Slot0.kP = IntakeValues.kP;
        rotationConfig.Slot0.kI = IntakeValues.kI;
        rotationConfig.Slot0.kD = IntakeValues.kD;

        rotationMotor.getConfigurator().apply(rotationConfig);
        targetPosition = new PositionVoltage(0).withPosition(IntakeValues.retracted);

        isZeroed = false;
        spikeTime = new Debouncer(IntakeLimits.duration);
    }

    @Override
    public void runRotation(double power) {
        rotationMotor.set(power);
    } 

    @Override
    public void stopRotation() {
        rotationMotor.stopMotor();
    } 

    @Override
    public void gotoPosition() {
        isZeroed = false;
        rotationMotor.setControl(targetPosition);
    }

    @Override
    public void setTargetPosition(IntakeStates position) {
        if (position == IntakeStates.RETRACTED) {
            targetPosition = new PositionVoltage(0).withPosition(IntakeValues.retracted);
        } else {
            targetPosition = new PositionVoltage(0).withPosition(IntakeValues.extended);
        }
    }

    @Override
    public double getPosition() {
        return rotationMotor.getPosition().getValueAsDouble();
    }

    @Override
    public boolean isExtended() {
        return (getPosition() > IntakeValues.extended-1);
    }

    @Override
    public boolean isRetracted() {
        return (getPosition() < IntakeValues.retracted+0.5);
    }
 
    @Override
    public double getSupplyCurrent() {
        return rotationMotor.getSupplyCurrent().getValueAsDouble();
    }

    @Override
    public double getStatorCurrent() {
        return rotationMotor.getStatorCurrent().getValueAsDouble();
    }

    @Override
    public void doZeroing() {
        if (!isZeroed) {
            rotationMotor.set(-0.1);
            if (spikeTime.calculate(getStatorCurrent() > HoodLimits.hardLimit)) {
                zeroRotation();
                isZeroed = true;
            }
        } else {
            rotationMotor.set(0.0);
        }
    }

    @Override
    public void zeroRotation() {
        rotationMotor.setPosition(0);
    }

    @Override
    public void periodic() {
        telemetry();
    }

    @Override
    public void telemetry() {
        SmartDashboard.putNumber("Rotation Target", targetPosition.Position);
        SmartDashboard.putNumber("Rotation Position", getPosition());
        SmartDashboard.putNumber("Rotation Supply Current", getSupplyCurrent());
        SmartDashboard.putNumber("Rotation Stator Current", getStatorCurrent());

        SmartDashboard.putNumber("state position", isExtended() ? 1 : 0);
    }
}
