package frc.robot.subsystems.Feeder.Feeder;


import org.opencv.features2d.Feature2D;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import frc.robot.Constants.HardwareID.FeederIds;
import frc.robot.Constants.CurrentLimits.FeederLimits;


public class FeederSubsystemReal extends FeederSubsystem {
    
    private TalonFX feederMotor;
    private VoltageOut targetVoltage;

    public FeederSubsystemReal() {
        feederMotor = new TalonFX(FeederIds.feederCanId);
        feederMotor.setNeutralMode(NeutralModeValue.Coast);

        var rollerConfig = new TalonFXConfiguration();

        rollerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.SupplyCurrentLimit = FeederLimits.supplyLimit;
        rollerConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        feederMotor.getConfigurator().apply(rollerConfig);

        targetVoltage = new VoltageOut(0.0);
        feederMotor.setControl(targetVoltage);
    }

    @Override
    public void runRollers(double voltage) {
        feederMotor.setControl(targetVoltage.withOutput(voltage));
    } 

    @Override
    public void stopRollers() {
        feederMotor.stopMotor();
    } 
 
    @Override
    public double getSupplyCurrent() {
        return feederMotor.getSupplyCurrent().getValueAsDouble();
    }

    @Override
    public double getStatorCurrent() {
        return feederMotor.getStatorCurrent().getValueAsDouble();
    }

    // // Make sure it's able to un jam balls but prevent it from drawing to much power when shooting
    // @Override
    // public void increaseCurrentLimits() {
    //     var rollerConfig = new TalonFXConfiguration();

    //     rollerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    //     rollerConfig.CurrentLimits.SupplyCurrentLimit = 50;
    //     rollerConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    //     feederMotor.getConfigurator().apply(rollerConfig);
    // }

    // @Override
    // public void decreaseCurrentLimits() {
    //     var rollerConfig = new TalonFXConfiguration();

    //     rollerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    //     rollerConfig.CurrentLimits.SupplyCurrentLimit = FeederLimits.supplyLimit;
    //     rollerConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    //     feederMotor.getConfigurator().apply(rollerConfig);
    // }
    
    @Override
    public void periodic() {
        telemetry();
    }

    @Override
    public void telemetry() {
        SmartDashboard.putNumber("Feeder Supply Current", getSupplyCurrent());
        SmartDashboard.putNumber("Feeder Stator Current", getStatorCurrent());

        SmartDashboard.putNumber("Feeder Velocity", feederMotor.getVelocity().getValueAsDouble());
    }
}
