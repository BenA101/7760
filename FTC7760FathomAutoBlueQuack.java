package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "FTC7760 Auto Blue Quack", group = "Blue Auto", preselectTeleOp = "FTC7760 Dual Controller Mode")
public class FTC7760FathomAutoBlueQuack extends FTC7760FathomAutoBase {

    public FTC7760FathomAutoBlueQuack() {
        super(TSEDetector.CAMERA_1_NAME);
    }

    @Override
    public void runAuto() {
        AutoToTeleStorage.quackDirection = true;
        setStartingHeading(0);
        double movement_speed = 0.3;

        // Start moving the arm up while we're getting into position...
        //driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 / 24.0 * 9.0);
        armPresetDrive();
        driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 / 24.0 * 1.0);
        if (tseStartingPosition == TSEDetector.TSEPosition.LEFT) {
            driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 / 24.0 * 0.5);
        } else if (tseStartingPosition == TSEDetector.TSEPosition.RIGHT) {
            driveForFathoms(0.0, -movement_speed, 0.0, -1.0 / 3.0 / 24.0 * 0.5);
        }
        
        // Move forward x amount of fathoms
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 * 1.5);
        
        // Set arm to right height and turn
        moveArmForTSE();
        waitForArm();
        rotateDegrees(87, movement_speed);
         
        // Spit out block
        spinIntake(true, 3.0);
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 / 24.0 * 2.0);
        
        // Set arm to safe height
        armPresetSafe();
        // Not necessary
        // waitForArm();
        
        // Go to Quack Wheel
        rotateDegrees(0, movement_speed);
        driveForTime(0.0, -movement_speed, 0.02, 1.25 / movement_speed);
        driveForTime(movement_speed, 0.0, 0.0, 1.0 / movement_speed);
        
        //Spin Quack Weel
        quackForTime(false, 4.0);
        
        //Park
        driveForFathoms(-movement_speed, 0.0, 0.0, -1.0 / 3.0 * 0.6);
        rotateDegrees(-87, -movement_speed);
        driveForTime(-movement_speed, 0.0, 0.0, 0.5 / movement_speed);
        armResetMin();
    }
}
