package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;


public class WaitCommand extends Command {

    private double time;
    private double start;

    public WaitCommand(double time) {
        this.time = time;
    }

    @Override
    public void initialize() { 
        start = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {
    }

    @Override
    public void end(boolean interrupted) {
    }

    @Override
    public boolean isFinished() {
        System.out.println(Timer.getFPGATimestamp() - start);
        return (Timer.getFPGATimestamp() - start > this.time);
    }
}