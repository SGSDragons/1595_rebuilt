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
import frc.robot.commands.Shooter.PassShot;
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
import frc.robot.subsystems.Intake.Rotation.IntakeSubsystemReal;
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
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
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
	private final SwerveSubsystemReal drive = new SwerveSubsystemReal(Units.MetersPerSecond.of(4.0), new Pose2d(13.0, 4.05, Rotation2d.kZero));

	private final IntakeSubsystem intake = new IntakeSubsystemReal();
	private final IntakeRollerSubsystem intakeRollers = new IntakeRollerSubsystemReal();

	private final HopperSubsystem hopper = new HopperSubsystemReal();
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
	Command enableShooter = new EnableShooter(shooter, goalAimer, false);
	Command extraShooter = new EnableShooter(shooter, goalAimer, true);
	Command defaultShooter = new DefaultShooter(shooter);
	
	Command closeShot = new CloseShot(shooter, hood, goalAimer);
	Command passShot = new PassShot(shooter, hood, feeder, hopper, intakeRollers);

	Command runFeeder = new ParallelCommandGroup(new RunFeeder(hopper, feeder, shooter, true), new RunIntakeRollers(intakeRollers, IntakeRollerSpeeds.SLOW), new EnableHood(hood, goalAimer));
	Command stopRollers = Commands.runOnce(() -> runFeeder.cancel());

	Command runIntakeRollers = new RunIntakeRollers(intakeRollers, IntakeRollerSpeeds.FAST);
	Command outtakeBalls = new ParallelCommandGroup(new RunIntakeRollers(intakeRollers, IntakeRollerSpeeds.REVERSE), new RunFeeder(hopper, feeder, shooter, false));

	Command zeroIntake = new ZeroIntake(intake);
	Command intakeOut = new IntakeToPosition(intake, IntakeStates.EXTENDED);
	Command intakeAgitate = new IntakeToPosition(intake, IntakeStates.AGITATE);
	Command intakeIn = new IntakeToPosition(intake, IntakeStates.RETRACTED);
	// Command setIntakeDown = Commands.runOnce(() -> this.intake.setDown());
	// Command setIntakeUp = Commands.runOnce(() -> this.intake.setDown());

	Command pointAtGoal	 = drive.aimAtGoal(() -> 0, () -> 0, goalAimer, 1.0);
	Command sleep3 = Commands.waitSeconds(3.0);

	Command reverseDirection = Commands.runOnce(() -> reverseDirection());
	Command resetOdometry = Commands.runOnce(() -> drive.resetOdometry(Pose2d.kZero));

	// Command autoShoot = new ParallelRaceGroup(Commands.waitSeconds(5.0), new RunFeeder(hopper, feeder, shooter, true), new EnableHood(hood, goalAimer), new EnableShooter(shooter, goalAimer), drive.aimAtGoal(() -> 0, () -> 0, goalAimer, 1.0));
	Command autoShoot = new SequentialCommandGroup(
		new ParallelRaceGroup(Commands.waitSeconds(2.0), new RunFeeder(hopper, feeder, shooter, true), new EnableHood(hood, goalAimer), new EnableShooter(shooter, goalAimer, false), drive.aimAtGoal(() -> 0, () -> 0, goalAimer, 1.0)),
		new ParallelDeadlineGroup(Commands.waitSeconds(2.0), new RunFeeder(hopper, feeder, shooter, true), new EnableHood(hood, goalAimer), new EnableShooter(shooter, goalAimer, false), new IntakeToPosition(intake, IntakeStates.RETRACTED), drive.aimAtGoal(() -> 0, () -> 0, goalAimer, 1.0)),
		new IntakeToPosition(intake, IntakeStates.EXTENDED));

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
	//    Driver joystick Up (-1 y-axis) maps to positive X.
	//    Driver joystick left (-1 x-axis) maps to positive Y.
	// Thus, Blue axis are negated.

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

	public void reverseDirection() {
		Pose2d current = drive.getPose();
		drive.resetOdometry(new Pose2d(current.getTranslation(), drive.getHeading().plus(Rotation2d.k180deg)));
	}

	public void configureBindings() {
		// driverController.a().onTrue(reconfigAlliance);
		driverController.povDown().onTrue(reverseDirection);

		// drive.setDefaultCommand(drive.driveCommand(driver::translateX, driver::translateY, driver::lookX, driver::lookY, 1.0));
		drive.setDefaultCommand(drive.turnRelative(driver::translateX, driver::translateY, driver::lookX, 1.0));
		driverController.y().whileTrue(drive.driveCommand(driver::translateX, driver::translateY, () -> 0.0, () -> -driver.inverter, 1.0));
		driverController.a().whileTrue(drive.driveCommand(driver::translateX, driver::translateY, () -> 0.0, () -> driver.inverter, 1.0));
		driverController.rightTrigger(0.2).whileTrue(drive.driveCommand(driver::translateX, driver::translateY, driver::lookX, driver::lookY, 1.0)); 

		driverController.rightBumper().whileTrue(drive.aimAtGoal(driver::translateX, driver::translateY, goalAimer , 0.5));
		driverController.leftBumper().whileTrue(drive.lockSwerveDrive(driver::translateX, driver::translateY, driver::lookX, 0.25));

		operatorController.a().whileTrue(enableShooter);
		operatorController.y().whileTrue(extraShooter);
		operatorController.b().whileTrue(closeShot);

		operatorController.x().whileTrue(passShot);

		hood.setDefaultCommand(new ZeroHood(hood));
		shooter.setDefaultCommand(defaultShooter);

		operatorController.rightTrigger(0.2).whileTrue(runFeeder);

		operatorController.povUp().onTrue(intakeOut);
		operatorController.povDown().onTrue(intakeIn);
		// operatorController.povRight().onTrue(intakeAgitate);
		// operatorController.povDown().onTrue(setIntakeDown);
		// operatorController.povLeft().onTrue(setIntakeUp);
		// intake.setDefaultCommand(zeroIntake);

		operatorController.rightBumper().whileTrue(runIntakeRollers);
		operatorController.leftBumper().whileTrue(outtakeBalls);
	}

	public void configureTestBindings() {
		driverController.x().onTrue(Commands.runOnce(drive::updateAnglePIDF));

		// driverController.a().onTrue(reconfigAlliance);
		driverController.povDown().onTrue(reverseDirection);
		driverController.povUp().onTrue(resetOdometry);

		// drive.setDefaultCommand(drive.driveCommand(driver::translateX, driver::translateY, driver::lookX, driver::lookY, 1.0));
		drive.setDefaultCommand(drive.turnRelative(driver::translateX, driver::translateY, driver::lookX, 1.0));
		driverController.rightTrigger(0.2).whileTrue(drive.driveCommand(driver::translateX, driver::translateY, driver::lookX, driver::lookY, 1.0)); 

		driverController.y().whileTrue(drive.driveCommand(driver::translateX, driver::translateY, () -> 0.0, () -> -driver.inverter, 1.0));
		driverController.a().whileTrue(drive.driveCommand(driver::translateX, driver::translateY, () -> 0.0, () -> driver.inverter, 1.0));

		driverController.rightBumper().whileTrue(drive.aimAtGoal(driver::translateX, driver::translateY, goalAimer , 0.5));
		driverController.leftBumper().whileTrue(drive.lockSwerveDrive(driver::translateX, driver::translateY, driver::lookX, 0.25));

		operatorController.a().whileTrue(enableShooter);
		operatorController.y().whileTrue(extraShooter);
		operatorController.b().whileTrue(closeShot);

		operatorController.x().whileTrue(passShot);

		hood.setDefaultCommand(zeroHood);
		shooter.setDefaultCommand(defaultShooter);

		operatorController.rightTrigger(0.2).whileTrue(runFeeder);
		driverController.rightTrigger(0.2).whileTrue(runIntakeRollers);

		operatorController.povUp().onTrue(intakeOut);
		operatorController.povDown().onTrue(intakeIn);
		// operatorController.povRight().onTrue(intakeAgitate);
		// operatorController.povRight().onTrue(setIntakeDown);
		// operatorController.povLeft().onTrue(setIntakeUp);
		// intake.setDefaultCommand(zeroIntake);

		operatorController.rightBumper().whileTrue(runIntakeRollers);
		operatorController.leftBumper().whileTrue(outtakeBalls);
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
