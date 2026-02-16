// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.HoodSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.parser.PIDFConfig;
import edu.wpi.first.math.geometry.Pose2d;
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
<<<<<<< HEAD
    private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
    private final SwerveSubsystem swerve = new SwerveSubsystem(Units.MetersPerSecond.of(8), Pose2d.kZero);
    private final HoodSubsystem hood = new HoodSubsystem();

    private final CommandXboxController m_driverController =
        new CommandXboxController(OperatorConstants.kDriverControllerPort);
        
    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        configureBindings();
    }

    /**
     * Use this method to define your trigger->command mappings. Triggers can be created via the
     * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
     * predicate, or via the named factories in {@link
     * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
     * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
     * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
     * joysticks}.
     */
    private void configureBindings() {
        new Trigger(m_exampleSubsystem::exampleCondition)
            .onTrue(new ExampleCommand(m_exampleSubsystem));


        m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
    }

  

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        // An example command will be run in autonomous
        return Autos.exampleAuto(m_exampleSubsystem);
    }
=======
  // The robot's subsystems and commands are defined here...
  private final SwerveSubsystem drive = new SwerveSubsystem(Units.MetersPerSecond.of(2.0), Pose2d.kZero);
  private final HoodSubsystem hood = new HoodSubsystem();

  private final CommandXboxController driverController = new CommandXboxController(OperatorConstants.driverControllerPort);
  private final CommandXboxController operatorController = new CommandXboxController(OperatorConstants.operatorControllerPort);


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Configure the trigger bindings
    configureBindings();

    if (drive instanceof SwerveSubsystem) {
      SwerveSubsystem swerve = (SwerveSubsystem) drive;
      swerve.resetOdometry(Pose2d.kZero);
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
    private final double inverter = 1.0;
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
    
    drive.setDefaultCommand(drive.driveCommand(driver::translateX, driver::translateY, driver::lookX, driver::lookY, 1.0));
  }

  public void configureTestBindings() {

    driverController.x().onTrue(Commands.runOnce(drive::updateAnglePIDF));

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

>>>>>>> 403196ddef33a4a623b3f37db74b549ad3349d9b
}
