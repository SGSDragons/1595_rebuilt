package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.subsystems.Intake.Roller.IntakeRollerSubsystem;


public class RunIntakeRollers extends Command {

    private final IntakeRollerSubsystem intakeRollerSubsystem;

    public RunIntakeRollers(IntakeRollerSubsystem intakeRollerSubsystem) {
        this.intakeRollerSubsystem = intakeRollerSubsystem;
        addRequirements((Subsystem) intakeRollerSubsystem);
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