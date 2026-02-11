// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

	public static final class HardwareID {

		// All the CANBus and Channel IDs used on the robot. If any of these IDs are repeated,
		// then things won't work correctly
		//
		// Can ID -1 denotes numbers that have yet to be programmed on devices.
		// 
		// IDs 3-14 are used by the swerve modules. Do NOT reuse here.

		public static final class ShooterIds {
			public static final int leftShooterCanId = 16;
			public static final int rightShooterCanId = 17;
			public static final int hoodCanId = 20;
		}
	}

	public static final class TuningValues {

		public static final class ShooterValues {
			public static final double kS = 0;
			public static final double kV = 0;
			public static final double kP = 0;
			public static final double kI = 0;
			public static final double kD = 0;

			public static final double runSpeed = 10;
		}

		public static final class HoodValues {
			public static final double kS = 0;
			public static final double kG = 0;
			public static final double kP = 0;
			public static final double kI = 0;
			public static final double kD = 0;

			public static final double minLimit = 1;
			public static final double maxLimit = 10;
		}
	}
}
