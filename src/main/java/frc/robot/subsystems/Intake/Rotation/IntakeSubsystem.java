package frc.robot.subsystems.Intake.Rotation;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.controls.PositionVoltage;

import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CurrentLimits.HoodLimits;
import frc.robot.Constants.TuningValues.IntakeValues;


public class IntakeSubsystem extends SubsystemBase {

    public enum IntakeStates {
        EXTENDED,
        RETRACTED,
        ZEROED
    }

    public PositionVoltage targetPosition;

    public IntakeSubsystem() {}

    public void runRotation(double power) {} 

    public void stopRotation() {} 

    public void gotoPosition() {}

    public void setTargetPosition(IntakeStates position) {}

    public void increasePID() {}

    public void decreasePID() {}

    public double getPosition() { return 0; }

    public boolean isExtended() { return false; }

    public boolean isRetracted() { return false; }
 
    public double getSupplyCurrent() { return 0; }

    public double getStatorCurrent() { return 0; }

    public void doZeroing() {}

    public void zeroRotation() {}

    public void setUp() {}

    public void setDown() {}

    public void periodic() { telemetry(); }

    public void telemetry() {}
}
