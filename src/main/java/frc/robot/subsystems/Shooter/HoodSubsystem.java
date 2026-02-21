package frc.robot.subsystems.Shooter;


import com.ctre.phoenix6.controls.PositionVoltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class HoodSubsystem extends SubsystemBase {

    PositionVoltage targetPosition;

    public HoodSubsystem() {
        targetPosition = new PositionVoltage(0).withPosition(0);
    }

    public void runHood(double power) {} 

    public void enableHood() {}

    public void setTargetPosition(double position) {}

    public double getCurrent() { return 0; }

    public double getPosition() { return 0; }

    public void zeroHood() {}

    @Override
    public void periodic() { telemetry(); }

    public void telemetry() {}
}

