package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Constants.Aiming;
import frc.robot.Constants.FieldConstants;
import frc.robot.subsystems.Drive.SwerveSubsystem;

public class GoalAim {
    
    SwerveSubsystem swerve;
    Translation2d goalPosition;

    public GoalAim(SwerveSubsystem swerve) {
        this.swerve = swerve;
        updateAlliance();
    }

    public void updateAlliance() {
        if (FieldConstants.isRedAlliance()) {
            goalPosition = FieldConstants.redGoal;
        }
        else {
            goalPosition = FieldConstants.blueGoal;
        }
    }

    public Translation2d pointAtGoal() {
        // Translation2d robotPosition = this.swerve.getPose().getTranslation();
        Translation2d robotPosition = velocityAdjustedPosition();

        // Find the X and Y offsets between the robot and goal, normalize, and turn to Translation2d
        Translation2d heading = goalPosition.minus(robotPosition);
        heading = heading.div(heading.getNorm());
        heading = new Translation2d(heading.getX(), heading.getY());
        
        return heading;
    }

    public double getCurrentDistance() {
        Translation2d robotPosition = this.swerve.getPose().getTranslation();
        return robotPosition.getDistance(goalPosition);
    }

    public double getAdjustedDistance() {
        Translation2d robotPosition = velocityAdjustedPosition();
        return robotPosition.getDistance(goalPosition);
    }

    // Get average of time of flights and use that to calculate the robot position
    public Translation2d velocityAdjustedPosition() {
        double currentTOF = Aiming.getFlightValue(getCurrentDistance());
        Translation2d futurePosition = getTranslation(currentTOF, this.swerve.getRobotVelocity(), this.swerve.getPose().getTranslation());
        
        double futureTOF = Aiming.getFlightValue(futurePosition.getDistance(goalPosition));
        double averageTOF = (currentTOF + futureTOF) / 2;
        return getTranslation(averageTOF, this.swerve.getFieldVelocity(), this.swerve.getPose().getTranslation());
    }

    public Translation2d getTranslation(double time, ChassisSpeeds velocity, Translation2d position) {
        return position.plus(new Translation2d(time*velocity.vxMetersPerSecond, time*velocity.vyMetersPerSecond));
    }
}
