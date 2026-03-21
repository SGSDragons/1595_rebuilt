package frc.robot.commands.Feeder;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.Constants.TuningValues.FeederValues;
import frc.robot.Constants.TuningValues.HopperValues;
import frc.robot.Constants.TuningValues.ShooterValues;
import frc.robot.subsystems.Drive.SwerveSubsystemReal;
import frc.robot.subsystems.Feeder.Feeder.FeederSubsystem;
import frc.robot.subsystems.Feeder.Hopper.HopperSubsystem;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystem;


public class RunFeeder extends Command {

    private final HopperSubsystem hopperSubsystem;
    private final FeederSubsystem feederSubsystem;
    private final ShooterSubsystem shooter;
    private boolean runForward;
    private boolean atSpeed;

    public RunFeeder(HopperSubsystem hopperSubsystem, FeederSubsystem feederSubsystem, ShooterSubsystem shooter, boolean runForward) {
        this.hopperSubsystem = hopperSubsystem;
        this.feederSubsystem = feederSubsystem;
        this.shooter = shooter;
        this.runForward = runForward;
        atSpeed = false;

        addRequirements((Subsystem) hopperSubsystem, (Subsystem) feederSubsystem);
    }

    @Override   
    public void initialize() {
        this.feederSubsystem.runRollers(-FeederValues.feederVoltage);
        this.hopperSubsystem.runRollers(-HopperValues.hopperVoltage, -HopperValues.spinnerVoltage);
        atSpeed = false;
        // System.out.println("Feeder running");  
    }

    // If shooter is up to speed run hopper and feeder no matter what 
    @Override
    public void execute() {
        if (this.shooter.nearTargetSpeed()) {
            shooter.FFkick(ShooterValues.kickVoltage, ShooterValues.kickLength);
            atSpeed = true;
        }
        if (runForward) {
            if (atSpeed) {
                this.feederSubsystem.runRollers(FeederValues.feederVoltage);
                this.hopperSubsystem.runRollers(HopperValues.hopperVoltage, HopperValues.spinnerVoltage);
            } else {
                this.feederSubsystem.stopRollers();
                this.hopperSubsystem.stopRollers();
            }
        } else {
            this.feederSubsystem.runRollers(-FeederValues.feederVoltage);
            this.hopperSubsystem.runRollers(-HopperValues.hopperVoltage, -HopperValues.spinnerVoltage);
        }
    }

    @Override
    public void end(boolean interrupted) {
        this.hopperSubsystem.stopRollers();
        this.feederSubsystem.stopRollers();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}