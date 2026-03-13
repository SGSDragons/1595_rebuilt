package frc.robot.subsystems.Intake.Rotation;

import com.ctre.phoenix6.controls.PositionVoltage;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class IntakeSubsystem extends SubsystemBase {

    public enum IntakeStates {
        EXTENDED,
        RETRACTED,
        ZEROED
    }

    public PositionVoltage targetPosition;

    public IntakeSubsystem() {}

    public void runRotation(double power) {} 

    public void stopRotation() {} 

    public void gotoPosition() {}

    public void setTargetPosition(IntakeStates position) {}

    public double getPosition() { return 0; }

    public boolean isExtended() { return false; }

    public boolean isRetracted() { return false; }
 
    public double getCurrent() { return 0; }

    public void zeroRotation() {}

    public void periodic() { telemetry(); }

    public void telemetry() {}
}
