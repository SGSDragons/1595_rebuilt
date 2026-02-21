package frc.robot.subsystems.Shooter;

import com.ctre.phoenix6.controls.VelocityVoltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ShooterSubsystem extends SubsystemBase {

    VelocityVoltage targetVelocity;

    public ShooterSubsystem() {
        targetVelocity = new VelocityVoltage(0).withVelocity(0);
    }

    public void runShooter() {}

    public void setTargetVelocity(double velocity) {}

    public double getVelocity() { return 0; }

    @Override
    public void periodic() { telemetry(); }

    public void telemetry() {}
}
