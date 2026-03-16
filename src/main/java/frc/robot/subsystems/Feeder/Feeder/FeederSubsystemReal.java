package frc.robot.subsystems.Feeder.Feeder;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants.HardwareID.FeederIds;
import frc.robot.Constants.CurrentLimits.FeederLimits;


public class FeederSubsystemReal extends FeederSubsystem {
    
    TalonFX feederMotor;

    public FeederSubsystemReal() {
        feederMotor = new TalonFX(FeederIds.feederCanId);
        feederMotor.setNeutralMode(NeutralModeValue.Brake);

        var rollerConfig = new TalonFXConfiguration();

        rollerConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.StatorCurrentLimit = FeederLimits.statorLimit;
        rollerConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

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

    public void resetCurrentLimits() {
        var rollerConfig = new TalonFXConfiguration();

        rollerConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.StatorCurrentLimit = 100;
        rollerConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        feederMotor.getConfigurator().apply(rollerConfig);
    }
    
    @Override
    public void periodic() {
        telemetry();
    }

    public void telemetry() {
        SmartDashboard.putNumber("Feeder Current", getCurrent());
    }
}
