// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import frc.robot.Constants.FieldConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Feeder.RunFeeder;
import frc.robot.commands.Intake.IntakeToPosition;
import frc.robot.commands.Intake.RunIntakeRollers;
import frc.robot.commands.Intake.ZeroIntake;
import frc.robot.commands.Shooter.Hood.EnableHood;
import frc.robot.commands.Shooter.Hood.ZeroHood;
import frc.robot.commands.Shooter.Shooter.EnableShooter;
import frc.robot.commands.Shooter.Shooter.RunShooter;
import frc.robot.subsystems.GoalAim;
import frc.robot.subsystems.Climber.ClimberSubsystem;
import frc.robot.subsystems.Drive.SwerveSubsystem;
import frc.robot.subsystems.Drive.SwerveSubsystemReal;
import frc.robot.subsystems.Feeder.Feeder.FeederSubsystem;
import frc.robot.subsystems.Feeder.Feeder.FeederSubsystemReal;
import frc.robot.subsystems.Feeder.Hopper.HopperSubsystem;
import frc.robot.subsystems.Feeder.Hopper.HopperSubsystemReal;
import frc.robot.subsystems.Intake.Roller.IntakeRollerSubsystem;
import frc.robot.subsystems.Intake.Roller.IntakeRollerSubsystemReal;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem.IntakeStates;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystemReal;
import frc.robot.subsystems.Shooter.Hood.HoodSubsystem;
import frc.robot.subsystems.Shooter.Hood.HoodSubsystemReal;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystem;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystemReal;
import swervelib.parser.PIDFConfig;

import java.util.List;
import java.util.function.DoubleSupplier;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import com.pathplanner.lib.path.PathPlannerPath;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SwerveControllerCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
	SendableChooser<Command> autoChooser;

	// The robot's subsystems and commands are defined here...
	// private final SwerveSubsystem drive = new SwerveSubsystem();
	private final SwerveSubsystemReal drive = new SwerveSubsystemReal(Units.MetersPerSecond.of(1.5), new Pose2d(13.0, 4.05, Rotation2d.kZero));

	private final IntakeSubsystem intake = new IntakeSubsystem();
	private final IntakeRollerSubsystem intakeRollers = new IntakeRollerSubsystem();

	private final HopperSubsystem hopper = new HopperSubsystemReal();
	private final FeederSubsystem feeder = new FeederSubsystemReal();

	private final ShooterSubsystem shooter = new ShooterSubsystemReal(new GoalAim(drive));
	private final HoodSubsystem hood = new HoodSubsystemReal();

	private final ClimberSubsystem climber = new ClimberSubsystem();

	private final CommandXboxController driverController = new CommandXboxController(OperatorConstants.driverControllerPort);
	private final CommandXboxController operatorController = new CommandXboxController(OperatorConstants.operatorControllerPort);

	boolean isRedAlliance = FieldConstants.isRedAlliance();

	DriverSticks driver = new DriverSticks();
	GoalAim goalAimer = new GoalAim(drive);

	Command startShooter = new ParallelCommandGroup(new EnableHood(hood, goalAimer), new EnableShooter(shooter, goalAimer));
	Command shootWithHood = new ParallelCommandGroup(new RunFeeder(hopper, feeder), new EnableHood(hood, goalAimer));
	Command shootWithoutHood = new RunFeeder(hopper, feeder);

	Command runIntakeRollers = new RunIntakeRollers(intakeRollers, true);
	Command outtakeIntakeRollers = new RunIntakeRollers(intakeRollers, false);

	Command intakeOut = new IntakeToPosition(intake, IntakeStates.EXTENDED);
	Command intakeIn = new IntakeToPosition(intake, IntakeStates.RETRACTED);
	Command intakeBounce = new IntakeToPosition(intake, IntakeStates.BOUNCE);

	// Command shootAtGoal = new ParallelCommandGroup(drive.aimAtGoal(() -> 0, () -> 0, goalAimer, 1.0), shootWithHood);

  	public RobotContainer() {
		configureBindings();
		registerCommands();

		// Auto Chooser 
		autoChooser = AutoBuilder.buildAutoChooser();
		SmartDashboard.putData("Auto Chooser", autoChooser);

		if (drive instanceof SwerveSubsystem) {
			SwerveSubsystem swerve = (SwerveSubsystem) drive;
		}
  	}

	//      ______________________________(17, 8)
	//      |                                |
	//     B|                                |R
	//     B|                                |R
	//     B|                                |R
	//     B|                                |R
	//      |________________________________|
	//    (0,0)
	//
	// With 0deg heading pointing right.
	//
	// When on the Blue alliance
	//    Left joystick Up (-1 y-axis) maps to positive X.
	//    Left joystick left (-1 x-axis) maps to positive Y.
	// Thus, Blue axis are inverted.

	class DriverSticks {
		private final double inverter = isRedAlliance ? 1.0 : 1.0;
		double readAxis(XboxController.Axis axis) {
			return driverController.getRawAxis(axis.value);
		}
		public double translateX() { return inverter*readAxis(Axis.kLeftY); }
		public double translateY() { return inverter*readAxis(Axis.kLeftX); }
		public double lookX() { return inverter*readAxis(Axis.kRightX); }
		public double lookY() { return inverter*readAxis(Axis.kRightY); }
	}

	private void registerCommands() {
		NamedCommands.registerCommand("startShooter", startShooter);
		NamedCommands.registerCommand("shootWithHood", shootWithHood);
		NamedCommands.registerCommand("shootWithoutHood", shootWithoutHood);

		NamedCommands.registerCommand("intakeIn", intakeIn);
		NamedCommands.registerCommand("intakeOut", intakeOut);
		NamedCommands.registerCommand("runIntakeRollers", runIntakeRollers);

		// NamedCommands.registerCommand("shootAtGoal", shootAtGoal);
	}

	public void reconfigAlliance() {
		isRedAlliance = FieldConstants.isRedAlliance();
		
		goalAimer.updateAlliance();
		driver = new DriverSticks();
		shootWithHood = new ParallelCommandGroup(new RunFeeder(hopper, feeder), new EnableHood(hood, goalAimer), new EnableShooter(shooter, goalAimer));
		// shootAtGoal = new ParallelCommandGroup(drive.aimAtGoal(() -> 0, () -> 0, goalAimer, 1.0), shootWithHood);
	}


	public void configureBindings() {
		// if (isRedAlliance) {
		// 	Pose2d current = drive.getPose();
		// 	drive.resetOdometry(new Pose2d(current.getTranslation(), current.getRotation().plus(Rotation2d.k180deg)));
		// }
		
		driver = new DriverSticks();

		// drive.setDefaultCommand(drive.driveRelative(driver::translateX, driver::translateY, driver::lookX));
		drive.setDefaultCommand(drive.driveCommand(driver::translateX, driver::translateY, driver::lookX));
		driverController.povDown().onTrue(drive.runOnce(drive::zeroGyro));
		driverController.leftBumper().whileTrue(drive.aimAtGoal(driver::translateX, driver::translateY, goalAimer , 1.0));

		operatorController.b().whileTrue(shootWithoutHood);
		operatorController.a().whileTrue(shootWithHood);

		// TODO: Also enable shooting based on position?
		operatorController.x().onTrue(shooter.runOnce(shooter::enableShooting));
		operatorController.y().onTrue(shooter.runOnce(shooter::disableShooting));

		hood.setDefaultCommand(new ZeroHood(hood));

		// shooter.setDefaultCommand(new RunShooter(shooter));

		operatorController.povUp().onTrue(intakeOut);
		operatorController.povDown().onTrue(intakeIn);
		operatorController.povLeft().onTrue(intakeBounce);
		// intake.setDefaultCommand(new ZeroIntake(intake));

		operatorController.rightBumper().whileTrue(runIntakeRollers);
		operatorController.leftBumper().whileTrue(outtakeIntakeRollers);
	}

	public void configureTestBindings() {

		drive.zeroGyro();
		if (FieldConstants.isRedAlliance()) {
			drive.resetOdometry(new Pose2d(13.0, 4.05, Rotation2d.k180deg));
		} else {
			drive.resetOdometry(new Pose2d(3.0, 4.05, Rotation2d.kZero));
		}
		
		driverController.x().onTrue(Commands.runOnce(drive::updateAnglePIDF));

		// drive.setDefaultCommand(drive.driveRelative(driver::translateX, driver::translateY, driver::lookX));
		drive.setDefaultCommand(drive.driveCommand(driver::translateX, driver::translateY, driver::lookX, driver::lookY, 1.0));
		driverController.leftBumper().whileTrue(drive.aimAtGoal(driver::translateX, driver::translateY, goalAimer , 1.0));

		operatorController.b().whileTrue(shootWithoutHood);
		operatorController.a().whileTrue(shootWithHood);
		hood.setDefaultCommand(new ZeroHood(hood));

		shooter.setDefaultCommand(new RunShooter(shooter));

		operatorController.povUp().onTrue(intakeOut);
		operatorController.povDown().onTrue(intakeIn);
		// intake.setDefaultCommand(new ZeroIntake(intake));

		operatorController.rightBumper().whileTrue(runIntakeRollers);
		operatorController.leftBumper().whileTrue(outtakeIntakeRollers);
	}


	/**
	 * Use this to pass the autonomous command to the main {@link Robot} class.
	 *
	 * @return the command to run in autonomous
	 */
	public Command getAutonomousCommand() {
		Command auto = autoChooser.getSelected();
		return auto;
	}

}
