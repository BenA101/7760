package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "FTC7760 Auto Blue Top", group = "Blue Auto", preselectTeleOp = "FTC7760 Dual Controller Mode")
public class FTC7760FathomAutoBlueTop extends FTC7760FathomAutoBase {

    @Override
    public void runAuto() {
        double movement_speed = 0.25;
        
        armAutoDrive();
        driveForFathoms(movement_speed, 0.0, 0.0, 1.0 / 3.0 * 1.5);
    }
}