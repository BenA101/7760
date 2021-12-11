package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "FTC7760 Auto Blue Warehouse", group = "Blue Auto", preselectTeleOp = "FTC7760 Dual Controller Mode")
public class FTC7760FathomAutoBlueWarehouse extends FTC7760FathomAutoBase {

    public FTC7760FathomAutoBlueWarehouse() {
        super(TSEDetector.CAMERA_1_NAME);
    }

    @Override
    public void runAuto() {
        AutoToTeleStorage.quackDirection = true;
        setStartingHeading(0);
        double movement_speed = 0.3;

        // Start moving the arm up while we're getting into position...
        armPresetDrive();
        if (tseStartingPosition == TSEDetector.TSEPosition.LEFT || tseStartingPosition == TSEDetector.TSEPosition.RIGHT) {
            driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 / 24.0 * 1.0);
        }
        
        // Move right to avoid pipes
        driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 / 24.0 * 7.5);
        
        // Move forward x amount of fathoms
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 * 1.5);
        
        // Rotate while moving to scoring container
        moveArmForTSE();
        waitForArm();
        rotateDegrees(-87, -movement_speed);
         
        // Spit out block
        spinIntake(true, 3.0);
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 / 24.0 * 2.0);
        
        // Set arm to safe height
        armPresetSafe();
        // Not needed
        //waitForArm();
        
        // Park
        driveForTime(0.0, -movement_speed, 0.02, 1.25 / movement_speed);
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 * 1.25);
        armPresetDrive();
        waitForArm();
    }
}
