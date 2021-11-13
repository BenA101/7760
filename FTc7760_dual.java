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

@TeleOp(name="FTC7760 Dual Controllers", group="Linear Opmode")
public class FTc7760_dual extends LinearOpMode {
    
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
    private BNO055IMU imu = null;
    private final boolean fieldCentricDriving = true;
    private final int armDrivingHeight = -200;
    private final boolean armRaisesAfterIntaking = false;
    private final int armMinLocation = 0;
    private final int armMaxLocation = -4000;
    
 // left front drive 0 left rear 1 right rear 2 front right 3 rear right 4 
 //duckdrive 0 arm 2
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        int armLocation = armDrivingHeight;

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
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftRearDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightRearDrive.setDirection(DcMotor.Direction.FORWARD);
    
        duckDrive.setDirection(DcMotor.Direction.FORWARD);
        duckDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);        
        duckDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        
        armDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armDrive.setTargetPosition(0);
        armDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armDrive.setDirection(DcMotor.Direction.FORWARD); 
        armDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        //                                    P     I   D    F
        armDrive.setVelocityPIDFCoefficients(5.0, 0.1, 2.0, 7.0);
        // armDrive.setPositionPIDFCoefficients(50.0);
        
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);
        
        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        boolean intakePullingIn = false;
        
        while (opModeIsActive()) { 
            double y = -gamepad1.left_stick_y;       //Driving
            if (y < 0.1 && y > -0.1) {
                y = 0;
            }
            double x = gamepad1.left_stick_x;      // Counteract imperfect strafing is now removed
            if (x < 0.1 && x > -0.1) {
                x = 0;
            }
            double rx = gamepad1.right_stick_x;    //Turning
    
            // Compute field-oriented x & y values.

            if (fieldCentricDriving) {
                double angle = -imu.getAngularOrientation().firstAngle;
                telemetry.addData("Heading", "%f", angle);
                double xr = x * Math.cos(angle) - y * Math.sin(angle);
                double yr = x * Math.sin(angle) + y * Math.cos(angle);
                x = xr;
                y = yr;
            }

            /* 
            Denominator is the largest motor power (absolute value) or 1
            This ensures all the powers maintain the same ratio, but only when
            at least one is out of the range [-1, 1]
            */    
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double leftFrontDrivePower = (y + x + rx) / denominator;
            double leftRearDrivePower = (y - x + rx) / denominator;
            double rightFrontDrivePower = (y - x - rx) / denominator;
            double rightRearDrivePower = (y + x - rx) / denominator;
            
            if (gamepad1.right_trigger > 0.1) {     //Halves the power going to the drive wheels 
                SPEED_REDUCER = 0.5;
            } else {
                SPEED_REDUCER = 1.0; 
            }
            
            leftFrontDrive.setPower(leftFrontDrivePower * SPEED_REDUCER);
            leftRearDrive.setPower(leftRearDrivePower * SPEED_REDUCER);
            rightFrontDrive.setPower(rightFrontDrivePower * SPEED_REDUCER);
            rightRearDrive.setPower(rightRearDrivePower * SPEED_REDUCER);
        
                                                    //Quack Wheel
            if (gamepad2.a) {                       //Normal direction (For red duck wheel I think)
                duckDrive.setVelocity(-550);
                if (gamepad2.right_trigger> 0.1){
                    duckDrive.setVelocity(-10000);  //Super fast!!
                } 
            } else if (gamepad2.y) {                //Reverse direction (For blue duck wheel I think)
            duckDrive.setVelocity(550);
                if (gamepad2.right_trigger> 0.1){
                    duckDrive.setVelocity(10000);   //Super fast!!
                } 
            }
            else {
                duckDrive.setVelocity(0);
            }

            // Intake fun timez...    
            if (gamepad1.right_bumper || gamepad1.left_bumper) {    //Spins intake
                if (gamepad1.right_bumper) { // Push out
                    intakeDrive.setPower(1);
                } else if (gamepad1.left_bumper) { // Pull in 
                    intakeDrive.setPower(-1);
                    intakePullingIn = true;
                } 
            } else {
                intakeDrive.setPower(0.0);
                if (intakePullingIn) {
                    intakePullingIn = false;
                    // Pull the arm up to driving height if necessary
                    if (armDrive.getCurrentPosition() > armDrivingHeight && armRaisesAfterIntaking) {
                        armLocation = armDrivingHeight;
                    }
                }
            }

            // Arm action!
            int armLocationDelta = 17;              //Arm increment - Increased to 17 from 15
            if (gamepad2.left_trigger > 0.1){
                armLocationDelta /= 2;
            }
            if (gamepad2.right_bumper) {
                armLocation += armLocationDelta;
            } else if (gamepad2.left_bumper) {
                armLocation -= armLocationDelta;
            } 
            
            if (armLocation > armMinLocation) {                  //Keeps the arm from moving too much
                armLocation = armMinLocation;
            } //else if (armLocation < armMaxLocation) { 
                //armLocation = armMaxLocation;
            //}

            armDrive.setTargetPosition(armLocation);
            armDrive.setPower(1.0);                 //Now with full power! Originally had only 0.5 power
            
            telemetry.addData("Arm", "Location %d", armLocation); 
            telemetry.addData("Arm", "Current position %d", armDrive.getCurrentPosition()); 
            
            //Shows the elapsed game time and arm location. Currently not necessary    
            
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
            
        }
    }
}