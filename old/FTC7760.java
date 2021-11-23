package org.firstinspires.ftc.teamcode.old;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="FTC7760 Dual Controllers old 2", group="Linear Opmode")
@Disabled

public class FTC7760 extends LinearOpMode {

    private double SPEED_REDUCER = 1.0;

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftRearDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightRearDrive = null;
    private DcMotorEx  duckDrive  = null;
    private DcMotorEx  armDrive  = null;
    private CRServo    intakeDrive = null;
    
 // left front drive 0 left rear 1 right rear 2 front right 3 rear right 4 
 //duckdrive 0 arm 2
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        int armLocation=-400;

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "leftFrontDrive");
        leftRearDrive = hardwareMap.get(DcMotor.class, "leftRearDrive");
        rightFrontDrive  = hardwareMap.get(DcMotor.class, "rightFrontDrive");
        rightRearDrive  = hardwareMap.get(DcMotor.class, "rightRearDrive");
        duckDrive  = hardwareMap.get(DcMotorEx.class, "Quack wheel");
        armDrive  = hardwareMap.get(DcMotorEx.class, "arm");
        intakeDrive  = hardwareMap.get(CRServo.class, "intake");
        
        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftRearDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightRearDrive.setDirection(DcMotor.Direction.REVERSE);
    
        duckDrive.setDirection(DcMotor.Direction.FORWARD);
        duckDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);        
        duckDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        
        armDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armDrive.setTargetPosition(0);
        armDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armDrive.setDirection(DcMotor.Direction.FORWARD); 
        armDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //                                    P     I   D    F
        armDrive.setVelocityPIDFCoefficients(9.0, 7.0, 0.0, 0.0);
        // armDrive.setPositionPIDFCoefficients(50.0);
        
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) { 
            if (armLocation > 0) { 
                armLocation = 0;
            } else if (armLocation < -3700) { 
                armLocation = -3700;
            }
            
            armDrive.setTargetPosition(armLocation);
            armDrive.setPower(0.5);
            
            telemetry.addData("ARM MOTOR", "%d", armDrive.getCurrentPosition()); 
            
            
            double y = gamepad1.left_stick_y; 
            double x = -gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = -gamepad1.right_stick_x;
    
//          double rx = 0.0;
            
             // Denominator is the largest motor power (absolute value) or 1
             // This ensures all the powers maintain the same ratio, but only when
             // at least one is out of the range [-1, 1]
                
             double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
             double leftFrontDrivePower = (y + x + rx) / denominator;
             double leftRearDrivePower = (y - x + rx) / denominator;
             double rightFrontDrivePower = (y - x - rx) / denominator;
             double rightRearDrivePower = (y + x - rx) / denominator;
               
             leftFrontDrive.setPower(leftFrontDrivePower*SPEED_REDUCER);
             leftRearDrive.setPower(leftRearDrivePower*SPEED_REDUCER);
             rightFrontDrive.setPower(rightFrontDrivePower*SPEED_REDUCER);
             rightRearDrive.setPower(rightRearDrivePower*SPEED_REDUCER);
            
        
          
            if (gamepad2.a) {
                duckDrive.setVelocity(-700);
                if (gamepad2.right_trigger> 0.1){
                    duckDrive.setVelocity(-10000);
                }
            } 
            else {
                duckDrive.setVelocity(0);
            }
        
            int armLocationDelta = 10;
            if (gamepad2.left_trigger > 0.1){
                armLocationDelta /= 2;
            }
            if (gamepad2.right_bumper) {
                armLocation += armLocationDelta;
            } else if (gamepad2.left_bumper) {
                armLocation -= armLocationDelta;
            } 
    
            if (gamepad1.right_bumper || gamepad1.left_bumper) {
                if (gamepad1.right_bumper) {
                    intakeDrive.setPower(1);
                } else if (gamepad1.left_bumper) {
                    intakeDrive.setPower(-1);
                } 
            } else {
                intakeDrive.setPower(0.0);
            }
            if (gamepad1.right_trigger > 0.1) { 
                SPEED_REDUCER = 0.5;
            } else {
                SPEED_REDUCER = 1.0; 
            }
                
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            //telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower); 
            telemetry.update();
        }
    }
}
    