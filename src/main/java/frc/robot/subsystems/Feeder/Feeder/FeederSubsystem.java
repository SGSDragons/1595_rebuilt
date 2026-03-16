package frc.robot.subsystems.Feeder.Feeder;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class FeederSubsystem extends SubsystemBase {

    public FeederSubsystem() {}

    public void runRollers(double power) {} 

    public void stopRotation() {} 
 
    public double getCurrent() { return 0; }

    public void resetCurrentLimits() {}

    public void periodic() {}

    public void telemetry() {}
}
