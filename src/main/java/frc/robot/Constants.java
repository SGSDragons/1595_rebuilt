// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import frc.robot.Constants.TuningValues.ShooterValues;

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
			public static final int leftSpinnerCanId = 22;
			public static final int rightSpinnerCanId = 21;
			public static final int feederCanId = 17;
		}

		public static final class ClimberIds {
			public static final int hopperCanId = 13;
		}

		public static final class LimeLightConstants {
			public static final String limelight2 = "limelight-two";
			public static final String limelight3a = "limelight-three";

			public static final double maxReadDistance = 5.0;

		}
	}

	public static final class Aiming {
		public static InterpolatingDoubleTreeMap hoodMap = new InterpolatingDoubleTreeMap();
		public static InterpolatingDoubleTreeMap wheelMap = new InterpolatingDoubleTreeMap();
		public static InterpolatingDoubleTreeMap timeOfFlightMap = new InterpolatingDoubleTreeMap();

		public static double closeShot = 1.3;
		public static double wheelExtra = 0;
		public static double hoodExtra = 1.5 ;

		public static double passingHood = 8.0;
		public static double passingShooter = 70;

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
			hoodMap.put(3.9,4.96);
			hoodMap.put(4.2,5.23);
			hoodMap.put(4.5,5.60);
			hoodMap.put(4.8,6.0);
			hoodMap.put(5.1,6.40);
			hoodMap.put(5.4,6.80);
			hoodMap.put(5.7,7.20);
			hoodMap.put(6.0,7.60);

			wheelMap.put(1.225,65.0);
			wheelMap.put(1.529,65.0);
			wheelMap.put(1.834,67.0);
			wheelMap.put(2.138,68.0);
			wheelMap.put(2.443,70.0);
			wheelMap.put(2.747,72.0);
			wheelMap.put(3.052,75.0);
			wheelMap.put(3.357,77.0);
			wheelMap.put(3.661,80.0);
			wheelMap.put(3.9,82.35);
			wheelMap.put(4.2,85.53);
			wheelMap.put(4.5,88.93);
			wheelMap.put(4.8,92.56);
			// wheelMap.put(5.1,85.0);
			// wheelMap.put(5.4,85.0);
			// wheelMap.put(5.7,85.0);
			// wheelMap.put(6.0,85.0);

			timeOfFlightMap.put(1.225,1.1);
			timeOfFlightMap.put(1.529,1.12);
			timeOfFlightMap.put(1.834,1.14);
			timeOfFlightMap.put(2.138,1.16);
			timeOfFlightMap.put(2.443,1.18);
			timeOfFlightMap.put(2.747,1.2);
			timeOfFlightMap.put(3.052,1.22);
			timeOfFlightMap.put(3.357,1.24);
			timeOfFlightMap.put(3.661,1.25);
			timeOfFlightMap.put(3.9,1.25);
			timeOfFlightMap.put(4.2,1.29);
			timeOfFlightMap.put(4.5,1.31);
			timeOfFlightMap.put(4.8,1.33);
			timeOfFlightMap.put(5.1,1.35);
			timeOfFlightMap.put(5.4,1.37);
			timeOfFlightMap.put(5.7,1.39);
			timeOfFlightMap.put(6.0,1.41);
		}

		public static double getHoodValue(double distance) {
			return hoodMap.get(distance)+hoodExtra;
		}

		public static double getWheelValue(double distance) {
			return wheelMap.get(distance)+wheelExtra;
		}

		public static double getFlightValue(double distance) {
			return timeOfFlightMap.get(distance);
		}

		// Hood Regression
		// y = 0.932525x+1.31822

		// Shooter Regression
		// y = 1.23898x^2 + 0.553157x + 61.35479
	}


	public static final class TuningValues {

		public static final class DrivingValues {
			public static final double joystickPower = 3;
 		}

		public static final class ShooterValues {
			// Increase This
			public static final double kS = 0.3;
			public static final double kV = 0.15;
			public static final double kA = 0;
			public static final double kP = 1.0;
			public static final double kI = 0.8;
			public static final double kD = 0;

			public static double kickVoltage = 4.0;
			
			public static final double runSpeed = 25;
			public static final double maxShooterSpeed = 85.0;
			public static final double tolerance = 15.0;
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
			public static final double kS = 1.0;	
			public static final double kG = 0;
			public static final double kP = 0.4;
			public static final double kI = 0.01;
			public static final double kD = 0.0;

			public static final double upStatic = 3.0;

			public static final double extended = 9.5;
			public static final double retracted = 1.5;
			public static final double tolerance = 0.5;

			// public static final double intakeFastRunSpeed = 0.8;
			// public static final double intakeSlowRunSpeed = 0.4;
			public static final double intakeFastVoltage = 6.0;
			public static final double intakeSlowVoltage = 2.0;
		}

		public static final class HopperValues {
			// public static final double hopperRunSpeed = 0.8;
			public static final double hopperVoltage = 8.0;
			public static final double spinnerVoltage = 8.0;
		}

		public static final class FeederValues {
			// public static final double feederRunSpeed = 0.8;
			public static final double feederVoltage = 6.0;
		}
	}

	public static final class CurrentLimits {

		public static final class ShooterLimits {
			public static final double supplyLimit = 100;
		}

		public static final class HoodLimits {
			public static final double supplyLimit = 40;
			public static final double hardLimit = 25;
			public static final double duration = 0.1;
		}

		public static final class IntakeLimits {
			public static final double statorLimit = 100;
			public static final double hardLimit = 50;
			public static final double duration = 0.1;
			public static final double maxTravelTime = 2;
		}

		public static final class IntakeRollerLimits {
			public static final double supplyLimit = 50;
		}

		public static final class HopperLimits {
			public static final double supplyLimit = 10;
		}

		public static final class SpinnerLimits {
			public static final double supplyLimit = 10;
		}

		public static final class FeederLimits {
			public static final double supplyLimit = 40;
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
