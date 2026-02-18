package frc.robot.subsystems.Feeder;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.HardwareID.FeederIds;
import frc.robot.Constants.CurrentLimits.FeederLimits;
import frc.robot.subsystems.SubsystemInterfaces.FeederSubsystemFake;


public class FeederSubsystem {
    
    TalonFX feederMotor;

    public FeederSubsystem() {
        feederMotor = new TalonFX(FeederIds.hopperCanId);
        feederMotor.setNeutralMode(NeutralModeValue.Brake);

        var rollerConfig = new TalonFXConfiguration();

        rollerConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.StatorCurrentLimit = FeederLimits.maxLimit;

        feederMotor.getConfigurator().apply(rollerConfig);
    }

    public void runRollers(double power) {
        feederMotor.set(power);
    } 

    public void stopRotation() {
        feederMotor.stopMotor();
    } 
 
    public double getCurrent() {
        return feederMotor.getStatorCurrent().getValueAsDouble();
    }
}
