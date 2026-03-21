package frc.robot.subsystems.Intake.Roller;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareID.IntakeIds;
import frc.robot.Constants.CurrentLimits.IntakeRollerLimits;;


public class IntakeRollerSubsystemReal extends IntakeRollerSubsystem {
    
    TalonFX rollerMotor;

    public IntakeRollerSubsystemReal() {
        rollerMotor = new TalonFX(IntakeIds.rollerCanId);
        rollerMotor.setNeutralMode(NeutralModeValue.Coast);

        var rollerConfig = new TalonFXConfiguration();

        rollerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.SupplyCurrentLimit = IntakeRollerLimits.supplyLimit;
        rollerConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        rollerMotor.getConfigurator().apply(rollerConfig);

    }

    @Override
    public void runRollers(double power) {
        rollerMotor.set(power);
    } 

    @Override
    public void stopRollers() {
        rollerMotor.stopMotor();
    } 
 
    @Override
    public double getSupplyCurrent() {
        return rollerMotor.getSupplyCurrent().getValueAsDouble();
    }

    @Override
    public double getStatorCurrent() {
        return rollerMotor.getStatorCurrent().getValueAsDouble();
    }

    // @Override
    // public void resetCurrentLimits() {
    //     var rollerConfig = new TalonFXConfiguration();

    //     rollerConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
    //     rollerConfig.CurrentLimits.SupplyCurrentLimit = IntakeRollerLimits.supplyLimit;
    //     rollerConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

    //     rollerMotor.getConfigurator().apply(rollerConfig);
    // }
    
    @Override
    public void periodic() {
        telemetry();
    }

    @Override
    public void telemetry() {
        SmartDashboard.putNumber("IntakeRoller Supply Current", getSupplyCurrent());
        SmartDashboard.putNumber("IntakeRoller Stator Current", getStatorCurrent());
    }
}
