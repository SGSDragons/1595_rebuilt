// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  
  public static final class OperatorConstants {

      public static final int driverControllerPort = 0;
      public static final int operatorControllerPort = 1;

      public static double stickDeadband() {
          return Preferences.getDouble("Deadband", 0.1);
      }
  }

	public static final class HardwareID {

		// All the CANBus and Channel IDs used on the robot. If any of these IDs are repeated,
		// then things won't work correctly
		//
		// Can ID -1 denotes numbers that have yet to be programmed on devices.
		// 
		// IDs 3-14 are used by the swerve modules. Do NOT reuse here.

		public static final class ShooterIds {
			public static final int leftShooterCanId = 0;
			public static final int rightShooterCanId = 0;
		}

		public static final class HoodIds {
			public static final int hoodCanId = 18;
		}

		public static final class IntakeIds {
			public static final int rotationCanId = 0;
			public static final int rollerCanId = 0;
		}

		public static final class FeederIds {
			public static final int hopperCanId = 0;
			public static final int feederCanId = 0;
		}
	}

	public static final class TuningValues {

		public static final class ShooterValues {
			public static final double kS = 0;
			public static final double kV = 0;
			public static final double kP = 0;
			public static final double kI = 0;
			public static final double kD = 0;

			public static final double fastSpeed = 10;
			public static final double slowSpeed = 10;

			public static final class AngleFunction {
				public static final double x2coef = 1;
				public static final double xcoef = 0;
				public static final double constant = 0;

				public static double calculate(double input) {
					return x2coef*Math.pow(input,2) + xcoef*input + constant;
				}
			}
 		}

		public static final class HoodValues {
			public static final double kS = 0;
			public static final double kG = 0;
			public static final double kP = 0;
			public static final double kI = 0;
			public static final double kD = 0;
		}

		public static final class IntakeValues {
			public static final double kS = 0;
			public static final double kG = 0;
			public static final double kP = 0;
			public static final double kI = 0;
			public static final double kD = 0;

			public static final double extended = 4;
			public static final double retracted = 0;
			public static final double tolerance = 1;

			public static final double intakeRunSpeed = 0.8;
		}

		public static final class FeederValues {
			public static final double feederRunSpeed = 0.8;
		}

		public static final class HopperValues {
			public static final double hopperRunSpeed = 0.8;
		}
	}

	public static final class CurrentLimits {

		public static final class ShooterLimits {
			public static final double maxLimit = 10;
		}

		public static final class HoodLimits {
			public static final double maxLimit = 8;
			public static final double currentLimit = 4;
			public static final double duration = 0.1;
		}

		public static final class IntakeLimits {
			public static final double maxLimit = 10;
			public static final double currentLimit = 10;
			public static final double duration = 10;
		}

		public static final class IntakeRollerLimits {
			public static final double maxLimit = 50;
		}

		public static final class HopperLimits {
			public static final double maxLimit = 50;
		}

		public static final class FeederLimits {
			public static final double maxLimit = 50;
		}
	}

	public static final class FeildConstants {
		public static boolean isRedAlliance() {
			var alliance = DriverStation.getAlliance();
			return (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red);
		}

		public static final Translation2d redGoal = new Translation2d(12,4);
		public static final Translation2d blueGoal = new Translation2d(4.5,4);
	}

}
