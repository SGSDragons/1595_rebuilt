package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.CurrentLimits.IntakeLimits;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem;

public class ZeroIntake extends Command {

    private final IntakeSubsystem intakeSubsystem;

    final double currentLimit = IntakeLimits.currentLimit;
    double currentDraw;
    double time;
    double spikeStartTime;
    boolean hasZeroed;

    public ZeroIntake(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        hasZeroed = false;

        addRequirements((Subsystem) intakeSubsystem);
    }

    @Override
    public void initialize() {  
        currentDraw = this.intakeSubsystem.getCurrent();
        time = Timer.getFPGATimestamp();
    }

    // Slowly run intake and record stator current
    @Override
    public void execute() {
        if (!hasZeroed) {
            currentDraw = this.intakeSubsystem.getCurrent();
            time = Timer.getFPGATimestamp();
            this.intakeSubsystem.runRotation(0.1);

            if (currentDraw < currentLimit) {
                spikeStartTime = time;
            }

            // Detect when stator current is above threshold for enough time
            // When stator current is above threshold for enough time, stop motor and zero the motor
            if (time - spikeStartTime > IntakeLimits.duration) {
                hasZeroed = true;
                this.intakeSubsystem.zeroRotation();
            }
        }
        else {
            this.intakeSubsystem.runRotation(0);
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