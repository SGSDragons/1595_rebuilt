package frc.robot.subsystems.Feeder.Hopper;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.HardwareID.FeederIds;
import frc.robot.Constants.CurrentLimits.HopperLimits;;


public class HopperSubsystemReal extends HopperSubsystem {
    
    TalonFX hopperMotor;

    public HopperSubsystemReal() {
        hopperMotor = new TalonFX(FeederIds.hopperCanId);
        hopperMotor.setNeutralMode(NeutralModeValue.Brake);

        var rollerConfig = new TalonFXConfiguration();

        rollerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.SupplyCurrentLimit = HopperLimits.supplyLimit;

        hopperMotor.getConfigurator().apply(rollerConfig);

    }

    @Override
    public void runRollers(double power) {
        hopperMotor.set(power);
    } 

    @Override
    public void stopRollers() {
        hopperMotor.stopMotor();
    } 
 
    @Override
    public double getSupplyCurrent() {
        return hopperMotor.getSupplyCurrent().getValueAsDouble();
    }

    @Override
    public double getStatorCurrent() {
        return hopperMotor.getSupplyCurrent().getValueAsDouble();
    }

    // @Override
    // public void resetCurrentLimits() {
    //     var rollerConfig = new TalonFXConfiguration();

    //     rollerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    //     rollerConfig.CurrentLimits.SupplyCurrentLimit = 100;

    //     hopperMotor.getConfigurator().apply(rollerConfig);
    // }
    
    @Override
    public void periodic() {
        telemetry();
    }

    @Override

    public void telemetry() {
        SmartDashboard.putNumber("Hopper Supply Current", getSupplyCurrent());
        SmartDashboard.putNumber("Hopper Stator Current", getStatorCurrent());
    }
}
