package frc.robot.commands.Shooter;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.subsystems.Shooter.Hood.HoodSubsystem;

public class ZeroHood extends Command {

    private final HoodSubsystem hoodSubsystem;

    public ZeroHood(HoodSubsystem hoodSubsystem) {
        this.hoodSubsystem = hoodSubsystem;
        addRequirements((Subsystem) hoodSubsystem);
    }

    @Override
    public void initialize() {  
    }
    @Override
    public void execute() {
        // Slowly run hood down and record stator current
        // When stator current is above threshold for enough time, stop motor and zero the hood
        this.hoodSubsystem.doZeroing();
    }

    @Override
    public void end(boolean interrupted) {
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }
}