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
        rollerMotor.setNeutralMode(NeutralModeValue.Brake);

        var rollerConfig = new TalonFXConfiguration();

        rollerConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.StatorCurrentLimit = IntakeRollerLimits.statorLimit;
        rollerConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        rollerMotor.getConfigurator().apply(rollerConfig);

    }

    public void runRollers(double power) {
        rollerMotor.set(power);
    } 

    public void stopRollers() {
        rollerMotor.stopMotor();
    } 
 
    public double getCurrent() {
        return rollerMotor.getStatorCurrent().getValueAsDouble();
    }

    public void resetCurrentLimits() {
        var rollerConfig = new TalonFXConfiguration();

        rollerConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.StatorCurrentLimit = 100;
        rollerConfig.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;

        rollerMotor.getConfigurator().apply(rollerConfig);
    }
    
    @Override
    public void periodic() {
        telemetry();
    }

    public void telemetry() {
        SmartDashboard.putNumber("IntakeRoller Current", getCurrent());
    }
}
