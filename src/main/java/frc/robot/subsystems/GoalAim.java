package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import frc.robot.Constants.FeildConstants;
import frc.robot.subsystems.Drive.SwerveSubsystem;

public class GoalAim {
    
    SwerveSubsystem swerveSubsystem;
    Translation2d goalPosition;
    boolean isRed;

    public GoalAim(SwerveSubsystem swerveSubsystem, boolean isRed) {
        this.swerveSubsystem = swerveSubsystem;

        if (isRed) {
            goalPosition = FeildConstants.redGoal;
        }
        else {
            goalPosition = FeildConstants.blueGoal;
        }
    }

    // return robot angle to point at goal
    public Translation2d pointAtGoal() {
        Translation2d robotPosition = this.swerveSubsystem.getPose().getTranslation();

        // Find the X and Y offsets bettween the robot and goal, normalize, and turn to Translation2d
        Translation2d heading = goalPosition.minus(robotPosition);
        heading = heading.div(heading.getNorm());
        heading = new Translation2d(heading.getY(), heading.getX());

        return heading;
    }

    // return distance between robot and goal for the hood 
    public double getDistance() {
        Translation2d robotPosition = this.swerveSubsystem.getPose().getTranslation();
        return robotPosition.getDistance(goalPosition);
    }
}
