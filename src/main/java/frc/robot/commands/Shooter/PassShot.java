package frc.robot.commands.Shooter;

import org.ironmaple.simulation.Goal;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.GoalAim;
import frc.robot.subsystems.Shooter.Hood.HoodSubsystem;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystem;
import frc.robot.Constants.Aiming;


public class PassShot extends Command {

    private final ShooterSubsystem shooterSubsystem;
    private final HoodSubsystem hoodSubsystem;
    private final GoalAim goalAim;
    double targetSpeed;
    double targetPosition;

    public PassShot(ShooterSubsystem shooterSubsystem, HoodSubsystem hoodSubsystem, GoalAim goalAim) {
        this.shooterSubsystem = shooterSubsystem;
        this.hoodSubsystem = hoodSubsystem;
        this.goalAim = goalAim;

        addRequirements((Subsystem) shooterSubsystem);
        addRequirements((Subsystem) hoodSubsystem);
    }
    
    @Override
    public void initialize() {
        targetSpeed = Aiming.getWheelValue(Aiming.passingShooter);
        this.shooterSubsystem.setTargetVelocity(targetSpeed);

        targetPosition = Aiming.getHoodValue(Aiming.passingHood);
        this.hoodSubsystem.setTargetPosition(targetPosition);
    }

    @Override
    public void execute() {
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