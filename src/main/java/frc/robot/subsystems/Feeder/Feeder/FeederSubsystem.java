package frc.robot.subsystems.Feeder.Feeder;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CurrentLimits.FeederLimits;


public class FeederSubsystem extends SubsystemBase {

    public FeederSubsystem() {}

    public void runRollers(double voltage) {} 

    public void stopRollers() {} 
 
    public double getSupplyCurrent() { return 0; }

    public double getStatorCurrent() { return 0; }

    public void increaseCurrentLimits() {}

    public void decreaseCurrentLimits() {}

    public void periodic() {}

    public void telemetry() {}
}
