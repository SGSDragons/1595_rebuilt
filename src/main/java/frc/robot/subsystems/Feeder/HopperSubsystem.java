package frc.robot.subsystems.Feeder;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.HardwareID.FeederIds;
import frc.robot.Constants.CurrentLimits.HopperLimits;;


public class HopperSubsystem extends SubsystemBase {
    
    TalonFX hopperMotor;

    public HopperSubsystem() {
        hopperMotor = new TalonFX(FeederIds.hopperCanId);
        hopperMotor.setNeutralMode(NeutralModeValue.Brake);

        var rollerConfig = new TalonFXConfiguration();

        rollerConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.StatorCurrentLimit = HopperLimits.maxLimit;

        hopperMotor.getConfigurator().apply(rollerConfig);

    }

    public void runRollers(double power) {
        hopperMotor.set(power);
    } 

    public void stopRotation() {
        hopperMotor.stopMotor();
    } 
 
    public double getCurrent() {
        return hopperMotor.getStatorCurrent().getValueAsDouble();
    }
    
    @Override
    public void periodic() {

    }

    public void telemetry() {
        
    }
}
