package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "FTC7760 Auto Red Quack", group = "Red Auto", preselectTeleOp = "FTC7760 Dual Controller Mode")
public class FTC7760FathomAutoRedQuack extends FTC7760FathomAutoBase {

    public FTC7760FathomAutoRedQuack() {
        super(TSEDetector.CAMERA_1_NAME);
    }

    @Override
    public void runAuto() {
        AutoToTeleStorage.quackDirection = false;
        setStartingHeading(0);
        double movement_speed = 0.3;

        // Start moving the arm up while we're getting into position...
        driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 / 24.0 * 9.0);
        armPresetDrive();
        if (tseStartingPosition == TSEDetector.TSEPosition.LEFT) {
            driveForFathoms(0.0, -movement_speed, 0.0, -1.0 / 3.0 / 24.0 * 0.5);
        }
        
        // Move forward x amount of fathoms
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 * 1.5);
        
        // Set arm to right height and turn
        moveArmForTSE();
        waitForArm();
        rotateDegrees(-87, -movement_speed);
         
        // Spit out block
        spinIntake(true, 3.0);
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 / 24.0 * 2.0);
        
        // Set arm to safe height
        armPresetSafe();
        // Not necessary
        //waitForArm();
        
        // Go to Quack Wheel
        driveForTime(0.0, -movement_speed, 0.02, 1.5 / movement_speed);
        driveForFathoms(0.0, movement_speed, -0.02, 1.0 / 3.0 / 24.0 * 7.0);
        driveForTime(movement_speed, 0.0, 0.0, 0.5 / movement_speed);
        
        //Spin Quack Weel
        quackForTime(true, 4.0);
        
        //Park
        driveForFathoms(0.0, movement_speed, -0.01, 1.0 / 3.0 * 0.8);
        rotateDegrees(90, movement_speed);
        driveForTime(-movement_speed, 0.0, 0.0, 0.5 / movement_speed);
        armResetMin();
    }
}
