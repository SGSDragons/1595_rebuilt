package frc.robot.commands.Feeder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.TuningValues.FeederValues;
import frc.robot.Constants.TuningValues.HopperValues;
import frc.robot.subsystems.Drive.SwerveSubsystemReal;
import frc.robot.subsystems.Feeder.Feeder.FeederSubsystem;
import frc.robot.subsystems.Feeder.Hopper.HopperSubsystem;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystem;


public class RunFeeder extends Command {

    private final HopperSubsystem hopperSubsystem;
    private final FeederSubsystem feederSubsystem;
    private final ShooterSubsystem shooter;
    private boolean runForward;

    public RunFeeder(HopperSubsystem hopperSubsystem, FeederSubsystem feederSubsystem, ShooterSubsystem shooter, boolean runForward) {
        this.hopperSubsystem = hopperSubsystem;
        this.feederSubsystem = feederSubsystem;
        this.shooter = shooter;
        this.runForward = runForward;

        addRequirements((Subsystem) hopperSubsystem, (Subsystem) feederSubsystem);
    }

    @Override   
    public void initialize() {
        this.hopperSubsystem.runRollers(-HopperValues.hopperRunSpeed);
        this.feederSubsystem.runRollers(-FeederValues.feederRunSpeed);
        // System.out.println("Feeder running");  
    }

    // Run feeder no matter what so it doesn't get jammed but stop hopper if flywheel isn't up to speed
    @Override
    public void execute() {
        if (runForward) {
            this.feederSubsystem.runRollers(FeederValues.feederRunSpeed);
            if (this.shooter.nearTargetSpeed()) {
                this.hopperSubsystem.runRollers(HopperValues.hopperRunSpeed);
            } else {
                this.hopperSubsystem.runRollers(0.0);
            }
        } else {
            // this.feederSubsystem. increaseCurrentLimits();
            this.hopperSubsystem.runRollers(-HopperValues.hopperRunSpeed);
            this.feederSubsystem.runRollers(-FeederValues.feederRunSpeed);
        }
    }

    @Override
    public void end(boolean interrupted) {
        this.hopperSubsystem.runRollers(0);
        this.feederSubsystem.runRollers(0);
        // this.feederSubsystem.decreaseCurrentLimits();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}