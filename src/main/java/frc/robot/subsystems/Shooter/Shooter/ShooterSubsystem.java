package frc.robot.subsystems.Shooter.Shooter;
import java.util.function.BooleanSupplier;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.signals.StaticFeedforwardSignValue;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.CurrentLimits.ShooterLimits;
import frc.robot.Constants.HardwareID.ShooterIds;
import frc.robot.Constants.TuningValues.ShooterValues;


public class ShooterSubsystem extends SubsystemBase {

    public ShooterSubsystem() {}

    public void runAtPower(double power) {}

    public void runShooter() {}

    public void setTargetVelocity(double velocity) {}

    public boolean nearTargetSpeed() { return false; }

    public double getLeftVelocity() { return 0; }

    public double getRightVelocity() { return 0; }

    public double getAverageVelocity() { return 0; }

    public double getSupplyCurrent() { return 0; }

    public double getStatorCurrent() { return 0; }

    public void resetCurrentLimits() {}

    public void periodic() { telemetry(); }

    public void telemetry() {}
}
