package frc.robot.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.Constants.TuningValues.HoodValues;;

public class RunShooter extends Command {

    public enum Speed {
        FAST,
        MEDIUM,
        SLOW
    }

    private final ShooterSubsystem shooterSubsystem;

    public ZeroHood(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
        addRequirements(shooterSubsystem);
    }

    @Override
    public void initialize() {  
        currentDraw = this.hoodSubsystem.getCurrent();
        time = Timer.getFPGATimestamp();
    }

    // Slowly run hood down and record stator current
    @Override
    public void execute() {
        currentDraw = this.hoodSubsystem.getCurrent();
        this.hoodSubsystem.runHood(-0.1);

        if (currentDraw > currentLimit) {
            if (spikeStartTime < 0) {
                spikeStartTime = time;
            }
        }
        else {
            spikeStartTime = -1;
        }
    }

    // When stator current is above threshold for enough time, stop motor and zero the hood
    @Override
    public void end(boolean interrupted) {
        this.hoodSubsystem.runHood(0);
        this.hoodSubsystem.zeroHood();
    }

    // Detect when stator current is above threshold for enough time
    @Override
    public boolean isFinished() {
        return (time - spikeStartTime > HoodValues.duration);
    }
}