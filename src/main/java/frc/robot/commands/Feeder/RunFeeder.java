package frc.robot.commands.Feeder;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.TuningValues.FeederValues;
import frc.robot.Constants.TuningValues.HopperValues;
import frc.robot.subsystems.Feeder.*;


public class RunFeeder extends Command {

    private final HopperSubsystemReal hopperSubsystem;
    private final FeederSubsystemReal feederSubsystem;

    public RunFeeder(HopperSubsystemReal hopperSubsystem, FeederSubsystemReal feederSubsystem) {
        this.hopperSubsystem = hopperSubsystem;
        this.feederSubsystem = feederSubsystem;

        addRequirements(hopperSubsystem, feederSubsystem);
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