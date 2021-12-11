package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "FTC7760 Dual Controller Mode", group = "Linear Opmode")
public class FTC7760TeleOpDualControllerMode extends FTC7760OpBase {

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
            quackWheelReverse = gamepad2.right_trigger <= 0.1;
            
            // Manual Quack Wheel input
            quackWheelManualDefault = gamepad2.x;
            quackWheelManualSuper = gamepad2.y;
            if (!quackWheelManualDefault) {
                quackWheelManualSuper();
            } else {
                quackWheelManualSlow();
            }

            // Single duck Quack Wheel input
            quackWheelSingle = gamepad2.b;
            quackWheelSingle();

            // Intake input
            intakeIn = gamepad1.left_bumper;
            intakeOut = gamepad1.right_bumper && !gamepad1.left_bumper;
            intake();

            // Arm presets input
            if (gamepad2.dpad_up) {
                armPresetHigh();
            } else if (gamepad2.dpad_right) {
                armPresetMiddle();
            } else if (gamepad2.dpad_down) {
                armPresetLow();
            } else if (gamepad2.dpad_left) {
                armPresetSafe();
            } else if (gamepad2.left_trigger > 0.1 && gamepad2.right_trigger > 0.1) {
                armPresetIntake();
            } else if (gamepad2.left_trigger > 0.1) {
                armPresetDrive();
            }

            // Arm Manual input
            armUp = gamepad2.left_bumper;
            armDown = gamepad2.right_bumper && !gamepad2.left_bumper;
            armManual();

            // Arm Reset Input
            if (gamepad2.left_stick_button) {
                armResetMin();
            }
            
            telemetry();
        }
    }
}