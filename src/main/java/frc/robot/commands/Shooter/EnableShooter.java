package frc.robot.commands.Shooter;

import org.ironmaple.simulation.Goal;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.GoalAim;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystem;
import frc.robot.Constants.TuningValues.ShooterValues;
import frc.robot.Constants.Aiming;


public class EnableShooter extends Command {

    private final ShooterSubsystem shooterSubsystem;
    private final GoalAim goalAim;
    double targetspeed;

    public EnableShooter(ShooterSubsystem shooterSubsystem, GoalAim goalAim) {
        this.shooterSubsystem = shooterSubsystem;
        this.goalAim = goalAim;
        addRequirements((Subsystem) shooterSubsystem);
    }

    // Set target speed to interpolation value
    @Override
    public void initialize() {
        targetspeed = Aiming.getWheelValue(this.goalAim.getAdjustedDistance());
        this.shooterSubsystem.setTargetVelocity(targetspeed);
        // System.out.println("Shooter enabled");  
    }

    @Override
    public void execute() {
        targetspeed = Aiming.getWheelValue(this.goalAim.getAdjustedDistance());
        this.shooterSubsystem.setTargetVelocity(targetspeed);
        this.shooterSubsystem.runShooter();
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}