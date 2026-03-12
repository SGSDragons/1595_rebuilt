package frc.robot.commands.Intake;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.CurrentLimits.IntakeLimits;
import frc.robot.Constants.TuningValues.IntakeValues;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem.IntakeStates;


public class IntakeToPosition extends Command {

    private final IntakeSubsystem intakeSubsystem;
    private IntakeStates position;
    
    final double currentLimit = IntakeLimits.currentLimit;
    private double startTime;
    private double time;

    public IntakeToPosition(IntakeSubsystem intake, IntakeStates position) {
        this.intakeSubsystem = intake;
        this.position = position;

        System.out.println(this.position);

        addRequirements((Subsystem) intake);
    }

    // Set target position
    @Override
    public void initialize() {  
        this.intakeSubsystem.setTargetPosition(this.position);
        startTime = Timer.getFPGATimestamp();
        time = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
        this.intakeSubsystem.gotoPosition();

        // Detect when intake takes to long to move
        // set the target position to extended to not break the motor
        time = Timer.getFPGATimestamp();
        if (time - startTime > IntakeLimits.maxTravelTime && this.intakeSubsystem.getPosition() > 2.0) {
            // this.position = IntakeStates.EXTENDED;
            this.intakeSubsystem.setTargetPosition(IntakeStates.EXTENDED);
            startTime = Timer.getFPGATimestamp();
        }
    }

    @Override
    public void end(boolean interrupted) {
        this.intakeSubsystem.stopRotation();
        System.out.println("End: "+this.intakeSubsystem.getState()+" "+this.position);
    }

    @Override
    public boolean isFinished() {
        if (this.intakeSubsystem.getState() == this.position) {
            return true;
        }
        if (this.position == IntakeStates.EXTENDED && this.intakeSubsystem.isExtended()) {
            return true;
        }
        if (this.position == IntakeStates.RETRACTED && this.intakeSubsystem.isRetracted()) {
            return true;
        }
        else {
            return false;
        }
    }
}