package frc.robot.subsystems.Intake;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.Constants.HardwareID.IntakeIds;
import frc.robot.Constants.CurrentLimits.IntakeRollerLimits;;


public class IntakeRollerSubsystemReal extends IntakeRollerSubsystem {
    
    TalonFX rollerMotor;

    public IntakeRollerSubsystemReal() {
        rollerMotor = new TalonFX(IntakeIds.rollerCanId);
        rollerMotor.setNeutralMode(NeutralModeValue.Brake);

        var rollerConfig = new TalonFXConfiguration();

        rollerConfig.CurrentLimits.StatorCurrentLimitEnable = true;
        rollerConfig.CurrentLimits.StatorCurrentLimit = IntakeRollerLimits.maxLimit;

        rollerMotor.getConfigurator().apply(rollerConfig);

    }

    public void runRollers(double power) {
        rollerMotor.set(power);
    } 

    public void stopRotation() {
        rollerMotor.stopMotor();
    } 
 
    public double getCurrent() {
        return rollerMotor.getStatorCurrent().getValueAsDouble();
    }
    
    @Override
    public void periodic() {

    }

    public void telemetry() {
        
    }
}
