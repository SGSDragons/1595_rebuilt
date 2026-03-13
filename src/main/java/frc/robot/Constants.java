// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
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
			public static final int leftShooterCanId = 14;
			public static final int rightShooterCanId = 15;
		}

		public static final class HoodIds {
			public static final int hoodCanId = 19;
		}

		public static final class IntakeIds {
			public static final int rotationCanId = 18;
			public static final int rollerCanId = 20;
		}

		public static final class FeederIds {
			public static final int hopperCanId = 16;
			public static final int feederCanId = 17;
		}

		public static final class ClimberIds {
			public static final int hopperCanId = 13;
		}

		public static final class LimeLightNames {
			public static final String limelight2 = "limelight-two";
			public static final String limelight3a = "limelight-three";
		}
	}

	public static final class Aiming {
		public static InterpolatingDoubleTreeMap hoodMap = new InterpolatingDoubleTreeMap();
		public static InterpolatingDoubleTreeMap wheelMap = new InterpolatingDoubleTreeMap();
		public static InterpolatingDoubleTreeMap timeOfFlightMap = new InterpolatingDoubleTreeMap();

		public static double closeShot = 1.5;
		public static double offset = 0.5;

		static {
			hoodMap.put(1.225,1.5);
			hoodMap.put(1.529,2.75);
			hoodMap.put(1.834,3.0);
			hoodMap.put(2.138,3.25);
			hoodMap.put(2.443,3.65);
			hoodMap.put(2.747,3.90);
			hoodMap.put(3.052,4.20);
			hoodMap.put(3.357,4.50);
			hoodMap.put(3.661,4.65);
			// hoodMap.put(3.0,0.0);
			// hoodMap.put(3.0,0.0);
			// hoodMap.put(3.0,0.0);
			// hoodMap.put(3.0,0.0);

			wheelMap.put(1.225,65.0);
			wheelMap.put(1.529,65.0);
			wheelMap.put(1.834,67.0);
			wheelMap.put(2.138,68.0);
			wheelMap.put(2.443,70.0);
			wheelMap.put(2.747,72.0);
			wheelMap.put(3.052,75.0);
			wheelMap.put(3.357,77.0);
			wheelMap.put(3.661,80.0);
			// wheelMap.put(2.0,75.0);
			// wheelMap.put(2.0,75.0);
			// wheelMap.put(2.0,75.0);
			// wheelMap.put(2.0,75.0);

			timeOfFlightMap.put(1.225,0.0);
			timeOfFlightMap.put(1.529,0.0);
			timeOfFlightMap.put(1.834,0.0);
			timeOfFlightMap.put(2.138,0.0);
			timeOfFlightMap.put(2.443,0.0);
			timeOfFlightMap.put(2.747,0.0);
			timeOfFlightMap.put(3.052,0.0);
			timeOfFlightMap.put(3.357,0.0);
			timeOfFlightMap.put(3.661,0.0);
			// timeOfFlightMap.put(2.0,1.5);
			// timeOfFlightMap.put(2.0,1.5);
			// timeOfFlightMap.put(2.0,1.5);
			// timeOfFlightMap.put(2.0,1.5);
		}

		public static double getHoodValue(double distance) {
			// return hoodMap.get(distance);
			return hoodMap.get(distance+offset);
		}

		public static double getWheelValue(double distance) {
			// return wheelMap.get(distance);
			return wheelMap.get(distance+offset);
		}

		public static double getFlightValue(double distance) {
			return timeOfFlightMap.get(distance);
		}
	}


	public static final class TuningValues {

		public static final class ShooterValues {
			public static final double kS = 0.25;
			public static final double kV = 0.12;
			public static final double kP = 0.1;
			public static final double kI = 0.05;
			public static final double kD = 0;

			public static final double runSpeed = 10;

			// public static final class AngleFunction {
			// 	public static final double x2coef = 1;
			// 	public static final double xcoef = 0;
			// 	public static final double constant = 0;

			// 	public static double calculate(double input) {
			// 		return x2coef*Math.pow(input,2) + xcoef*input + constant;
			// 	}
			// }
 		}

		public static final class HoodValues {
			public static final double kS = 0.6;
			public static final double kG = 0;
			public static final double kP = 1;
			public static final double kI = 0;
			public static final double kD = 0.01;

			public static final double min = 0;
			public static final double max = 8;
			public static final double tolerance = 0.1;
		}

		public static final class IntakeValues {
			public static final double kS = 0.2;
			public static final double kG = 0;
			public static final double kP = 0.4;
			public static final double kI = 0.01;
			public static final double kD = 0.0;

			public static final double extended = 9;
			public static final double retracted = 1.5;
			public static final double tolerance = 0.5;

			public static final double intakeRunSpeed = 0.8;
		}

		public static final class HopperValues {
			public static final double hopperRunSpeed = 0.4;
		}

		public static final class FeederValues {
			public static final double feederRunSpeed = 0.8;
		}
	}

	public static final class CurrentLimits {

		public static final class ShooterLimits {
			public static final double maxLimit = 100;
		}

		public static final class HoodLimits {
			public static final double maxLimit = 60;
			public static final double currentLimit = 25;
			public static final double duration = 0.1;
		}

		public static final class IntakeLimits {
			public static final double maxLimit = 100;
			public static final double currentLimit = 50;
			public static final double duration = 0.1;
			public static final double maxTravelTime = 2;
		}

		public static final class IntakeRollerLimits {
			public static final double maxLimit = 120;
		}

		public static final class HopperLimits {
			public static final double maxLimit = 120;
		}

		public static final class FeederLimits {
			public static final double maxLimit = 120;
		}
	}

	public static final class FieldConstants {
		public static boolean isRedAlliance() {
			var alliance = DriverStation.getAlliance();
			return (alliance.isPresent() && alliance.get() == DriverStation.Alliance.Red);
		}

		public static final Translation2d redGoal = new Translation2d(11.92,4.05);
		public static final Translation2d blueGoal = new Translation2d(4.625,4.05);
	}

}
