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

        while (opModeIsActive()) {

            if (gamepad1.start) {
                fieldCentricDriving = gamepad1.a;
            }

            // Driving input
            drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,
                    gamepad1.right_trigger > 0.1);

            // Manual Quack Wheel input
            quackWheelManualBlue = gamepad2.a;
            quackWheelManualRed = gamepad2.y && !gamepad2.a;
            quackWheelManual();

            // Single duck Quack Wheel input
            quackWheelSingleBlue = gamepad2.x;
            quackWheelSingleRed = gamepad2.b && !gamepad2.x;
            quackWheelSingle();

            // Intake input
            intakeIn = gamepad1.left_bumper;
            intakeOut = gamepad1.right_bumper && !gamepad1.left_bumper;
            intake();

            // Arm input
            armUp = gamepad2.left_bumper;
            armDown = gamepad2.right_bumper && !gamepad2.left_bumper;
            armManual();

            telemetry();
        }
    }
}