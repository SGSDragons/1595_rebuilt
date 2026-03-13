package frc.robot.subsystems.Drive;

import java.io.File;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.units.measure.LinearVelocity;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Subsystem;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import frc.robot.subsystems.GoalAim;
import swervelib.math.SwerveMath;
import swervelib.parser.SwerveParser;
import swervelib.telemetry.SwerveDriveTelemetry;
import swervelib.telemetry.SwerveDriveTelemetry.TelemetryVerbosity;

public class SwerveSubsystem extends SubsystemBase {

    public SwerveSubsystem() {}

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

    public Command driveCommand(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier headingX, DoubleSupplier headingY, double scale) { return runOnce(null); }

    /**
     * Command to drive the robot relative to the robot itself.
     * 
     * @param translationX A supplier where positive values drive forward
     * @param translationY A supplier where positive values strafe left
     * @param angularRotationX A supplier where positive values turn right
     * @return A drive command
     */
    public Command driveRelative(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier angularRotationX) { return runOnce(null); }

    /**
     * Command to drive the robot using translation values and heading pointed to the goal
     *
     * @param translationX Translation in the X direction. Cubed for smoother controls.
     * @param translationY Translation in the Y direction. Cubed for smoother controls.
     * @param aimer Used to find goal heaading offset.
     * @return A drive command.
     */
    public Command aimAtGoal(DoubleSupplier translationX, DoubleSupplier translationY, GoalAim aimer, double scale) { return runOnce(null); }

    public Pose2d getPose() { return Pose2d.kZero; }

    public ChassisSpeeds getRobotVelocity() { return new ChassisSpeeds(); }

    public ChassisSpeeds getFieldVelocity() { return new ChassisSpeeds(); };

    public void updateAnglePIDF() {}

}