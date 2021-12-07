package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "FTC7760 Auto Blue Top", group = "Blue Auto", preselectTeleOp = "FTC7760 Dual Controller Mode")
public class FTC7760FathomAutoBlueTop extends FTC7760FathomAutoBase {

    public FTC7760FathomAutoBlueTop() {
        super(TSEDetector.CAMERA_1_NAME);
    }

    @Override
    public void runAuto() {
        double movement_speed = 0.25;

        // Start moving the arm up while we're getting into position...
        moveArmForTSE();
        // move forward x amount of fathoms
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 * 2.0);

        
        // rotate while moving to scoring container

        //move arm to x 
         waitForArm();
         
        // spit out block
          spinIntake(true, 2.0);

        //possibly do duck wheel
        
        //park
    
    }
}