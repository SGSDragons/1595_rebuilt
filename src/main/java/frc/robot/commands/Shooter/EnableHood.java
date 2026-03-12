package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.GoalAim;
import frc.robot.subsystems.Shooter.Hood.HoodSubsystem;
import frc.robot.Constants.Aiming;


public class EnableHood extends Command {

    private final HoodSubsystem hoodSubsystem;
    private final GoalAim goalAim;
    private double targetPosition;

    public EnableHood(HoodSubsystem hoodSubsystem, GoalAim goalAim) {
        this.hoodSubsystem = hoodSubsystem;
        this.goalAim = goalAim;
        addRequirements((Subsystem) hoodSubsystem);
    }

    // set target position based on distance to the goal
    @Override
    public void initialize() {
        targetPosition = Aiming.getHoodValue(this.goalAim.getAdjustedDistance());
        this.hoodSubsystem.setTargetPosition(targetPosition);
    }

    // always try to reach position
    @Override
    public void execute() {
        targetPosition = Aiming.getHoodValue(this.goalAim.getAdjustedDistance());
        this.hoodSubsystem.setTargetPosition(targetPosition);

        // if it's close enough prevent hood from oscilating 
        if (this.hoodSubsystem.atTargetPosition()) {
            this.hoodSubsystem.runHood(0);
        }
        else {
            this.hoodSubsystem.enableHood();
        }
    }

    // ends when button is released and runs zero command
    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}