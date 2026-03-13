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
    double targetspeed;

    public RunShooter(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        addRequirements((Subsystem) shooterSubsystem);
    }

    @Override
    public void initialize() {
        targetspeed = ShooterValues.runSpeed;
        this.shooterSubsystem.setTargetVelocity(targetspeed);
    }

    @Override
    public void execute() {
        this.shooterSubsystem.runShooter();
    }

    // default command
    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}