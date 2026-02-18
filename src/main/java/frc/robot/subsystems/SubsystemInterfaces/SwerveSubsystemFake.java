// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.SubsystemInterfaces;

import static edu.wpi.first.units.Units.MetersPerSecond;

// import com.pathplanner.lib.auto.AutoBuilder;
// import com.pathplanner.lib.commands.PathfindingCommand;
// import com.pathplanner.lib.config.PIDConstants;
// import com.pathplanner.lib.config.RobotConfig;
// import com.pathplanner.lib.controllers.PPHolonomicDriveController;
// import com.pathplanner.lib.controllers.PathFollowingController;
// import com.pathplanner.lib.util.DriveFeedforwards;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Config;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import frc.robot.subsystems.GoalAim;

import java.io.File;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import swervelib.SwerveController;
import swervelib.SwerveDrive;
import swervelib.SwerveDriveTest;
import swervelib.SwerveModule;
import swervelib.math.SwerveMath;
import swervelib.parser.PIDFConfig;
import swervelib.parser.SwerveDriveConfiguration;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

public class SwerveSubsystemFake extends DriveSubsystemFake {

    /**
     * Swerve drive object.
     */
    private final SwerveDrive swerveDrive;

    private final double maxSpeedMps;

    private Pose2d initPose;

    /**
     * Initialize {@link SwerveDrive} with the directory provided.
     *
     * @param maxSpeed The maximum operating speed to allow.
     */
    public SwerveSubsystemFake(LinearVelocity maxSpeed, Pose2d initialPose) {
        this.maxSpeedMps = maxSpeed.in(MetersPerSecond);

        // Configure the Telemetry before creating the SwerveDrive to avoid unnecessary objects being created.
        SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;
        try {
            File configs = new File(Filesystem.getDeployDirectory(), "swerve");
            SwerveParser configParser = new SwerveParser(configs);
            swerveDrive = configParser.createSwerveDrive(maxSpeedMps, initialPose);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void periodic() {}

    @Override
    public void simulationPeriodic() {}

    /**
     * Setup AutoBuilder for PathPlanner.
     */
    public void setupPathPlanner() {}

    /**
     * Command to characterize the robot drive motors using SysId
     *
     * @return SysId Drive Command
     */
    public Command sysIdDriveMotorCommand() { return runOnce(() -> {}); }

    /**
     * Command to characterize the robot angle motors using SysId
     *
     * @return SysId Angle Command
     */
    public Command sysIdAngleMotorCommand() { return runOnce(() -> {}); }

    /**
     * Returns a Command that centers the modules of the SwerveDrive subsystem.
     *
     * @return a Command that centers the modules of the SwerveDrive subsystem
     */
    public Command centerModulesCommand() { return runOnce(() -> {}); }

    /**
     * Replaces the swerve module feedforward with a new SimpleMotorFeedforward object.
     *
     * @param kS the static gain of the feedforward
     * @param kV the velocity gain of the feedforward
     * @param kA the acceleration gain of the feedforward
     */
    public void replaceSwerveModuleFeedforward(double kS, double kV, double kA) {}

    /**
     * Command to drive the robot using translative values and heading as a setpoint.
     * Using this method, the robot will move in the direction of one joystick's tilt, and
     * point in the direction of the other joystick's tilt.
     *
     * @param translationX Translation in the X direction. Cubed for smoother controls.
     * @param translationY Translation in the Y direction. Cubed for smoother controls.
     * @param headingX     Heading X to calculate angle of the joystick.
     * @param headingY     Heading Y to calculate angle of the joystick.
     * @return Drive command.
     */
    @Override
    public Command driveCommand(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier headingX, DoubleSupplier headingY, double scale) { return runOnce(() -> {}); }

    @Override
    public Command pointAtGoal(DoubleSupplier translationX, DoubleSupplier translationY, GoalAim aimer, double scale) { return runOnce(() -> {}); }

    @Override
    public Command driveRelative(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier angularRotationX) { return runOnce(() -> {}); }

    /**
     * The primary method for controlling the drivebase.  Takes a {@link Translation2d} and a rotation rate, and
     * calculates and commands module states accordingly.  Can use either open-loop or closed-loop velocity control for
     * the wheel velocities.  Also has field- and robot-relative modes, which affect how the translation vector is used.
     *
     * @param translation   {@link Translation2d} that is the commanded linear velocity of the robot, in meters per
     *                      second. In robot-relative mode, positive x is torwards the bow (front) and positive y is
     *                      torwards port (left).  In field-relative mode, positive x is away from the alliance wall
     *                      (field North) and positive y is torwards the left wall when looking through the driver station
     *                      glass (field West).
     * @param rotation      Robot angular rate, in radians per second. CCW positive.  Unaffected by field/robot
     *                      relativity.
     * @param fieldRelative Drive mode.  True for field-relative, false for robot-relative.
     */
    public void drive(Translation2d translation, double rotation, boolean fieldRelative) {}

    /**
     * Drive the robot given a chassis field oriented velocity.
     *
     * @param velocity Velocity according to the field.
     */
    public void driveFieldOriented(ChassisSpeeds velocity) {}

    /**
     * Drive the robot given a chassis field oriented velocity.
     *
     * @param velocity Velocity according to the field.
     */
    public Command driveFieldOriented(Supplier<ChassisSpeeds> velocity) { return runOnce(() -> {}); }

    /**
     * Drive according to the chassis robot oriented velocity.
     *
     * @param velocity Robot oriented {@link ChassisSpeeds}
     */
    public void drive(ChassisSpeeds velocity) {}


    /**
     * Get the swerve drive kinematics object.
     *
     * @return {@link SwerveDriveKinematics} of the swerve drive.
     */
    public SwerveDriveKinematics getKinematics() { return new SwerveDriveKinematics(); }

    /**
     * Resets odometry to the given pose. Gyro angle and module positions do not need to be reset when calling this
     * method.  However, if either gyro angle or module position is reset, this must be called in order for odometry to
     * keep working.
     *
     * @param initialHolonomicPose The pose to set the odometry to
     */
    public void resetOdometry(Pose2d initialHolonomicPose) {}

    public void resetController() {}

    /**
     * Gets the current pose (position and rotation) of the robot, as reported by odometry.
     *
     * @return The robot's pose
     */
    public Pose2d getPose() { return Pose2d.kZero; }

    /**
     * Set chassis speeds with closed-loop velocity control.
     *
     * @param chassisSpeeds Chassis Speeds to set.
     */
    public void setChassisSpeeds(ChassisSpeeds chassisSpeeds) {}

    /**
     * Post the trajectory to the field.
     *
     * @param trajectory The trajectory to post.
     */
    public void postTrajectory(Trajectory trajectory) {}

    /**
     * Resets the gyro angle to zero and resets odometry to the same position, but facing toward 0.
     */
    public void zeroGyro() {}

    /**
     * Sets the drive motors to brake/coast mode.
     *
     * @param brake True to set motors to brake mode, false for coast.
     */
    public void setMotorBrake(boolean brake) {}

    /**
     * Gets the current yaw angle of the robot, as reported by the swerve pose estimator in the underlying drivebase.
     * Note, this is not the raw gyro reading, this may be corrected from calls to resetOdometry().
     *
     * @return The yaw angle
     */
    public Rotation2d getHeading() { return Rotation2d.kZero; }

    /**
     * Get the chassis speeds based on controller input of 2 joysticks. One for speeds in which direction. The other for
     * the angle of the robot.
     *
     * @param xInput   X joystick input for the robot to move in the X direction.
     * @param yInput   Y joystick input for the robot to move in the Y direction.
     * @param headingX X joystick which controls the angle of the robot.
     * @param headingY Y joystick which controls the angle of the robot.
     * @return {@link ChassisSpeeds} which can be sent to the Swerve Drive.
     */
    public ChassisSpeeds getTargetSpeeds(double xInput, double yInput, double headingX, double headingY) { return new ChassisSpeeds(); }

    /**
     * Gets the current field-relative velocity (x, y and omega) of the robot
     *
     * @return A ChassisSpeeds object of the current field-relative velocity
     */
    public ChassisSpeeds getFieldVelocity() { return new ChassisSpeeds(); }

    /**
     * Gets the current velocity (x, y and omega) of the robot
     *
     * @return A {@link ChassisSpeeds} object of the current velocity
     */
    public ChassisSpeeds getRobotVelocity() { return new ChassisSpeeds(); }

    /**
     * Get the {@link SwerveController} in the swerve drive.
     *
     * @return {@link SwerveController} from the {@link SwerveDrive}.
     */
    public SwerveController getSwerveController() { return swerveDrive.getSwerveController(); }

    /**
     * Get the {@link SwerveDriveConfiguration} object.
     *
     * @return The {@link SwerveDriveConfiguration} fpr the current drive.
     */
    public SwerveDriveConfiguration getSwerveDriveConfiguration() { return swerveDrive.swerveDriveConfiguration; }

    /**
     * Lock the swerve drive to prevent it from moving.
     */
    public void lock() {}

    /**
     * Gets the current pitch angle of the robot, as reported by the imu.
     *
     * @return The heading as a {@link Rotation2d} angle
     */
    public Rotation2d getPitch() { return new Rotation2d(); }

    /**
     * Gets the swerve drive object.
     *
     * @return {@link SwerveDrive}
     */
    public SwerveDrive getSwerveDrive() { return swerveDrive; }

    public void updateAnglePIDF() {}

    // private NetworkTable getAnglePIDFTable() {}

    private void publishTunables() {}
}