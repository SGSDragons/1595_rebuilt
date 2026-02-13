package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj.motorcontrol.Talon;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareID.IntakeIds;
import frc.robot.Constants.TuningValues.IntakeValues;

public class IntakeSubsystem extends SubsystemBase {
    
    TalonFX rotationMotor;
    TalonFX rollerMotor;

    VelocityVoltage targetVelocity;
    PositionVoltage targetPosition;

    public enum intakePosition {
        EXTENDED,
        RETRACTED
    }

    public IntakeSubsystem() {
        rollerMotor = new TalonFX(IntakeIds.rollerCanId);
        rollerMotor.setNeutralMode(NeutralModeValue.Brake);

        rotationMotor = new TalonFX(IntakeIds.rotationCanId);
        rotationMotor.setNeutralMode(NeutralModeValue.Brake);

        var rotationConfig = new Slot0Configs();
        rotationConfig.withStaticFeedforwardSign(StaticFeedforwardSignValue.UseClosedLoopSign);
        rotationConfig.kS = IntakeValues.kS;
        rotationConfig.kG = IntakeValues.kG;
        rotationConfig.kP = IntakeValues.kP;
        rotationConfig.kI = IntakeValues.kI;
        rotationConfig.kD = IntakeValues.kD;

        rotationMotor.getConfigurator().apply(rotationConfig);
        targetPosition = new PositionVoltage(0).withPosition(0);
    }

    public void runRollers(double power) {
        rollerMotor.set(power);
    } 

    public void runRotation(double power) {
        rotationMotor.set(power);
    } 

    public void gotoPosition() {
        rotationMotor.setControl(targetPosition);
    }

    public void setTargetPosition(double position) {
        targetPosition = new PositionVoltage(0).withPosition(position);
    }

    public double getCurrent() {
        return rotationMotor.getStatorCurrent().getValueAsDouble();
    }

    public void zeroRotation() {
        rotationMotor.setPosition(0);
    }
}
