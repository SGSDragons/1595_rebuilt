package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem;


public class PullBalls extends Command {

    private final IntakeSubsystem intakeSubsystem;

    public PullBalls(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        addRequirements((Subsystem) intakeSubsystem);
    }

    @Override
    public void initialize() { 
    }

    @Override
    public void execute() {
        this.intakeSubsystem.runRotation(IntakeValues.agitateSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        this.intakeSubsystem.stopRotation();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}