package frc.robot.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.Constants.CurrentLimits.HoodLimits;

public class ZeroHood extends Command {

    private final HoodSubsystem hoodSubsystem;

    final double currentLimit = HoodLimits.currentLimit;
    double currentDraw;
    double time;
    double spikeStartTime;

    public ZeroHood(HoodSubsystem hoodSubsystem) {
        this.hoodSubsystem = hoodSubsystem;
        addRequirements(hoodSubsystem);
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
        return (time - spikeStartTime > HoodLimits.duration);
    }
}