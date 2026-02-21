package frc.robot.subsystems.Intake;

import com.ctre.phoenix6.controls.PositionVoltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class IntakeSubsystem extends SubsystemBase {

    PositionVoltage targetPosition;

    public IntakeSubsystem() {
        targetPosition = new PositionVoltage(0).withPosition(0);
    }

    public void runRotation(double power) {} 

    public void stopRotation() {} 

    public void gotoPosition() {}

    public void setTargetPosition(double position) {}

    public double getPosition() { return 0; }

    public boolean isExtended() { return false; }

    public boolean isRetracted() { return false; }
 
    public double getCurrent() { return 0; }

    public void zeroRotation() {}

    @Override
    public void periodic() { telemetry(); }

    public void telemetry() {}
}
