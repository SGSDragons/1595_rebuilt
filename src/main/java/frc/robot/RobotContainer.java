// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.FeildConstants;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Intake.IntakeToPosition;
import frc.robot.commands.Intake.IntakeToPosition.IntakePosition;
import frc.robot.commands.Shooter.EnableHood;
import frc.robot.commands.Shooter.ZeroHood;
import frc.robot.subsystems.GoalAim;
import frc.robot.subsystems.Drive.SwerveSubsystem;
import frc.robot.subsystems.Drive.SwerveSubsystemReal;
import frc.robot.subsystems.Intake.IntakeSubsystem;
import frc.robot.subsystems.Intake.IntakeSubsystemReal;
import frc.robot.subsystems.Shooter.HoodSubsystem;
import frc.robot.subsystems.Shooter.HoodSubsystemReal;
import swervelib.parser.PIDFConfig;

import java.util.function.DoubleSupplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
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
  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystemReal drive = new SwerveSubsystemReal(Units.MetersPerSecond.of(6.0), new Pose2d(4, 4, Rotation2d.kZero));
  private final HoodSubsystemReal hood = new HoodSubsystemReal();
  private final IntakeSubsystem intake = new IntakeSubsystem();

  private final CommandXboxController driverController = new CommandXboxController(OperatorConstants.driverControllerPort);
  private final CommandXboxController operatorController = new CommandXboxController(OperatorConstants.operatorControllerPort);

  private final boolean isRedAlliance = FeildConstants.isRedAlliance();

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

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
    // TODO: Make this adapt to the Red/Blue alliance
    private final double inverter = isRedAlliance ? 1.0 : -1.0;
    double readAxis(XboxController.Axis axis) {
      return driverController.getRawAxis(axis.value);
    }
    public double translateX() { return inverter*readAxis(Axis.kLeftY); }
    public double translateY() { return inverter*readAxis(Axis.kLeftX); }
    public double lookX() { return inverter*readAxis(Axis.kRightX); }
    public double lookY() { return inverter*readAxis(Axis.kRightY); }
  }

  private void configureBindings() {

    DriverSticks driver = new DriverSticks();
    GoalAim goalAimer = new GoalAim(drive, isRedAlliance);

    drive.setDefaultCommand(drive.driveCommand(driver::translateX, driver::translateY, driver::lookX, driver::lookY, 1.0));
    driverController.leftBumper().whileTrue(drive.pointAtGoal(driver::translateX, driver::translateY, goalAimer , 1.0));

    operatorController.a().onTrue(new EnableHood(hood, goalAimer));
    hood.setDefaultCommand(new EnableHood(hood, goalAimer));
  }

  public void configureTestBindings() {

    DriverSticks driver = new DriverSticks();
    GoalAim goalAimer = new GoalAim(drive, isRedAlliance);

    driverController.x().onTrue(Commands.runOnce(drive::updateAnglePIDF));

    operatorController.a().onTrue(new EnableHood(hood, goalAimer));
    hood.setDefaultCommand(new EnableHood(hood, goalAimer));

    operatorController.povRight().onTrue(new IntakeToPosition(intake, IntakePosition.RETRACTED));
    operatorController.povLeft().onTrue(new IntakeToPosition(intake, IntakePosition.EXTENDED));
  }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return null;
  }

}
