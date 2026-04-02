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
import frc.robot.commands.Shooter.CloseShot;
import frc.robot.commands.Shooter.EnableHood;
import frc.robot.commands.Shooter.EnableShooter;
import frc.robot.commands.Shooter.DefaultShooter;
import frc.robot.commands.Shooter.ZeroHood;
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
import frc.robot.subsystems.Intake.Roller.IntakeRollerSubsystem.IntakeRollerSpeeds;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem;
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystem.IntakeStates;
import frc.robot.subsystems.Shooter.Hood.HoodSubsystem;
import frc.robot.subsystems.Shooter.Hood.HoodSubsystemReal;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystem;
import frc.robot.subsystems.Shooter.Shooter.ShooterSubsystemReal;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;


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
	private final SwerveSubsystemReal drive = new SwerveSubsystemReal(Units.MetersPerSecond.of(2.0), new Pose2d(13.0, 4.05, Rotation2d.kZero));

	private final IntakeSubsystem intake = new IntakeSubsystem();
	private final IntakeRollerSubsystem intakeRollers = new IntakeRollerSubsystemReal();

	private final HopperSubsystem hopper = new HopperSubsystem();
	private final FeederSubsystem feeder = new FeederSubsystemReal();

	// private final ShooterSubsystem shooter = new ShooterSubsystem();
	private final ShooterSubsystem shooter = new ShooterSubsystemReal(() -> feeder.getStatorCurrent() > 1.0);
	private final HoodSubsystem hood = new HoodSubsystemReal();

	private final ClimberSubsystem climber = new ClimberSubsystem();

	private final CommandXboxController driverController = new CommandXboxController(OperatorConstants.driverControllerPort);
	private final CommandXboxController operatorController = new CommandXboxController(OperatorConstants.operatorControllerPort);

	DriverSticks driver = new DriverSticks();
	GoalAim goalAimer = new GoalAim(drive);

	Command zeroHood = new ZeroHood(hood);
	Command enableShooter = new ParallelCommandGroup(new EnableHood(hood, goalAimer), new EnableShooter(shooter, goalAimer));
	Command closeShot = new CloseShot(shooter, hood, goalAimer);
	Command defaultShooter = new DefaultShooter(shooter);

	Command runFeeder = new ParallelCommandGroup(new RunFeeder(hopper, feeder, shooter, true), new RunIntakeRollers(intakeRollers, IntakeRollerSpeeds.SLOW));
	Command stopRollers = Commands.runOnce(() -> runFeeder.cancel());

	Command runIntakeRollers = new RunIntakeRollers(intakeRollers, IntakeRollerSpeeds.FAST);
	Command outtakeBalls = new ParallelCommandGroup(new RunIntakeRollers(intakeRollers, IntakeRollerSpeeds.REVERSE), new RunFeeder(hopper, feeder, shooter, false));

	Command increaseFeederLimit = Commands.runOnce(() -> feeder.increaseCurrentLimits());
	Command decreaseFeederLimit = Commands.runOnce(() -> feeder.decreaseCurrentLimits());

	Command zeroIntake = new ZeroIntake(intake);
	Command intakeOut = new IntakeToPosition(intake, IntakeStates.EXTENDED);
	Command intakeIn = new IntakeToPosition(intake, IntakeStates.RETRACTED);

	Command pointAtGoal	 = drive.aimAtGoal(() -> 0, () -> 0, goalAimer, 1.0);
	Command sleep3 = Commands.waitSeconds(3.0);

	Command reconfigAlliance = Commands.runOnce(() -> reconfigAlliance());
	Command reverseDirection = Commands.runOnce(() -> reverseDirection());
	Command resetOdometry = Commands.runOnce(() -> drive.resetOdometry(new Pose2d(Translation2d.kZero, Rotation2d.kZero)));

	Command autoShoot = new ParallelCommandGroup(Commands.waitSeconds(10.0), new RunFeeder(hopper, feeder, shooter, false), new EnableHood(hood, goalAimer), new EnableShooter(shooter, goalAimer), drive.aimAtGoal(() -> 0, () -> 0, goalAimer, 1.0));
	Command resetForNext = new ParallelCommandGroup(Commands.runOnce(() -> runFeeder.cancel()), new ZeroHood(hood), new DefaultShooter(shooter));

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
		private final double inverter = FieldConstants.isRedAlliance() ? 1.0 : -1.0;
		double readAxis(XboxController.Axis axis) {
			return driverController.getRawAxis(axis.value);
		}
		public double translateX() { return inverter*readAxis(Axis.kLeftY); }
		public double translateY() { return inverter*readAxis(Axis.kLeftX); }
		public double lookX() { return inverter*readAxis(Axis.kRightX); }
		public double lookY() { return inverter*readAxis(Axis.kRightY); }
	}

	private void registerCommands() {
		NamedCommands.registerCommand("enableShooter", enableShooter);
		NamedCommands.registerCommand("closeShot", closeShot);
		NamedCommands.registerCommand("zeroHood", zeroHood);
		NamedCommands.registerCommand("defaultShooter", defaultShooter);

		NamedCommands.registerCommand("runFeeder", runFeeder);
		NamedCommands.registerCommand("stopRollers", stopRollers);
		NamedCommands.registerCommand("runIntakeRollers", runIntakeRollers);

		NamedCommands.registerCommand("intakeIn", intakeIn);
		NamedCommands.registerCommand("intakeOut", intakeOut);
		NamedCommands.registerCommand("zeroIntake", zeroIntake);

		NamedCommands.registerCommand("pointAtGoal", pointAtGoal);
		NamedCommands.registerCommand("sleep3", sleep3);

		NamedCommands.registerCommand("autoShoot", autoShoot);
		NamedCommands.registerCommand("resetForNext", resetForNext);
	}

	public void reconfigAlliance() {
		drive.configureAutoBuilder();
		driver = new DriverSticks();
		enableShooter = new ParallelCommandGroup(new EnableHood(hood, goalAimer), new EnableShooter(shooter, goalAimer));
		pointAtGoal = drive.aimAtGoal(() -> 0, () -> 0, goalAimer, 1.0);
	}

	public void reverseDirection() {
		Pose2d current = drive.getPose();
		drive.resetOdometry(new Pose2d(current.getTranslation(), drive.getHeading().plus(Rotation2d.k180deg)));
	}

	public void configureBindings() {
		driverController.a().onTrue(reconfigAlliance);
		driverController.povDown().onTrue(reverseDirection);

		drive.setDefaultCommand(drive.driveCommand(driver::translateX, driver::translateY, driver::lookX, driver::lookY, 1.0));
		driverController.rightBumper().whileTrue(drive.aimAtGoal(driver::translateX, driver::translateY, goalAimer , 0.5));
		driverController.leftBumper().whileTrue(drive.lockSwerveDrive(driver::translateX, driver::translateY, driver::lookX, driver::lookY, 0.25));
		
		// Drive Tangent
		// driverController.rightTrigger(0.2).whileTrue(drive.driveCommand(driver::translateX, driver::translateY, driver::translateY, driver::translateX, 1.0));

		operatorController.a().whileTrue(enableShooter);
		operatorController.b().whileTrue(closeShot);

		hood.setDefaultCommand(new ZeroHood(hood));
		shooter.setDefaultCommand(defaultShooter);

		operatorController.rightTrigger(0.2).whileTrue(runFeeder);

		operatorController.povUp().onTrue(intakeOut);
		operatorController.povDown().onTrue(intakeIn);
		// intake.setDefaultCommand(zeroIntake);

		operatorController.rightBumper().whileTrue(runIntakeRollers);
		operatorController.leftBumper().whileTrue(outtakeBalls);

		operatorController.x().onTrue(decreaseFeederLimit);
		operatorController.y().onTrue(increaseFeederLimit);
	}

	public void configureTestBindings() {
		driverController.x().onTrue(Commands.runOnce(drive::updateAnglePIDF));

		driverController.a().onTrue(reconfigAlliance);
		driverController.povDown().onTrue(reverseDirection);
		driverController.povUp().onTrue(resetOdometry);

		drive.setDefaultCommand(drive.driveCommand(driver::translateX, driver::translateY, driver::lookX, driver::lookY, 1.0));
		driverController.rightBumper().whileTrue(drive.aimAtGoal(driver::translateX, driver::translateY, goalAimer , 0.5));
		driverController.leftBumper().whileTrue(drive.lockSwerveDrive(driver::translateX, driver::translateY, driver::lookX, driver::lookY, 0.5));
		
		// Drive Tangent
		// driverController.rightTrigger(0.2).whileTrue(drive.driveCommand(driver::translateX, driver::translateY, driver::translateY, driver::translateX, 1.0));

		operatorController.a().whileTrue(enableShooter);
		operatorController.b().whileTrue(closeShot);

		hood.setDefaultCommand(zeroHood);
		shooter.setDefaultCommand(defaultShooter);

		operatorController.rightTrigger(0.2).whileTrue(runFeeder);

		operatorController.povUp().onTrue(intakeOut);
		operatorController.povDown().onTrue(intakeIn);
		// intake.setDefaultCommand(zeroIntake);

		operatorController.rightBumper().whileTrue(runIntakeRollers);
		operatorController.leftBumper().whileTrue(outtakeBalls);

		operatorController.x().onTrue(decreaseFeederLimit);
		operatorController.y().onTrue(increaseFeederLimit);
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
