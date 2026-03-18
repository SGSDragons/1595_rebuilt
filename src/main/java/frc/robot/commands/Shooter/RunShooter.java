package frc.robot.commands.Shooter;

import org.ironmaple.simulation.Goal;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.GoalAim;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystem;
import frc.robot.Constants.TuningValues.ShooterValues;
import frc.robot.Constants.Aiming;


public class RunShooter extends Command {

    private final ShooterSubsystem shooterSubsystem;
    private double targetpower;

    public RunShooter(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        addRequirements((Subsystem) shooterSubsystem);
    }

    @Override
    public void initialize() {
        targetpower = ShooterValues.runSpeed/100;
    }

    @Override
    public void execute() {
        if (this.shooterSubsystem.getAverageVelocity() < ShooterValues.runSpeed) {
            this.shooterSubsystem.runAtPower(targetpower);
        }
        else {
            this.shooterSubsystem.runAtPower(0.0);
        }
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}