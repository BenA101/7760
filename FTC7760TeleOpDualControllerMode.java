/*
    Description: The dual controller mode for TeleOp. Best TeleOp to use with two experienced drivers.
    Controls:
        Gamepad1:
            Driving and strafing is controlled by left joystick
            Rotation is controlled by right joystick (Left is counterclockwise, right is clockwise)
            Drives at half speed while right trigger is held down
            Robot centric driving is activated when a is pressed
            Field centric driving as activated when a AND start is pressed at the same time
            WIP: Field centric driving heading is reset when right joystick is pressed
            Toggles intake in when left bumper is pressed
            Toggles intake out when right bumper is pressed
                Additional Note: Toggling off the current intake mode will always turn the intake completely off
            Spins intake in while left bumper is held AND right trigger is held
            Spins intake out while right bumper is held AND right trigger is held
                Additional Note: Both pressing right trigger and taking your finger off right trigger will toggle off the intake
        Gamepad2:
            Quack wheel spins at default speed while x is held down
            Quack wheel spins at super speed while y is held down
            Quack wheel spins off a single duck when a is pressed
                Additional Note: Pressing x or y overrides and stops a single duck from spinning off, in case the duck is unstable
            Quack wheel spins in opposite direction while right trigger is held down
            Arm is set to high goal position when dpad up is pressed
            Arm is set to middle goal position when dpad right is pressed
            Arm is set to low goal position when dpad down is pressed
            Arm is set to straight up when dpad left is pressed
            Arm is set to drive height when left trigger is pressed
            Arm is set to intake height when left trigger AND right trigger is pressed at the same time
                Additional Note: This is both useful and annoying
                                 It makes it easy to switch between intake and drive height
                                 But it mean you have to take off the left trigger before the right trigger in order to stay at intake height
            Arm rotates up while left bumper is held down
            Arm rotates down while right bumper is held down
            Arm moves down until it touches the limit switch when left joystick is pressed
*/

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "FTC7760 Dual Controller Mode", group = "Linear Opmode")
public class FTC7760TeleOpDualControllerMode extends FTC7760OpBase {

    //Used to keep the intake from repeatedly toggling on and off when the intake button is held down
    boolean holdingIntakeIn = false;
    boolean holdingIntakeOut = false;
    boolean heldRightTrigger = false;

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
            if(gamepad1.right_trigger > 0.1) {
                heldRightTrigger = true;
            }
            if(gamepad1.right_trigger <= 0.1) {
                if (heldRightTrigger) {
                    // Ensures the intake stops spinning when you finish holding right trigger
                    intakeIn = false;
                    intakeOut = false;
                    heldRightTrigger = false;
                }
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
            } else {
                intakeIn = gamepad1.left_bumper;
                intakeOut = gamepad1.right_bumper && !gamepad1.left_bumper;
            }
            holdingIntakeIn = gamepad1.left_bumper;
            holdingIntakeOut = gamepad1.right_bumper;
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

            // Quit armResetMin input
            quitArmResetMin = gamepad2.right_stick_button;
            // Arm Reset Input
            if (gamepad2.left_stick_button) {
                armResetMin();
            }
            
            telemetry();
        }
    }
}