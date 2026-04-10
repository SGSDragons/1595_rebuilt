package frc.robot.commands.Shooter;

import org.ironmaple.simulation.Goal;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.GoalAim;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystem;
import frc.robot.Constants.Aiming;


public class EnableShooter extends Command {

    private final ShooterSubsystem shooterSubsystem;
    private final GoalAim goalAim;
    private double targetspeed;
    private boolean addExtra;

    public EnableShooter(ShooterSubsystem shooterSubsystem, GoalAim goalAim, boolean addExtra) {
        this.shooterSubsystem = shooterSubsystem;
        this.goalAim = goalAim;
        this.addExtra = addExtra;
        addRequirements((Subsystem) shooterSubsystem);
    }

    // Set target speed to interpolation value
    @Override
    public void initialize() {
        if (addExtra) {
            targetspeed = Aiming.getWheelValue(this.goalAim.getAdjustedDistance()+1.0);
        }
        else {
            targetspeed = Aiming.getWheelValue(this.goalAim.getAdjustedDistance());
        }
        
        this.shooterSubsystem.setTargetVelocity(targetspeed);
        // System.out.println("Shooter enabled");  
    }

    @Override
    public void execute() {
        if (addExtra) {
            targetspeed = Aiming.getWheelValue(this.goalAim.getAdjustedDistance()+1.0);
        }
        else {
            targetspeed = Aiming.getWheelValue(this.goalAim.getAdjustedDistance());
        }

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