package org.firstinspires.ftc.teamcode;

// This allows us to pass some data from our auto opmode to our tele opmode.

public class AutoToTeleStorage {

    // The direction the robot if facing before the auto opmode starts, in degrees.
    // 0 degrees is the front pointing away from the wall.
    // 90 degrees is the front pointing to the RIGHT.
    // -90 degrees is pointing to the LEFT.
    public static double startingHeadingDegrees = 0;

    // This is the last heading from the IMU at the end of the auto opmode, in radians.
    public static double finalAutoHeading = 0.0;
    
    // Used to make the quack wheel spin the right direction by default. Blue is true, false is red
    public static boolean quackDirection = true;
}
