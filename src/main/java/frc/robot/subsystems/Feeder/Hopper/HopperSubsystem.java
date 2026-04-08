package frc.robot.subsystems.Feeder.Hopper;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class HopperSubsystem extends SubsystemBase {

    public HopperSubsystem() {}

    // public void runRollers(double power) {}

    public void runRollers(double hopperTargetVoltage, double spinnerVoltage) {}  

    public void stopRollers() {} 

    public double getSupplyCurrent() { return 0; }

    public double getStatorCurrent() { return 0; }

    public void resetCurrentLimits() {}

    public void periodic() { telemetry(); }

    public void telemetry() {}
}
