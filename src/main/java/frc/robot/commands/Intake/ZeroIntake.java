package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.CurrentLimits.IntakeLimits;
import frc.robot.subsystems.Intake.IntakeSubsystem;

public class ZeroIntake extends Command {

    private final IntakeSubsystem intakeSubsystem;

    final double currentLimit = IntakeLimits.currentLimit;
    double currentDraw;
    double time;
    double spikeStartTime;

    public ZeroIntake(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        addRequirements(intakeSubsystem);
    }

    @Override
    public void initialize() {  
        currentDraw = this.intakeSubsystem.getCurrent();
        time = Timer.getFPGATimestamp();
    }

    // Slowly run intake and record stator current
    @Override
    public void execute() {
        currentDraw = this.intakeSubsystem.getCurrent();
        this.intakeSubsystem.runRotation(-0.1);

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
        this.intakeSubsystem.runRotation(0);
        this.intakeSubsystem.zeroRotation();
    }

    // Detect when stator current is above threshold for enough time
    @Override
    public boolean isFinished() {
        return (time - spikeStartTime > IntakeLimits.duration);
    }
}