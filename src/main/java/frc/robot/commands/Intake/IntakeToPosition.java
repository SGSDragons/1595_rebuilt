package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.CurrentLimits.IntakeLimits;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystemReal;


public class IntakeToPosition extends Command {

    public enum IntakePosition {
        EXTENDED,
        RETRACTED
    }

    private final IntakeSubsystem intakeSubsystem;
    private IntakePosition position;
    
    final double currentLimit = IntakeLimits.currentLimit;
    double currentDraw;
    double time;
    double spikeStartTime;
    boolean hasZeroed;

    public IntakeToPosition(IntakeSubsystem intake, IntakePosition position) {
        this.intakeSubsystem = intake;
        this.position = position;
        addRequirements((Subsystem) intake);
    }

    // Set target speed
    @Override
    public void initialize() {  
        if (this.position == IntakePosition.EXTENDED) {
            this.intakeSubsystem.setTargetPosition(IntakeValues.extended);
        }
        else {
            this.intakeSubsystem.setTargetPosition(IntakeValues.retracted);
        }
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
        if (time - spikeStartTime > IntakeLimits.duration && this.position == IntakePosition.RETRACTED) {
            this.position = IntakePosition.EXTENDED;
            this.intakeSubsystem.setTargetPosition(IntakeValues.extended);
        }
    }

    @Override
    public void end(boolean interrupted) {
        this.intakeSubsystem.stopRotation();
    }

    @Override
    public boolean isFinished() {
        if (this.position == IntakePosition.EXTENDED) {
            return this.intakeSubsystem.isExtended();
        }
        else {
            return this.intakeSubsystem.isExtended();
        }
    }
}