package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="FTC7760 Auto Blue Left", group="Linear Opmode")
public class FTC7760FathomAutoBlueLeft extends FTC7760FathomAutoBase {

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        setupOpMode();
        
        driveForFathoms(0.0, -0.5, 0.0, -1.0 / 3.0 * 1.0);
        driveForFathoms(0.5, 0.0, 0.0, 1.0 / 3.0 * 0.75);

    }
    
    
}