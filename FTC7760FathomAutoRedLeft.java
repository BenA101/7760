package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="FTC7760 Auto Red Left", group="Linear Opmode")
public class FTC7760FathomAutoRedLeft extends FTC7760FathomAutoBase {

    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        
        setupOpMode();
        
        double movement_speed = 0.25;
        driveForFathoms(0.0, movement_speed, 0.0, 1.0 / 3.0 * 2.0);
        driveForFathoms(-movement_speed, 0.0, 0.0, -1.0 / 3.0 * 0.1);
        setArmPosition(-3000);
        spinIntake(true, 2.0);
        setArmPosition(-2000);
        driveForTime(movement_speed, 0.0, 0.0, 0.75 / movement_speed);
        driveForFathoms(0.0, -movement_speed, 0.0, -1.0 / 3.0 * 0.75);
    }
    
    
}