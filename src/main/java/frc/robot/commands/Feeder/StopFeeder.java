package frc.robot.commands.Feeder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Feeder.Feeder.FeederSubsystem;
import frc.robot.subsystems.Feeder.Hopper.HopperSubsystem;


public class StopFeeder extends Command {

    private final HopperSubsystem hopperSubsystem;
    private final FeederSubsystem feederSubsystem;

    public StopFeeder(HopperSubsystem hopperSubsystem, FeederSubsystem feederSubsystem) {
        this.hopperSubsystem = hopperSubsystem;
        this.feederSubsystem = feederSubsystem;

        addRequirements((Subsystem) hopperSubsystem, (Subsystem) feederSubsystem);
    }

    @Override   
    public void initialize() {
        // System.out.println("Feeder stopped");  
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {  
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}