package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "FTC7760 Auto Red Right", group = "Red Auto", preselectTeleOp = "FTC7760 Dual Controller Mode")
public class FTC7760FathomAutoRedRight extends FTC7760FathomAutoBase {

    @Override
    public void runAuto() {
        setStartingHeading(90);

        // TODO: use the TSE position via "tseStartingPosition", it will be LEFT, CENTER, or RIGHT.

        double movement_speed = 0.25;
        armPresetHigh(); // Start moving the arm up while we're getting into position...
        driveForFathoms(0.0, -movement_speed, 0.0, -1.0 / 3.0 * 2.0);
        driveForFathoms(-movement_speed, 0.0, 0.0, -1.0 / 3.0 * 0.1);
        waitForArm(); // ... and now wait to be sure the arm is up before we try to score!
        spinIntake(true, 2.0);
        armPresetSafe(); // Don't wait for this one, it'll move while we're driving.
        driveForTime(0.0, movement_speed, 0.0, 1.25 / movement_speed);
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 * 1.5);
        driveForFathoms(0.0, -movement_speed, 0.0, -1.0 / 3.0 * 0.75);
        armPresetDrive();
        waitForArm(); // Wait for it to finish before we end the OpMode, which will stop all motors.
    }
}