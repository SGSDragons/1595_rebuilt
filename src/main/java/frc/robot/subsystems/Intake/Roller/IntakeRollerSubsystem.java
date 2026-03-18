package frc.robot.subsystems.Intake.Roller;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class IntakeRollerSubsystem extends SubsystemBase {

    public enum IntakeRollerSpeeds {
        FAST,
        SLOW,
        REVERSE
    }

    public IntakeRollerSubsystem() {}

    public void runRollers(double power) {} 

    public void stopRollers() {} 
    
    public double getSupplyCurrent() { return 0; }

    public double getStatorCurrent() { return 0; }

    public void resetCurrentLimits() {}

    public void periodic() { telemetry(); }

    public void telemetry() {}
}
