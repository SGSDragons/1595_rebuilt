package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystemReal;


public class IntakeToPosition extends Command {

    public enum IntakePosition {
        EXTENDED,
        RETRACTED
    }

    private final IntakeSubsystem intakeSubsystem;
    private final IntakePosition position;
    public boolean autoRun;

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
    }

    @Override
    public void execute() {
        this.intakeSubsystem.gotoPosition();
        System.out.println("Works");
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