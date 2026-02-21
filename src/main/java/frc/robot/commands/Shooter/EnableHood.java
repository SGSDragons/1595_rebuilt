package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.GoalAim;
import frc.robot.subsystems.Shooter.HoodSubsystemReal;
import frc.robot.Constants.TuningValues.ShooterValues.AngleFunction;


public class EnableHood extends Command {

    private final HoodSubsystemReal hoodSubsystem;
    private final GoalAim goalAim;
    private double targetPosition;

    public EnableHood(HoodSubsystemReal hoodSubsystem, GoalAim goalAim) {
        this.hoodSubsystem = hoodSubsystem;
        this.goalAim = goalAim;
        addRequirements(hoodSubsystem);
    }

    // set target position based on distance to the goal
    @Override
    public void initialize() {
        targetPosition = AngleFunction.calculate(goalAim.getDistance());
        this.hoodSubsystem.setTargetPosition(targetPosition);
    }

    // always try to reach position
    @Override
    public void execute() {
        targetPosition = AngleFunction.calculate(goalAim.getDistance());
        this.hoodSubsystem.setTargetPosition(targetPosition);
        this.hoodSubsystem.enableHood();
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