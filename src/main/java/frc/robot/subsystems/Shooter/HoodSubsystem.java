package frc.robot.subsystems.Shooter;


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

    public HoodSubsystem() {}

    public void runHood(double power) {} 

    public void enableHood() {}

    public void setTargetPosition(double position) {}

    public double getCurrent() { return 0; }

    public double getPosition() { return 0; }

    public void zeroHood() {}

    public void periodic() { telemetry(); }

    public void telemetry() {}
}

