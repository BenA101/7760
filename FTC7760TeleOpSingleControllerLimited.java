package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "FTC7760 Single Controller Limited Mode", group = "Linear Opmode")
public class FTC7760TeleOpSingleControllerLimited extends FTC7760OpBase {

    @Override
    public void runOpMode() {
        setupRobot();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        armResetMin();
        
        while (opModeIsActive()) {

            if (gamepad1.start) {
                fieldCentricDriving = gamepad1.a;
            }

            // Driving input
            drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,
                    gamepad1.right_trigger > 0.1);
            
            // Reset heading
            if (gamepad1.right_stick_button) {
                resetHeading();
            }
            
            //Used for manual and single Quack Wheel
            quackWheelReverse = gamepad1.right_trigger <= 0.1;
            
            // Manual Quack Wheel input
            quackWheelManualDefault = gamepad1.x;
            quackWheelManualSuper = gamepad1.y;
            if (!quackWheelManualDefault) {
                quackWheelManualSuper();
            } else {
                quackWheelManualSlow();
            }

            // Single duck Quack Wheel input
            quackWheelSingle = gamepad1.b;
            quackWheelSingle();

            // Intake input & arm input
            if (gamepad1.right_trigger <= 0.1) {
            //Default mode
                intakeIn = gamepad1.left_bumper;
                intakeOut = gamepad1.right_bumper && !gamepad1.left_bumper;
            } else {
            //Linear Arm mode
                armUp = gamepad1.left_bumper;
                armDown = gamepad1.right_bumper && !gamepad1.left_bumper;
            }
            intake();
            armManual();

            // Arm presets input
            if (gamepad1.dpad_up) {
                armPresetHigh();
            } else if (gamepad1.dpad_right) {
                armPresetMiddle();
            } else if (gamepad1.dpad_down) {
                armPresetLow();
            } else if (gamepad1.dpad_left) {
                armPresetSafe();
            } else if (gamepad1.left_trigger > 0.1 && gamepad1.right_trigger > 0.1) {
                armPresetIntake();
            } else if (gamepad1.left_trigger > 0.1) {
                armPresetDrive();
            }

            // Arm Reset Input
            if (gamepad1.left_stick_button) {
                armResetMin();
            }
            
            telemetry();
        }
    }
}