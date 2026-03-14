package frc.robot.subsystems.Intake.Rotation;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Constants.HardwareID.IntakeIds;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.Constants.CurrentLimits.IntakeLimits;

public class IntakeSubsystemReal extends IntakeSubsystem {
    
    TalonFX rotationMotor;
    IntakeStates target = IntakeStates.ZEROED;
    boolean zeroed = false;
    double deadline = Double.POSITIVE_INFINITY; // No predefined time.

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
    }

    public void gotoRetracted() {
        if (target != IntakeStates.RETRACTED) {
            target = IntakeStates.RETRACTED;
            deadline = IntakeLimits.maxTravelTime + Timer.getFPGATimestamp();
        }
    }

    public void gotoExtended() {
        if (target != IntakeStates.EXTENDED) {
            target = IntakeStates.EXTENDED;
            deadline = IntakeLimits.maxTravelTime + Timer.getFPGATimestamp();
        }
    }

    public void gotoZeroed() {
        if (target != IntakeStates.ZEROED) {
            target = IntakeStates.ZEROED;
            deadline = IntakeLimits.duration + Timer.getFPGATimestamp();
        }
    }

    @Override
    public void periodic() {
        if (target == IntakeStates.RETRACTED) {
            doRetracting();
        }
        if (target == IntakeStates.ZEROED) {
            doZeroing();
        }
        if (target == IntakeStates.EXTENDED) {
            doExtending();
        }

        telemetry();
    }

    private void doZeroing() {
        if (zeroed) {
            // It's finished
            return;
        }

        double now = Timer.getFPGATimestamp();
        if (now < deadline) {
            rotationMotor.set(-0.1);
            if (getCurrent() < IntakeLimits.currentLimit) {
                // Assuming it's making progress and extend the deadline
                deadline = now + IntakeLimits.duration;
            }
        } else {
            // The current has been too high for too long. Assume it has
            // stalled and zeroing is complete
            deadline = Double.POSITIVE_INFINITY;
            zeroed = true;
            rotationMotor.stopMotor();
            rotationMotor.setPosition(0.0);
        }
    }

    private void doRetracting() {
        double now = Timer.getFPGATimestamp();
        if (getPosition() < target.position+0.5) {
            // Begin rezeroing
            gotoZeroed();
        } else if (now < deadline) {
            // Still retracting and hasn't taken too long.
            rotationMotor.setControl(new PositionVoltage(target.position));
        } else {
            // Abort and try to go back to extended.
            gotoExtended();
        }
    }

    private void doExtending() {
        if (getPosition() < target.position) {
            rotationMotor.setControl(new PositionVoltage(target.position));
        } else {
            deadline = Double.POSITIVE_INFINITY;
            rotationMotor.stopMotor();
        }
    }

    private double getPosition() {
        return rotationMotor.getPosition().getValueAsDouble();
    }

    private double getCurrent() {
        return rotationMotor.getStatorCurrent().getValueAsDouble();
    }

    private void telemetry() {
        var table = NetworkTableInstance.getDefault().getTable("Intake");
        table.getEntry("Rotation Target").setDouble(target.position);
        table.getEntry("Rotation Position").setDouble(getPosition());
        table.getEntry("Rotation Current").setDouble(getCurrent());
        table.getEntry("isExtended").setBoolean(getPosition() > IntakeStates.EXTENDED.position-1);
    }
}
