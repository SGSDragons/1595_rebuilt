package frc.robot.subsystems.SubsystemInterfaces;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.HardwareID.FeederIds;
import frc.robot.Constants.CurrentLimits.HopperLimits;;


public class HopperSubsystemFake extends SubsystemBase {

    public HopperSubsystemFake() {}

    public void runRollers(double power) {} 

    public void stopRotation() {} 
 
    public double getCurrent() { return 0; }

    @Override
    public void periodic() {}
}
