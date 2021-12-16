package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "FTC7760 Auto Red Warehouse", group = "Red Auto", preselectTeleOp = "FTC7760 Dual Controller Mode")
public class FTC7760FathomAutoRedWarehouse extends FTC7760FathomAutoBase {

    public FTC7760FathomAutoRedWarehouse() {
        super(TSEDetector.CAMERA_1_NAME);
    }

    @Override
    public void runAuto() {
        AutoToTeleStorage.quackDirection = false;
        setStartingHeading(0);
        double movement_speed = 0.3;

        // Start moving the arm up while we're getting into position...
        armPresetDrive();
        if (tseStartingPosition == TSEDetector.TSEPosition.LEFT) {
            driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 / 24.0 * 1.0);
        }
        
        // Move forward x amount of fathoms
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 * 1.5);
        
        // Rotate while moving to scoring container
        moveArmForTSE();
        waitForArm();
        rotateDegrees(88, movement_speed);
         
        // Spit out block
        spinIntake(true, 3.0);
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 / 24.0 * 2.0);
        
        // Set arm to safe height
        armPresetSafe();
        // Not needed
        //waitForArm();
        
        // Park
        driveForTime(0.0, movement_speed, -0.02, 1.25 / movement_speed);
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 * 1.25);
        armResetMin();
    }
}
