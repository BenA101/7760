/*
    Description: A single controller mode with limited controls. Focuses on usability. Ideal for practicing without second drivers.
    Controls (One gamepad only: Gamepad1):
        Default Mode is active while the right trigger is not held down
        Linear Arm Mode is active while the right trigger is held down

        All Modes:
            Driving and strafing is controlled by left joystick
            Rotation is controlled by right joystick (Left is counterclockwise, right is clockwise)
            Quack wheel spins at default speed while x is held down
            Quack wheel spins at super speed while y is held down
            Quack wheel spins off a single duck when a is pressed
                Additional Note: Pressing x or y overrides and stops a single duck from spinning off, in case the duck is unstable
            Arm is set to high goal position when dpad up is pressed
            Arm is set to middle goal position when dpad right is pressed
            Arm is set to low goal position when dpad down is pressed
            Arm is set to straight up when dpad left is pressed

        Default Only:
            Toggles intake in when left bumper is pressed
            Toggles intake out when right bumper is pressed
                Additional Note: Toggling off the current mode will always turn the intake completely off
            Arm is set to drive height when left trigger is pressed
                Additional Note: See "Presets Note"

        Linear Arm Only:
            Quack wheel spins in opposite direction
            Arm is set to intake height when left trigger is pressed
                Presets Note: This is both useful and annoying
                              It makes it easy to switch between intake and drive height
                              But it mean you have to take off the left trigger before the right trigger in order to stay at intake height
            Arm rotates up while left bumper is held down
            Arm rotates down while right bumper is held down
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "FTC7760 Single Controller Limited Mode", group = "Linear Opmode")
public class FTC7760TeleOpSingleControllerLimited extends FTC7760OpBase {
    
    //Used to control toggling intake
    boolean holdingIntakeIn = false;
    boolean holdingIntakeOut = false;
    
    @Override
    public void runOpMode() {
        setupRobot();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();
        armResetMin();
        
        while (opModeIsActive()) {
            
            // Changing the mode changes how some controls work
            int mode = 0;
            if (gamepad1.right_trigger >= 0.1) {
                mode = 1;
            }

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
            quackWheelReverse = mode == 1;
            
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
            if (mode == 1) {
                armUp = gamepad1.left_bumper;
                armDown = gamepad1.right_bumper && !gamepad1.left_bumper;
            } else {
                if (gamepad1.left_bumper && !holdingIntakeIn) {
                    intakeIn = !intakeIn;
                    if (intakeIn) {
                        intakeOut = false;
                    }
                } else if (gamepad1.right_bumper && !holdingIntakeOut) {
                    intakeOut = !intakeOut;
                    if (intakeOut) {
                        intakeIn = false;
                    }
                }
            }
            holdingIntakeIn = gamepad1.left_bumper;
            holdingIntakeOut = gamepad1.right_bumper;
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