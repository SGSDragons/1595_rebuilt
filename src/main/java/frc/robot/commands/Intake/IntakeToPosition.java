package frc.robot.commands.Intake;

import edu.wpi.first.util.sendable.Sendable;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    private boolean safety = false;

    public IntakeToPosition(IntakeSubsystem intake, IntakeStates position) {
        this.intakeSubsystem = intake;
        this.position = position;
        // System.out.println(this.position);

        addRequirements((Subsystem) intake);
    }

    // Set target position
    @Override
    public void initialize() {  
        this.intakeSubsystem.setTargetPosition(this.position);
        startTime = Timer.getFPGATimestamp();
        time = Timer.getFPGATimestamp();

        // SmartDashboard.putNumber("target position", this.position == IntakeStates.EXTENDED ? 1 : 0);
    }

    @Override
    public void execute() {
        this.intakeSubsystem.gotoPosition();

        // Detect when intake takes too long to move
        // set the target position to extended to not break the motor
        // time = Timer.getFPGATimestamp();
        // if (time - startTime > IntakeLimits.maxTravelTime && this.intakeSubsystem.getPosition() > 2.0) {
        //     this.position = IntakeStates.EXTENDED;
        //     this.intakeSubsystem.setTargetPosition(IntakeStates.EXTENDED);
        //     startTime = Timer.getFPGATimestamp();
        //     safety = true; 
        // }
    }

    @Override
    public void end(boolean interrupted) {
        this.intakeSubsystem.stopRotation();
        if (safety) {
            this.position = IntakeStates.RETRACTED;
        }
        safety = false;
        // SmartDashboard.putNumber("end position", this.position == IntakeStates.EXTENDED ? 1 : 0);
    }

    @Override
    public boolean isFinished() {
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