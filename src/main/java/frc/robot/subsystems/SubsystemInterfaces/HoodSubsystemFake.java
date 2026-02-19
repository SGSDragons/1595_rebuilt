package frc.robot.subsystems.SubsystemInterfaces;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareID.HoodIds;
import frc.robot.Constants.TuningValues.HoodValues;
import frc.robot.Constants.CurrentLimits.HoodLimits;

public class HoodSubsystemFake extends SubsystemBase {

    PositionVoltage targetPosition;

    public HoodSubsystemFake() {
        targetPosition = new PositionVoltage(0).withPosition(0);
    }

    public void runHood(double power) {} 

    public void enableHood() {}

    public void setTargetPosition(double position) {}

    public double getCurrent() { return 0; }

    public double getPosition() { return 0; }

    public void zeroHood() {}

    @Override
    public void periodic() {}
}

