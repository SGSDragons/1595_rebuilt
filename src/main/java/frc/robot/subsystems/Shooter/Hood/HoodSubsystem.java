package frc.robot.subsystems.Shooter.Hood;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CurrentLimits.HoodLimits;
import frc.robot.Constants.HardwareID.HoodIds;
import frc.robot.Constants.TuningValues.HoodValues;


public class HoodSubsystem extends SubsystemBase {

    public enum HoodStates {
        ENABLED,
        ZEROED
    }

    public HoodSubsystem() {}

    public void runHood(double power) {} 

    public void enableHood() {}

    public void setTargetPosition(double position) {}

    public boolean atTargetPosition() { return false; }

    public double getSupplyCurrent() { return 0; }

    public double getStatorCurrent() { return 0; }

    public double getPosition() { return 0; }

    public void doZeroing() {}

    public void zeroHood() {}

    public void periodic() { telemetry(); }

    public void telemetry() {}
}

