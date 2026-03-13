package frc.robot.commands.Shooter;

import org.ironmaple.simulation.Goal;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.GoalAim;
import frc.robot.subsystems.Shooter.Hood.HoodSubsystem;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystem;
import frc.robot.Constants.TuningValues.ShooterValues;
import frc.robot.Constants.Aiming;


public class CloseShot extends Command {

    private final ShooterSubsystem shooterSubsystem;
    private final HoodSubsystem hoodSubsystem;
    private final GoalAim goalAim;
    double targetSpeed;
    double targetPosition;

    public CloseShot(ShooterSubsystem shooterSubsystem, HoodSubsystem hoodSubsystem, GoalAim goalAim) {
        this.shooterSubsystem = shooterSubsystem;
        this.hoodSubsystem = hoodSubsystem;
        this.goalAim = goalAim;

        addRequirements((Subsystem) shooterSubsystem);
        addRequirements((Subsystem) hoodSubsystem);
    }

    // Set target speed and hood to set value
    @Override
    public void initialize() {
        targetSpeed = Aiming.getWheelValue(Aiming.closeShot);
        this.shooterSubsystem.setTargetVelocity(targetSpeed);

        targetPosition = Aiming.getHoodValue(Aiming.closeShot);
        this.hoodSubsystem.setTargetPosition(targetPosition);
    }

    @Override
    public void execute() {
        targetSpeed = Aiming.getWheelValue(this.goalAim.getAdjustedDistance());

        this.shooterSubsystem.setTargetVelocity(targetSpeed);
        this.shooterSubsystem.runShooter();

        this.hoodSubsystem.setTargetPosition(targetPosition);
        if (this.hoodSubsystem.atTargetPosition()) {
            this.hoodSubsystem.runHood(0);
        }
        else {
            this.hoodSubsystem.enableHood();
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