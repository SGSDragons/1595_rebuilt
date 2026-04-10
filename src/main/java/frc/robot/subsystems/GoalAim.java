package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Constants.Aiming;
import frc.robot.Constants.FieldConstants;
import frc.robot.subsystems.Drive.SwerveSubsystem;
import frc.robot.subsystems.Drive.SwerveSubsystemReal;

public class GoalAim {
    
    SwerveSubsystem swerve;
    private Translation2d velocityAdjustedPosition;

    public GoalAim(SwerveSubsystem swerve) {
        this.swerve = swerve;
        this.velocityAdjustedPosition = swerve.getPose().getTranslation();
    }

    // Update the velocity adjusted position estimate only once per cycle,
    // and reuse the value for all other computations.
    public void periodic() {
        final ChassisSpeeds robotSpeeds;
        final ChassisSpeeds fieldSpeeds;
        if (swerve instanceof SwerveSubsystemReal) {
            robotSpeeds = this.swerve.getRobotVelocity();
            fieldSpeeds = ChassisSpeeds.fromRobotRelativeSpeeds(robotSpeeds, ((SwerveSubsystemReal)swerve).getSwerveDrive().getOdometryHeading());
        } else {
            robotSpeeds = new ChassisSpeeds();
            fieldSpeeds = new ChassisSpeeds();
        }

        Translation2d goalPose = goalPosition();
        Translation2d currentPosition = this.swerve.getPose().getTranslation();
        double currentDistance = currentPosition.getDistance(goalPose);
        double currentTOF = Aiming.getFlightValue(currentDistance);

        Translation2d futurePosition = getTranslation(currentTOF, robotSpeeds, currentPosition);
        double futureDistance = futurePosition.getDistance(goalPose);

        double averageDistance = (futureDistance + currentDistance) / 2.0;
        double timeOfFlight = Aiming.getFlightValue(averageDistance);
        this.velocityAdjustedPosition = getTranslation(timeOfFlight, fieldSpeeds, currentPosition);
    }

    private Translation2d goalPosition() {
        return FieldConstants.isRedAlliance() ? FieldConstants.redGoal : FieldConstants.blueGoal;
    }

    public Translation2d pointAtGoal() {
        // Find the X and Y offsets between the robot and goal, normalize, and turn to Translation2d
        Translation2d heading = goalPosition().minus(velocityAdjustedPosition);
        heading = heading.div(heading.getNorm());

        return heading;
    }

    public double getAdjustedDistance() {
        return velocityAdjustedPosition.getDistance(goalPosition());
    }

    private Translation2d getTranslation(double time, ChassisSpeeds velocity, Translation2d position) {
        double x = position.getX() + time * velocity.vxMetersPerSecond;
        double y = position.getY() + time * velocity.vyMetersPerSecond;
        return new Translation2d(x, y);
    }
}
