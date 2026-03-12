package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.subsystems.Intake.Roller.IntakeRollerSubsystem;


public class RunIntakeRollers extends Command {

    private final IntakeRollerSubsystem intakeRollerSubsystem;
    private boolean runForward;

    public RunIntakeRollers(IntakeRollerSubsystem intakeRollerSubsystem, boolean runForward) {
        this.intakeRollerSubsystem = intakeRollerSubsystem;
        this.runForward = runForward;
        addRequirements((Subsystem) intakeRollerSubsystem);
    }

    @Override
    public void initialize() { 
    }

    @Override
    public void execute() {
        if (runForward) {
            this.intakeRollerSubsystem.runRollers(IntakeValues.intakeRunSpeed);
        }
        else {
            this.intakeRollerSubsystem.runRollers(-IntakeValues.intakeRunSpeed);
        }
    }

    @Override
    public void end(boolean interrupted) {
        this.intakeRollerSubsystem.stopRollers();;
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}