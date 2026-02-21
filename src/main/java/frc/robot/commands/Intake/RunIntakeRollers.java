package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.subsystems.Intake.*;


public class RunIntakeRollers extends Command {

    private final IntakeRollerSubsystemReal intakeRollerSubsystem;


    public RunIntakeRollers(IntakeRollerSubsystemReal intakeRollerSubsystem) {
        this.intakeRollerSubsystem = intakeRollerSubsystem;
        addRequirements(intakeRollerSubsystem);
    }

    @Override
    public void initialize() {  
    }

    @Override
    public void execute() {
        this.intakeRollerSubsystem.runRollers(IntakeValues.intakeRunSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        this.intakeRollerSubsystem.runRollers(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}