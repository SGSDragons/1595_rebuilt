package frc.robot.commands.Shooter;

import org.ironmaple.simulation.Goal;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.GoalAim;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystem;
import frc.robot.Constants.TuningValues.ShooterValues;
import frc.robot.Constants.Aiming;


public class RunShooter extends Command {

    private final ShooterSubsystem shooterSubsystem;
    private final GoalAim goalAim;
    private final boolean useAutoSpeed;
    double targetspeed;

    public RunShooter(ShooterSubsystem shooterSubsystem, GoalAim goalAim, boolean useAutoSpeed) {
        this.shooterSubsystem = shooterSubsystem;
        this.goalAim = goalAim;
        this.useAutoSpeed = useAutoSpeed;
        addRequirements((Subsystem) shooterSubsystem);
    }

    // Set target speed to interpolation value if we want to use it, else set it to default speed
    @Override
    public void initialize() {  
        if (this.useAutoSpeed) {
            targetspeed = Aiming.getWheelValue(this.goalAim.getAdjustedDistance());
        } 
        else {
            targetspeed = ShooterValues.runSpeed;
        }
    }

    // run shooter and keep adjusting it if useAutoSpeed is true
    @Override
    public void execute() {
        if (this.useAutoSpeed) {
            targetspeed = Aiming.getWheelValue(this.goalAim.getAdjustedDistance());
        }
        this.shooterSubsystem.runShooter();
    }

    // ends when button is released
    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}