package frc.robot.subsystems.Intake.Rotation;

import com.ctre.phoenix6.controls.PositionVoltage;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class IntakeSubsystem extends SubsystemBase {

    public enum IntakeStates {
        EXTENDED(9.0),
        RETRACTED(1.5),
        ZEROED(0.0);

        public final double position;
        IntakeStates(double p) {
            position = p;
        }

    }

    public PositionVoltage targetPosition;

    public IntakeSubsystem() {}

    public void gotoRetracted() {}
    public void gotoExtended() {}
    public void gotoZeroed() {}
}
