package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.subsystems.Intake.IntakeSubsystem;


public class GotoPosition extends Command {

    public enum IntakePosition {
        EXTENDED,
        RETRACTED
    }

    private final IntakeSubsystem intakeSubsystem;
    private final IntakePosition position;
    public boolean autoRun;

    public GotoPosition(IntakeSubsystem intakeSubsystem, IntakePosition position) {
        this.intakeSubsystem = intakeSubsystem;
        this.position = position;
        addRequirements(intakeSubsystem);
    }

    // Set target speed
    @Override
    public void initialize() {  
        if (position == IntakePosition.EXTENDED) {
            this.intakeSubsystem.setTargetPosition(IntakeValues.extended);
        }
        else {
            this.intakeSubsystem.setTargetPosition(IntakeValues.retracted);
        }
    }

    @Override
    public void execute() {
        this.intakeSubsystem.gotoPosition();
    }

    @Override
    public void end(boolean interrupted) {
        intakeSubsystem.stopRotation();
    }

    @Override
    public boolean isFinished() {
        if (position == IntakePosition.EXTENDED) {
            return this.intakeSubsystem.isExtended();
        }
        else {
            return this.intakeSubsystem.isExtended();
        }
    }
}