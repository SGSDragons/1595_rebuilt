package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.subsystems.Intake.Roller.IntakeRollerSubsystem;
import frc.robot.subsystems.Intake.Roller.IntakeRollerSubsystem.IntakeRollerSpeeds;


public class RunIntakeRollers extends Command {

    private final IntakeRollerSubsystem intakeRollerSubsystem;
    private IntakeRollerSpeeds speed;

    public RunIntakeRollers(IntakeRollerSubsystem intakeRollerSubsystem, IntakeRollerSpeeds speed) {
        this.intakeRollerSubsystem = intakeRollerSubsystem;
        this.speed = speed;
        addRequirements((Subsystem) intakeRollerSubsystem);
    }

    @Override
    public void initialize() { 
        // System.out.println("Intake Rollers Running");  
    }

    @Override
    public void execute() {
        if (speed == IntakeRollerSpeeds.FAST) {
            this.intakeRollerSubsystem.runRollers(IntakeValues.intakeFastVoltage);
        } else if (speed == IntakeRollerSpeeds.SLOW) {
            this.intakeRollerSubsystem.runRollers(IntakeValues.intakeSlowVoltage);
        }
        else {
            this.intakeRollerSubsystem.runRollers(-IntakeValues.intakeFastVoltage);
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