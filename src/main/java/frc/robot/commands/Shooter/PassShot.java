package frc.robot.commands.Shooter;

import org.ironmaple.simulation.Goal;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.GoalAim;
import frc.robot.subsystems.Feeder.Feeder.FeederSubsystem;
import frc.robot.subsystems.Feeder.Hopper.HopperSubsystem;
import frc.robot.subsystems.Intake.Roller.IntakeRollerSubsystem;
import frc.robot.subsystems.Shooter.Hood.HoodSubsystem;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystem;
import frc.robot.Constants.Aiming;
import frc.robot.Constants.TuningValues.FeederValues;
import frc.robot.Constants.TuningValues.HopperValues;
import frc.robot.Constants.TuningValues.IntakeValues;


public class PassShot extends Command {

    private final ShooterSubsystem shooterSubsystem;
    private final HoodSubsystem hoodSubsystem;
    private final IntakeRollerSubsystem intakeRollers;
    private final FeederSubsystem feeder;
    private final HopperSubsystem hopper;
    double targetSpeed;
    double targetPosition;

    public PassShot(ShooterSubsystem shooterSubsystem, HoodSubsystem hoodSubsystem, FeederSubsystem feeder, HopperSubsystem hopper, IntakeRollerSubsystem intakeRollers) {
        this.shooterSubsystem = shooterSubsystem;
        this.hoodSubsystem = hoodSubsystem;
        this.feeder = feeder;
        this.hopper = hopper;
        this.intakeRollers = intakeRollers;

        addRequirements((Subsystem) shooterSubsystem);
        addRequirements((Subsystem) hoodSubsystem);
    }
    
    @Override
    public void initialize() {
        targetSpeed = Aiming.getWheelValue(Aiming.passingShooter);
        this.shooterSubsystem.setTargetVelocity(targetSpeed);

        targetPosition = Aiming.getHoodValue(Aiming.passingHood);
        this.hoodSubsystem.setTargetPosition(targetPosition);
    }

    @Override
    public void execute() {
        this.shooterSubsystem.setTargetVelocity(targetSpeed);
        this.shooterSubsystem.runShooter();

        this.hoodSubsystem.setTargetPosition(targetPosition);
        if (this.hoodSubsystem.atTargetPosition()) {
            this.hoodSubsystem.runHood(0);
        }
        else {
            this.hoodSubsystem.enableHood();
        }
        
        hopper.runRollers(HopperValues.hopperVoltage, HopperValues.spinnerVoltage);
        feeder.runRollers(FeederValues.feederVoltage);
        intakeRollers.runRollers(-IntakeValues.intakeFastVoltage);
    }

    @Override
    public void end(boolean interrupted) {
        hopper.stopRollers();
        feeder.stopRollers();
        intakeRollers.stopRollers();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}