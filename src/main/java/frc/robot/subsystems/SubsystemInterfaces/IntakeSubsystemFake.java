package frc.robot.subsystems.SubsystemInterfaces;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareID.IntakeIds;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.Constants.CurrentLimits.IntakeLimits;

public class IntakeSubsystemFake extends SubsystemBase {

    PositionVoltage targetPosition;

    public IntakeSubsystemFake() {
        targetPosition = new PositionVoltage(0).withPosition(0);
    }

    public void runRotation(double power) {} 

    public void stopRotation() {} 

    public void gotoPosition() {}

    public void setTargetPosition(double position) {}

    public double getPosition() { return 0; }

    public boolean isExtended() { return false; }

    public boolean isRetracted() { return false; }
 
    public double getCurrent() { return 0; }

    public void zeroRotation() {}

    @Override
    public void periodic() {}
}
