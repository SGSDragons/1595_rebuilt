package frc.robot.subsystems.Feeder.Hopper;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.HardwareID.FeederIds;
import frc.robot.Constants.CurrentLimits.HopperLimits;
import frc.robot.Constants.CurrentLimits.SpinnerLimits;;


public class HopperSubsystemTwo extends HopperSubsystem {
    
    private TalonFX hopperMotor;
    private TalonFX rightSpinner;
    private TalonFX spinner;

    private VoltageOut hopperTargetVoltage;
    private VoltageOut spinnerTargetVoltage;

    public HopperSubsystemTwo() {
        hopperMotor = new TalonFX(FeederIds.hopperCanId);
        hopperMotor.setNeutralMode(NeutralModeValue.Coast);

        var rollerConfig = new TalonFXConfiguration();
        rollerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.SupplyCurrentLimit = HopperLimits.supplyLimit;
        hopperMotor.getConfigurator().apply(rollerConfig);

        spinner = new TalonFX(FeederIds.leftSpinnerCanId);
        rightSpinner = new TalonFX(FeederIds.rightSpinnerCanId);
        rightSpinner.setControl(new Follower(spinner.getDeviceID(), MotorAlignmentValue.Opposed));
        spinner.setNeutralMode(NeutralModeValue.Coast);

        var spinnerConfig = new TalonFXConfiguration();
        spinnerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        spinnerConfig.CurrentLimits.SupplyCurrentLimit = SpinnerLimits.supplyLimit;
        spinner.getConfigurator().apply(spinnerConfig);

        var rightSpinnerConfig = new TalonFXConfiguration();
        rightSpinnerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        rightSpinnerConfig.CurrentLimits.SupplyCurrentLimit = SpinnerLimits.supplyLimit;
        rightSpinner.getConfigurator().apply(rightSpinnerConfig);

        hopperTargetVoltage = new VoltageOut(0.0);
        hopperMotor.setControl(hopperTargetVoltage);

        spinnerTargetVoltage = new VoltageOut(0.0);
        spinner.setControl(spinnerTargetVoltage);        
    }

    @Override
    public void runRollers(double hopperVoltage, double spinnerVoltage) {
        hopperMotor.setControl(hopperTargetVoltage.withOutput(hopperVoltage));
        spinner.setControl(spinnerTargetVoltage.withOutput(spinnerVoltage));
    } 

    @Override
    public void stopRollers() {
        hopperMotor.stopMotor();
        spinner.stopMotor();
    } 
 
    @Override
    public double getSupplyCurrent() {
        return hopperMotor.getSupplyCurrent().getValueAsDouble() + spinner.getSupplyCurrent().getValueAsDouble() + rightSpinner.getSupplyCurrent().getValueAsDouble();
    }

    @Override
    public double getStatorCurrent() {
        return hopperMotor.getSupplyCurrent().getValueAsDouble() + spinner.getSupplyCurrent().getValueAsDouble() + rightSpinner.getSupplyCurrent().getValueAsDouble();
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

        SmartDashboard.putNumber("Hopper Velocity", hopperMotor.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("Spinner Velocity", spinner.getVelocity().getValueAsDouble());
    }
}
