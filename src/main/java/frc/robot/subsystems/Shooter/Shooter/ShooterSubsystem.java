package frc.robot.subsystems.Shooter.Shooter;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class ShooterSubsystem extends SubsystemBase {

    public ShooterSubsystem() {}

    public void runAtPower(double power) {}

    public void runShooter() {}

    public void setTargetVelocity(double velocity) {}

    public void FFkick(double volts, double seconds) {}

    public boolean nearTargetSpeed() { return false; }

    public double getLeftVelocity() { return 0; }

    public double getRightVelocity() { return 0; }

    public double getAverageVelocity() { return 0; }

    public double getSupplyCurrent() { return 0; }

    public double getStatorCurrent() { return 0; }

    public void resetCurrentLimits() {}

    public void periodic() { telemetry(); }

    public void telemetry() {}
}
