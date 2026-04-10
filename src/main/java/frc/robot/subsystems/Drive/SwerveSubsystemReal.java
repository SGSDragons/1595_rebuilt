// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Drive;

import static edu.wpi.first.units.Units.MetersPerSecond;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

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
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Config;
import frc.robot.Constants.FieldConstants;
import frc.robot.Constants.HardwareID.LimeLightConstants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;
import frc.robot.subsystems.GoalAim;

import java.io.File;
import java.util.Arrays;
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

import frc.robot.Constants.TuningValues.DrivingValues;

public class SwerveSubsystemReal extends SwerveSubsystem {

    /**
     * Swerve drive object.
     */
    private final SwerveDrive swerveDrive;

    private final double maxSpeedMps;

    /**
     * Initialize {@link SwerveDrive} with the directory provided.
     *
     * @param maxSpeed The maximum operating speed to allow.
     */
    
    public SwerveSubsystemReal(LinearVelocity maxSpeed, Pose2d initialPose) {

        this.maxSpeedMps = maxSpeed.in(MetersPerSecond);


        // Configure the Telemetry before creating the SwerveDrive to avoid unnecessary objects being created.

        // Maybe slowing it down
        SwerveDriveTelemetry.verbosity = TelemetryVerbosity.HIGH;

        try {
            File configs = new File(Filesystem.getDeployDirectory(), "swerve");
            SwerveParser configParser = new SwerveParser(configs);
            swerveDrive = configParser.createSwerveDrive(maxSpeedMps, initialPose);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // might need to remove
        // swerveStatorLimits();

        // Heading correction should only be used while controlling the robot via angle.
        swerveDrive.setHeadingCorrection(false);

        swerveDrive.setCosineCompensator(false);

        //Correct for skew that gets worse as angular velocity increases. Start with a coefficient of 0.1.
        swerveDrive.setAngularVelocityCompensation(true, true, 0.1);

        // Enable if you want to resynchronize your absolute encoders and motor encoders periodically when they are not moving.
        swerveDrive.setModuleEncoderAutoSynchronize(true, 1);

        // Disable the odometry updates in a background thread and do them in
        // the main periodic loop. This way vision updates can be injected without
        // race conditions.
        swerveDrive.stopOdometryThread();
        
        configureAutoBuilder();
        publishTunables();
    }

    // keep everything else but limit stator hopefully
    public void swerveStatorLimits() {
        for (SwerveModule module : swerveDrive.getModules()) {
            TalonFX drive = (TalonFX) module.getDriveMotor().getMotor();

            TalonFXConfiguration config = new TalonFXConfiguration();
            drive.getConfigurator().refresh(config);

            config.CurrentLimits.StatorCurrentLimitEnable = true;
            config.CurrentLimits.StatorCurrentLimit = 60.0;

            drive.getConfigurator().apply(config);
        }
    }

    public double getSwerveSupplyCurrent() {
        double totalCurrent = 0;

        SwerveModule[] modules = swerveDrive.getModules();
        for (int i=0; i<4; i++) {
            TalonFX driveMotor = (TalonFX) modules[i].getDriveMotor().getMotor();
            TalonFX angleMotor = (TalonFX) modules[i].getAngleMotor().getMotor();
            totalCurrent += Math.abs(driveMotor.getSupplyCurrent().getValueAsDouble())+
                            Math.abs(angleMotor.getSupplyCurrent().getValueAsDouble());
        }

        return totalCurrent;
    }

    public double getSwerveStatorCurrent() {
        double totalCurrent = 0;

        SwerveModule[] modules = swerveDrive.getModules();
        for (int i=0; i<4; i++) {
            TalonFX driveMotor = (TalonFX) modules[i].getDriveMotor().getMotor();
            TalonFX angleMotor = (TalonFX) modules[i].getAngleMotor().getMotor();
            totalCurrent += Math.abs(driveMotor.getStatorCurrent().getValueAsDouble())+ 
                            Math.abs(angleMotor.getStatorCurrent().getValueAsDouble());
        }

        return totalCurrent;
    }

    public void telemetry() {
        if (FieldConstants.isRedAlliance()) {
            SmartDashboard.putNumber("Distance to Goal", getPose().getTranslation().getDistance(FieldConstants.redGoal));
        }
        else {
            SmartDashboard.putNumber("Distance to Goal", getPose().getTranslation().getDistance(FieldConstants.blueGoal));
        }

        SmartDashboard.putNumber("Swerve Stator Current", getSwerveStatorCurrent());
        SmartDashboard.putNumber("Swerve Supply Current", getSwerveSupplyCurrent());
    }

    @Override
    public void periodic() {
        swerveDrive.updateOdometry();

        // LimelightHelpers.SetRobotOrientation(LimeLightConstants.limelight2, getHeading().getDegrees(), 0, 0, 0, 0, 0);
        // PoseEstimate LL2poseEst = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(LimeLightConstants.limelight2);
        // boolean acceptLL2Reading = (LL2poseEst != null && LL2poseEst.tagCount > 0 && LL2poseEst.avgTagDist < LimeLightConstants.maxReadDistance);

        // if (acceptLL2Reading) {
        //     swerveDrive.swerveDrivePoseEstimator.addVisionMeasurement(
        //         LL2poseEst.pose,
        //         LL2poseEst.timestampSeconds,
        //         VecBuilder.fill(0.7, 0.7, 1e10)
        //         );
        // }

        LimelightHelpers.SetRobotOrientation(LimeLightConstants.limelight3a, getHeading().getDegrees(), 0, 0, 0, 0, 0);
        PoseEstimate LL3poseEst = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(LimeLightConstants.limelight3a);
        // boolean acceptLL3Reading = (LL3poseEst != null && LL3poseEst.tagCount > 0 && LL3poseEst.avgTagDist < LimeLightConstants.maxReadDistance);
        boolean acceptLL3Reading = (LL3poseEst != null && LL3poseEst.tagCount > 0);

        if (acceptLL3Reading) {
            swerveDrive.swerveDrivePoseEstimator.addVisionMeasurement(
                LL3poseEst.pose,
                LL3poseEst.timestampSeconds,
                VecBuilder.fill(0.7, 0.7, 1e10)
                );
        }

        telemetry();
    }

    public void configureAutoBuilder() {

        // Load the RobotConfig from the GUI settings. You should probably
        // store this in your Constants file
        RobotConfig config;
        try{
            config = RobotConfig.fromGUISettings();
        } catch (Exception e) {
            // Handle exception as needed
            e.printStackTrace();
            config = new RobotConfig(0, 0, null, 0);
        }

        // Configure AutoBuilder last
        AutoBuilder.configure(
                this::getPose, // Robot pose supplier
                this::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
                this::getRobotVelocity, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
                (speeds, feedforwards) -> drive(speeds), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds. Also optionally outputs individual module feedforwards
                new PPHolonomicDriveController( // PPHolonomicController is the built in path following controller for holonomic drive trains
                new PIDConstants(5.0, 0.0, 0.0), // Translation PID constants
                new PIDConstants(5.0, 0.0, 0.0) // Rotation PID constants
                ),
                config, // The robot configuration
                () -> FieldConstants.isRedAlliance(), // Flip alliance
                this // Reference to this subsystem to set requirements
        );
    }

    @Override
    public void simulationPeriodic() {
    }

    /**
     * Command to characterize the robot drive motors using SysId
     *
     * @return SysId Drive Command
     */
    public Command sysIdDriveMotorCommand() {
        SysIdRoutine routine = SwerveDriveTest.setDriveSysIdRoutine(new Config(), this, swerveDrive, 12, true);
        return SwerveDriveTest.generateSysIdCommand(routine, 3.0, 5.0, 3.0);
    }

    /**
     * Command to characterize the robot angle motors using SysId
     *
     * @return SysId Angle Command
     */
    public Command sysIdAngleMotorCommand() {
        SysIdRoutine routine = SwerveDriveTest.setAngleSysIdRoutine(new Config(), this, swerveDrive);
        return SwerveDriveTest.generateSysIdCommand(routine, 3.0, 5.0, 3.0);
    }

    /**
     * Returns a Command that centers the modules of the SwerveDrive subsystem.
     *
     * @return a Command that centers the modules of the SwerveDrive subsystem
     */
    public Command centerModulesCommand() {
        return run(() -> Arrays.asList(swerveDrive.getModules())
                .forEach(it -> it.setAngle(0.0)));
    }

    /**
     * Replaces the swerve module feedforward with a new SimpleMotorFeedforward object.
     *
     * @param kS the static gain of the feedforward
     * @param kV the velocity gain of the feedforward
     * @param kA the acceleration gain of the feedforward
     */
    public void replaceSwerveModuleFeedforward(double kS, double kV, double kA) {
        // TODO: We need to run the SysId routines to get these constants
        swerveDrive.replaceSwerveModuleFeedforward(new SimpleMotorFeedforward(kS, kV, kA));
    }

    @Override
    public Command driveRelative(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier angularRotationX) {
        return run(() -> {
            Translation2d joystick = new Translation2d(translationX.getAsDouble(), translationY.getAsDouble());
            if (joystick.getNorm() < 0.1) {
                joystick = Translation2d.kZero;
            }
            double omega = MathUtil.applyDeadband(angularRotationX.getAsDouble(), 0.2);
            omega = Math.pow(omega, 3) * swerveDrive.getMaximumChassisAngularVelocity() / 4.0;

            swerveDrive.drive(joystick, omega*2, false, false);
        });
    }

    @Override
    public Command turnRelative(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier angularRotationX, double scale) {
        return run(() -> {
            Translation2d joystick = computeTranslation(translationX.getAsDouble(), translationY.getAsDouble(), scale*this.maxSpeedMps);

            double omega = MathUtil.applyDeadband(angularRotationX.getAsDouble(), 0.2);
            omega = Math.pow(omega, 3) * swerveDrive.getMaximumChassisAngularVelocity();

            swerveDrive.drive(joystick, omega*-0.75, true, false);
        });
    }

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
    public Command driveCommand(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier headingX, DoubleSupplier headingY, double scale) {
        swerveDrive.setHeadingCorrection(true); // Normally you would want heading correction for this kind of control.
        return run(() -> {
            resetController();
            Translation2d joystick = computeTranslation(translationX.getAsDouble(), translationY.getAsDouble(), scale);

            driveFieldOriented(swerveDrive.swerveController.getTargetSpeeds(
                    joystick.getX(),
                    joystick.getY(),
                    headingX.getAsDouble(),
                    headingY.getAsDouble(),
                    swerveDrive.getOdometryHeading().getRadians(),
                    swerveDrive.getMaximumChassisVelocity()));
        });
    }

    @Override
    public Command aimAtGoal(DoubleSupplier translationX, DoubleSupplier translationY, GoalAim aimer, double scale) {
        swerveDrive.setHeadingCorrection(true); // Normally you would want heading correction for this kind of control.
        return run(() -> {

            Translation2d joystick = computeTranslation(translationX.getAsDouble(), translationY.getAsDouble(), scale*0.8);
            Translation2d vector = aimer.pointAtGoal();

            // Lock swerve if there's no translation and the robot is close to its heading
            if (Math.abs(vector.getAngle().getDegrees() - getHeading().getDegrees()) < 1.0 && joystick.getNorm() < 0.1) {
                lock();
            }
            else {
                driveFieldOriented(swerveDrive.swerveController.getTargetSpeeds(
                    joystick.getX(),
                    joystick.getY(),

                    // Vector inputs reversed
                    vector.getY(),
                    vector.getX(),

                    swerveDrive.getOdometryHeading().getRadians(),
                    swerveDrive.getMaximumChassisVelocity()));
            }
        });
    }

    @Override
    public Command lockSwerveDrive(DoubleSupplier translationX, DoubleSupplier translationY, DoubleSupplier angularRotationX, double scale) {
        swerveDrive.setHeadingCorrection(true);
        return run(() -> {

            Translation2d joystick = computeTranslation(translationX.getAsDouble(), translationY.getAsDouble(), scale*this.maxSpeedMps);

            double omega = MathUtil.applyDeadband(angularRotationX.getAsDouble(), 0.2);
            omega = Math.pow(omega, 3) * swerveDrive.getMaximumChassisAngularVelocity();

            if (Math.abs(angularRotationX.getAsDouble()) < 0.1 && joystick.getNorm() < 0.1) {
                lock();
            }
            else {
                swerveDrive.drive(joystick, omega*-0.75, true, false);
            }
        });
    }

    private Translation2d computeTranslation(double x, double y, double scale) {
        double magnitude = Math.hypot(x, y);
        if (magnitude < 0.1) {
            return Translation2d.kZero;
        } else {
            magnitude = scale * Math.pow(magnitude, DrivingValues.joystickPower);
            x = magnitude * x;
            y = magnitude * y;
            return new Translation2d(x, y);
        }
    }

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
    public void drive(Translation2d translation, double rotation, boolean fieldRelative) {
        swerveDrive.drive(translation,
                rotation,
                fieldRelative,
                false); // Open loop is disabled since it shouldn't be used most of the time.
    }

    /**
     * Drive the robot given a chassis field oriented velocity.
     *
     * @param velocity Velocity according to the field.
     */
    public void driveFieldOriented(ChassisSpeeds velocity) {
        swerveDrive.driveFieldOriented(velocity);
    }

    /**
     * Drive the robot given a chassis field oriented velocity.
     *
     * @param velocity Velocity according to the field.
     */
    public Command driveFieldOriented(Supplier<ChassisSpeeds> velocity) {
        return run(() -> swerveDrive.driveFieldOriented(velocity.get()));
    }

    /**
     * Drive according to the chassis robot oriented velocity.
     *
     * @param velocity Robot oriented {@link ChassisSpeeds}
     */
    public void drive(ChassisSpeeds velocity) {
        swerveDrive.drive(velocity);
    }


    /**
     * Get the swerve drive kinematics object.
     *
     * @return {@link SwerveDriveKinematics} of the swerve drive.
     */
    public SwerveDriveKinematics getKinematics() {
        return swerveDrive.kinematics;
    }

    /**
     * Resets odometry to the given pose. Gyro angle and module positions do not need to be reset when calling this
     * method.  However, if either gyro angle or module position is reset, this must be called in order for odometry to
     * keep working.
     *
     * @param initialHolonomicPose The pose to set the odometry to
     */
    public void resetOdometry(Pose2d initialHolonomicPose) {
        swerveDrive.resetOdometry(initialHolonomicPose);
        resetController();
    }

    public void resetController() {
         swerveDrive.swerveController.lastAngleScalar = swerveDrive.getPose().getRotation().getRadians();
    }

    /**
     * Gets the current pose (position and rotation) of the robot, as reported by odometry.
     *
     * @return The robot's pose
     */
    public Pose2d getPose() {
        return swerveDrive.getPose();
    }

    /**
     * Set chassis speeds with closed-loop velocity control.
     *
     * @param chassisSpeeds Chassis Speeds to set.
     */
    public void setChassisSpeeds(ChassisSpeeds chassisSpeeds) {
        swerveDrive.setChassisSpeeds(chassisSpeeds);
    }

    /**
     * Post the trajectory to the field.
     *
     * @param trajectory The trajectory to post.
     */
    public void postTrajectory(Trajectory trajectory) {
        swerveDrive.postTrajectory(trajectory);
    }

    /**
     * Resets the gyro angle to zero and resets odometry to the same position, but facing toward 0.
     */
    public void zeroGyro() {
        swerveDrive.zeroGyro();
    }

    /**
     * Sets the drive motors to brake/coast mode.
     *
     * @param brake True to set motors to brake mode, false for coast.
     */
    public void setMotorBrake(boolean brake) {
        swerveDrive.setMotorIdleMode(brake);
    }

    /**
     * Gets the current yaw angle of the robot, as reported by the swerve pose estimator in the underlying drivebase.
     * Note, this is not the raw gyro reading, this may be corrected from calls to resetOdometry().
     *
     * @return The yaw angle
     */
    public Rotation2d getHeading() {
        return getPose().getRotation();
    }

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
    public ChassisSpeeds getTargetSpeeds(double xInput, double yInput, double headingX, double headingY) {
        Translation2d scaledInputs = SwerveMath.cubeTranslation(new Translation2d(xInput, yInput));
        return swerveDrive.swerveController.getTargetSpeeds(scaledInputs.getX(),
                scaledInputs.getY(),
                headingX,
                headingY,
                getHeading().getRadians(),
                maxSpeedMps);
    }

    /**
     * Gets the current field-relative velocity (x, y and omega) of the robot
     *
     * @return A ChassisSpeeds object of the current field-relative velocity
     */
    public ChassisSpeeds getFieldVelocity() {
        return swerveDrive.getFieldVelocity();
    }

    /**
     * Gets the current velocity (x, y and omega) of the robot
     *
     * @return A {@link ChassisSpeeds} object of the current velocity
     */
    public ChassisSpeeds getRobotVelocity() {
        return swerveDrive.getRobotVelocity();
    }

    /**
     * Get the {@link SwerveController} in the swerve drive.
     *
     * @return {@link SwerveController} from the {@link SwerveDrive}.
     */
    public SwerveController getSwerveController() {
        return swerveDrive.swerveController;
    }

    /**
     * Get the {@link SwerveDriveConfiguration} object.
     *
     * @return The {@link SwerveDriveConfiguration} fpr the current drive.
     */
    public SwerveDriveConfiguration getSwerveDriveConfiguration() {
        return swerveDrive.swerveDriveConfiguration;
    }

    /**
     * Lock the swerve drive to prevent it from moving.
     */
    public void lock() {
        swerveDrive.lockPose();
    }

    /**
     * Gets the current pitch angle of the robot, as reported by the imu.
     *
     * @return The heading as a {@link Rotation2d} angle
     */
    public Rotation2d getPitch() {
        return swerveDrive.getPitch();
    }

    /**
     * Gets the swerve drive object.
     *
     * @return {@link SwerveDrive}
     */
    public SwerveDrive getSwerveDrive() {
        return swerveDrive;
    }

    public void updateAnglePIDF() {
        var table = getAnglePIDFTable();

        // Read values, providing current defaults if the table entry doesn't exist
        PIDFConfig init = swerveDrive.getModules()[0].getAnglePIDF();
        double p = table.getEntry("kP").getDouble(init.p);
        double i = table.getEntry("kI").getDouble(init.i);
        double d = table.getEntry("kD").getDouble(init.d);
        double f = table.getEntry("kF").getDouble(init.f);

        PIDFConfig newConfig = new PIDFConfig(p, i, d, f);

        // Apply to all modules (YAGSL SwerveDrive object)
        for (SwerveModule module : swerveDrive.getModules()) {
            module.setAnglePIDF(newConfig);
        }

        System.out.println("Applied new Angle PIDF: P=" + p);
    }

    private NetworkTable getAnglePIDFTable() {

        // A table for tuning the Angle PIDF
        return NetworkTableInstance.getDefault().getTable("Tuning").getSubTable("swerveAnglePIDF");
    }

    private void publishTunables() {
        var table = getAnglePIDFTable();
        PIDFConfig init = swerveDrive.getModules()[0].getAnglePIDF();
        table.getEntry("kP").setDouble(init.p);
        table.getEntry("kI").setDouble(init.i);
        table.getEntry("kD").setDouble(init.d);
        table.getEntry("kF").setDouble(init.f);
    }
}