package frc.robot.commands.Feeder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.TuningValues.FeederValues;
import frc.robot.Constants.TuningValues.HopperValues;
import frc.robot.subsystems.Feeder.Feeder.FeederSubsystem;
import frc.robot.subsystems.Feeder.Hopper.HopperSubsystem;


public class RunFeeder extends Command {

    private final HopperSubsystem hopperSubsystem;
    private final FeederSubsystem feederSubsystem;
    private boolean runForward;

    public RunFeeder(HopperSubsystem hopperSubsystem, FeederSubsystem feederSubsystem, boolean runForward) {
        this.hopperSubsystem = hopperSubsystem;
        this.feederSubsystem = feederSubsystem;
        this.runForward = runForward;

        addRequirements((Subsystem) hopperSubsystem, (Subsystem) feederSubsystem);
    }

    @Override   
    public void initialize() {  
    }

    @Override
    public void execute() {
        if (runForward) {
            this.hopperSubsystem.runRollers(HopperValues.hopperRunSpeed);
            this.feederSubsystem.runRollers(FeederValues.feederRunSpeed);
        } else {
            this.hopperSubsystem.runRollers(-HopperValues.hopperRunSpeed);
            this.feederSubsystem.runRollers(-FeederValues.feederRunSpeed);
        }
    
    }

    @Override
    public void end(boolean interrupted) {
        this.hopperSubsystem.runRollers(0);
        this.feederSubsystem.runRollers(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}