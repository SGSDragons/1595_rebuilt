package frc.robot.subsystems.SubsystemInterfaces;


import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.HardwareID.ShooterIds;
import frc.robot.Constants.TuningValues.ShooterValues;
import frc.robot.Constants.CurrentLimits.ShooterLimits;


public class ShooterSubsystemFake extends SubsystemBase {

    VelocityVoltage targetVelocity;

    public ShooterSubsystemFake() {
        targetVelocity = new VelocityVoltage(0).withVelocity(ShooterValues.slowSpeed);
    }

    public void runShooter() {}

    public void setTargetVelocity(double velocity) {}

    public double getVelocity() { return 0; }
}
