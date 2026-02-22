package frc.robot.commands.Feeder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.TuningValues.FeederValues;
import frc.robot.Constants.TuningValues.HopperValues;
import frc.robot.subsystems.Feeder.*;


public class RunFeeder extends Command {

    private final HopperSubsystem hopperSubsystem;
    private final FeederSubsystem feederSubsystem;

    public RunFeeder(HopperSubsystem hopperSubsystem, FeederSubsystem feederSubsystem) {
        this.hopperSubsystem = hopperSubsystem;
        this.feederSubsystem = feederSubsystem;

        addRequirements((Subsystem) hopperSubsystem, (Subsystem) feederSubsystem);
    }

    @Override   
    public void initialize() {  
    }

    @Override
    public void execute() {
        this.hopperSubsystem.runRollers(HopperValues.hopperRunSpeed);
        this.feederSubsystem.runRollers(FeederValues.feederRunSpeed);
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