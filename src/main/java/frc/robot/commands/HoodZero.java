package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ShooterSubsystem;

public class HoodZero extends Command {

    private final ShooterSubsystem shooterSubsystem;

    final double currentLimit = 40;
    double currentDraw;

    public HoodZero(ShooterSubsystem shooterSubsystem) {
        this.shooterSubsystem = shooterSubsystem;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        currentDraw = this.shooterSubsystem.getHoodCurrent();
        this.shooterSubsystem.runHood(-0.1);
    }

    @Override
    public void end(boolean interrupted) {
        this.shooterSubsystem.runHood(0);
        this.shooterSubsystem.zeroHood();
    }

    @Override
    public boolean isFinished() {
        return currentDraw > currentLimit;
    }
}