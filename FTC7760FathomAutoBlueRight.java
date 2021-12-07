package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.TSEDetector; 

@Autonomous(name = "FTC7760 Auto Blue Right", group = "Blue Auto", preselectTeleOp = "FTC7760 Dual Controller Mode")
public class FTC7760FathomAutoBlueRight extends FTC7760FathomAutoBase {

    public FTC7760FathomAutoBlueRight() {
        super(TSEDetector.CAMERA_1_NAME);
    }

     @Override
    public void runAuto() {
        setStartingHeading(90);

        double movement_speed = 0.25;
        
        // Start moving the arm up while we're getting into position...
        moveArmForTSE();

        driveForFathoms(0.0, -movement_speed, 0.0, -1.0 / 3.0 * 2.0);
        driveForFathoms(-movement_speed, 0.0, 0.0, -1.0 / 3.0 * 0.1);
        waitForArm(); // ... and now wait to be sure the arm is up before we try to score!
        spinIntake(true, 2.0);
        armPresetSafe(); // Don't wait for this one, it'll move while we're driving.
        driveForTime(-movement_speed, 0.0, 0.0, 0.75 / movement_speed);
        driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 * 0.75);
    }
}