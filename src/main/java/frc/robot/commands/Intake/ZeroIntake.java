package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem;

public class ZeroIntake extends Command {

    private final IntakeSubsystem intakeSubsystem;

    public ZeroIntake(IntakeSubsystem intakeSubsystem) {
        this.intakeSubsystem = intakeSubsystem;
        addRequirements((Subsystem) intakeSubsystem);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        // prevent zeroing when extended
        if (this.intakeSubsystem.isRetracted()) {
            this.intakeSubsystem.doZeroing();
        }
    }
    @Override
    public void end(boolean interrupted) {
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }
}