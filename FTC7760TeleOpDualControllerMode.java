package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="FTC7760 Dual Controller Mode", group="Linear Opmode")
public class FTC7760TeleOpDualControllerMode extends FTC7760TeleOpBase {
    
    //I don't actually know how to run functions in Java, hilariously.
    
    @Override
    
    public void runOpMode() {
        while (opModeIsActive()) {
            
            //Driving input
            y = -gamepad1.left_stick_y;
            x = gamepad1.left_stick_x;
            rx = gamepad1.right_stick_x;
            
            //Manual Quack Wheel input
            if (gamepad2.a) {
                quackWheelManualBlue = true;
            } else {
                quackWheelManualBlue = false;
            }
            if (gamepad2.y && !gamepad2.a) {
                quackWheelManualRed = true;
            } else {
                quackWheelManualRed = false;
            }
            
            //Single duck Quack Wheel input
            if (gamepad2.x) {
                quackWheelSingleBlue = true;
            } else {
                quackWheelSingleBlue = false;
            }
            if (gamepad2.b && !gamepad2.x) {
                quackWheelSingleRed = true;
            } else {
                quackWheelSingleBlue = false;
            }
            
            //Intake input
            if (gamepad1.left_bumper) {
                intakeIn = true;
            } else {
                intakeIn = false;
            }
            if (gamepad1.right_bumper && !gamepad1.left_bumper) {
                intakeOut = true;
            } else {
                intakeOut = false;
            }
            
            //Arm input
            if (gamepad2.left_bumper) {
                armUp = true;
            } else {
                armUp = false;
            }
            if (gamepad2.right_bumper && !gamepad2.left_bumper) {
                armDown = true;
            } else {
                armDown = false;
            }
        }
    }
}