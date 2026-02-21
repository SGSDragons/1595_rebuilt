package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Shooter.ShooterSubsystemReal;
import frc.robot.Constants.TuningValues.ShooterValues;


public class RunShooter extends Command {

    public enum ShooterSpeed {
        FAST,
        SLOW
    }

    private final ShooterSubsystemReal shooterSubsystem;
    private final ShooterSpeed speed;
    public boolean autoRun;

    public RunShooter(ShooterSubsystemReal shooterSubsystem, ShooterSpeed speed) {
        this.shooterSubsystem = shooterSubsystem;
        this.speed = speed;
        addRequirements(shooterSubsystem);
    }

    // Set target speed
    @Override
    public void initialize() {  
        if (speed == ShooterSpeed.FAST) {
            this.shooterSubsystem.setTargetVelocity(ShooterValues.fastSpeed);
        }
        else {
            this.shooterSubsystem.setTargetVelocity(ShooterValues.slowSpeed);
        }
    }

    @Override
    public void execute() {
        this.shooterSubsystem.runShooter();
    }

    @Override
    public void end(boolean interrupted) {
        if (autoRun) {
            this.shooterSubsystem.setTargetVelocity(0);
        }
        else {
            this.shooterSubsystem.setTargetVelocity(ShooterValues.slowSpeed);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}