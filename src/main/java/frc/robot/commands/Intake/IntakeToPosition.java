package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.CurrentLimits.IntakeLimits;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem.IntakeStates;


public class IntakeToPosition extends Command {

    private final IntakeSubsystem intakeSubsystem;
    private IntakeStates position;
    
    final double currentLimit = IntakeLimits.currentLimit;
    double currentDraw;
    double time;
    double spikeStartTime;
    boolean hasZeroed;

    public IntakeToPosition(IntakeSubsystem intake, IntakeStates position) {
        this.intakeSubsystem = intake;
        this.position = position;
        addRequirements((Subsystem) intake);
    }

    // Set target speed
    @Override
    public void initialize() {  
        this.intakeSubsystem.setTargetPosition(this.position);
        currentDraw = this.intakeSubsystem.getCurrent();
        time = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        this.intakeSubsystem.gotoPosition();

        currentDraw = this.intakeSubsystem.getCurrent();
        time = Timer.getFPGATimestamp();
        this.intakeSubsystem.runRotation(0.1);

        if (currentDraw < currentLimit) {
            spikeStartTime = time;
        }

        // Detect when stator current is above threshold for enough time
        // When stator current is above threshold for enough time, set the target position to extended to not break the motor
        if (time - spikeStartTime > IntakeLimits.duration && this.position == IntakeStates.RETRACTED) {
            this.position = IntakeStates.EXTENDED;
            this.intakeSubsystem.setTargetPosition(this.position);
        }
    }

    @Override
    public void end(boolean interrupted) {
        this.intakeSubsystem.stopRotation();
    }

    @Override
    public boolean isFinished() {
        if (this.position == IntakeStates.EXTENDED) {
            return this.intakeSubsystem.isExtended();
        }
        else {
            return this.intakeSubsystem.isRetracted();
        }
    }
}