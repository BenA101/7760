package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "FTC7760 Auto Red Quack", group = "Red Auto", preselectTeleOp = "FTC7760 Dual Controller Mode")
public class FTC7760FathomAutoRedQuack extends FTC7760FathomAutoBase {

    public FTC7760FathomAutoRedQuack() {
        super(TSEDetector.CAMERA_1_NAME);
    }

    @Override
    public void runAuto() {
        double movement_speed = 0.3;

        // Start moving the arm up while we're getting into position...
        driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 / 24.0 * 6.0);
        armPresetDrive();
        if (tseStartingPosition != TSEDetector.TSEPosition.RIGHT) {
            driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 / 24.0 * 3.0);
        }
        
        // Move forward x amount of fathoms
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 * 1.76);
        
        // Set arm to right height and turn
        moveArmForTSE();
        waitForArm();
        rotateDegrees(-88, -movement_speed);
         
        // Spit out block
        spinIntake(true, 3);
        
        // Set arm to safe height
        armPresetSafe();
        waitForArm();
        
        // Go to Quack Wheel
        driveForTime(0.0, -movement_speed, 0.02, 1.5 / movement_speed);
        driveForFathoms(0.0, movement_speed, -0.02, 1.0 / 3.0 / 24.0 * 7.0);
        driveForTime(movement_speed, 0.0, 0.0, 0.5 / movement_speed);
        
        //Spin Quack Weel
        quackForTime(true, 4.0);
        
        //Park
        driveForFathoms(0.0, movement_speed, -0.01, 1.0 / 3.0 * 1.0);
        driveForTime(movement_speed, 0.0, 0.0, 0.5 / movement_speed);
        rotateDegrees(90, movement_speed);
    }
}
