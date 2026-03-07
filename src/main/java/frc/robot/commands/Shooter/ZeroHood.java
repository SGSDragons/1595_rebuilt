package frc.robot.commands.Shooter;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Shooter.Hood.HoodSubsystem;
import frc.robot.Constants.CurrentLimits.HoodLimits;

public class ZeroHood extends Command {

    private final HoodSubsystem hoodSubsystem;

    final double currentLimit = HoodLimits.currentLimit;
    double currentDraw;
    double time;
    double spikeStartTime;
    boolean hasZeroed;

    public ZeroHood(HoodSubsystem hoodSubsystem) {
        this.hoodSubsystem = hoodSubsystem;
        hasZeroed = false;

        addRequirements((Subsystem) hoodSubsystem);
    }

    @Override
    public void initialize() {  
        currentDraw = this.hoodSubsystem.getCurrent();
        time = Timer.getFPGATimestamp();
    }

    // Slowly run hood down and record stator current
    @Override
    public void execute() {
        if (!hasZeroed) {
            currentDraw = this.hoodSubsystem.getCurrent();
            time = Timer.getFPGATimestamp();
            this.hoodSubsystem.runHood(-0.1);

            if (currentDraw < currentLimit) {
                spikeStartTime = time;
            }

            // Detect when stator current is above threshold for enough time
            // When stator current is above threshold for enough time, stop motor and zero the hood
            if (time - spikeStartTime > HoodLimits.duration) {
                hasZeroed = true;
                this.hoodSubsystem.zeroHood();
            }
        }
        else {
            this.hoodSubsystem.runHood(0);
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