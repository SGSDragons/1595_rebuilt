package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem.IntakeStates;


public class IntakeToPosition extends Command {

    private final IntakeSubsystem intakeSubsystem;
    private IntakeStates position;

    public IntakeToPosition(IntakeSubsystem intake, IntakeStates position) {
        this.intakeSubsystem = intake;
        this.position = position;

        addRequirements((Subsystem) intake);
    }

    // Set target position
    @Override
    public void initialize() {
        this.intakeSubsystem.setTargetPosition(this.position);
        // System.out.println("Intake"+this.position);
    }

    @Override
    public void execute() {
        this.intakeSubsystem.gotoPosition();  
    }

    @Override
    public void end(boolean interrupted) {
        this.intakeSubsystem.stopRotation();
    }

    @Override
    public boolean isFinished() {
        if (this.position == IntakeStates.EXTENDED && this.intakeSubsystem.isExtended()) {
            return true;
        }
        // if (this.position == IntakeStates.AGITATE && this.intakeSubsystem.isAgitated()) {
        //     return true;
        // }
        if (this.position == IntakeStates.RETRACTED && this.intakeSubsystem.isRetracted()) {
            return true;
        }
        else {
            return false;
        }
    }
}