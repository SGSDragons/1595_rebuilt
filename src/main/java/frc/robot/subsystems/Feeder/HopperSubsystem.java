package frc.robot.subsystems.Feeder;

import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class HopperSubsystem extends SubsystemBase {

    public HopperSubsystem() {}

    public void runRollers(double power) {} 

    public void stopRotation() {} 
 
    public double getCurrent() { return 0; }

    @Override
    public void periodic() { telemetry(); }

    public void telemetry() {}
}
