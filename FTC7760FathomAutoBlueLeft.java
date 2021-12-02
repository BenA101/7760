package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "FTC7760 Auto Blue Left", group = "Blue Auto", preselectTeleOp = "FTC7760 Dual Controller Mode")
public class FTC7760FathomAutoBlueLeft extends FTC7760FathomAutoBase {

    public FTC7760FathomAutoBlueLeft() {
        super(TSEDetector.CAMERA_1_NAME);
    }

    @Override
    public void runAuto() {
        setStartingHeading(-90);

        // TODO: use the TSE position via "tseStartingPosition", it will be LEFT, CENTER, or RIGHT.

        armAutoDrive();
        double movement_speed = 0.25;
        driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 * 2.0);
        driveForFathoms(-movement_speed, 0.0, 0.0, -1.0 / 3.0 * 0.1);
        armAutoHigh();
        spinIntake(true, 2.0);
        armAutoSafe();
        driveForTime(0.0, -movement_speed, 0.0, 1.25 / movement_speed);
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 * 1.5);
        driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 * 0.75);
        setArmPosition(armAutoHeight);
    }
}