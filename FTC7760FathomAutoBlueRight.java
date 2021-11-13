package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="FTC7760 Auto Blue Right", group="Linear Opmode")
public class FTC7760FathomAutoBlueRight extends FTC7760FathomAutoBase {

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        setupOpMode();
        
        driveForFathoms(0.0, -0.5, 0.0, -1.0 / 3.0 * 2.0);
        driveForFathoms(-0.5, 0.0, 0.0, -1.0 / 3.0 * 0.1);
        setArmPosition(-3000);
        spinIntake(true, 2.0);
        setArmPosition(-2000);
        driveForTime(0.5, 0.0, 0.0, 1.5);
        driveForFathoms(0.0, 0.5, 0.0, 1.0 / 3.0 * 1.0);
    }
    
    
}