package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//@Autonomous(name="FTC7760 Auto Base", group="Linear Opmode")
public class FTC7760FathomAutoBase extends LinearOpMode {
    
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
    public final int armAutoHeight = -400;
    private final boolean armRaisesAfterIntaking = false;
    private final int armMinLocation = 0;
    private final int armMaxLocation = -4000;
//    private int armLocation = armAutoHeight;
    
 // left front drive 0 left rear 1 right rear 2 front right 3 rear right 4 
 //duckdrive 0 arm 2
    @Override
    public void runOpMode() {}

    // This initializes the robot and waits for the game to start.    
    public void setupOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

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
    }
    
    public void driveForTime(double y, double x, double rx, double time) {
        runtime.reset();
        
        while(opModeIsActive() && runtime.seconds() < time) {
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double leftFrontDrivePower = (y + x + rx) / denominator;
            double leftRearDrivePower = (y - x + rx) / denominator;
            double rightFrontDrivePower = (y - x - rx) / denominator;
            double rightRearDrivePower = (y + x - rx) / denominator;
            
            leftFrontDrive.setPower(leftFrontDrivePower);
            leftRearDrive.setPower(leftRearDrivePower);
            rightFrontDrive.setPower(rightFrontDrivePower);
            rightRearDrive.setPower(rightRearDrivePower);
            }
        leftFrontDrive.setPower(0);
        leftRearDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightRearDrive.setPower(0);
    }       
 
    public void driveForTicks(double y, double x, double rx, double ticks) {
        boolean positiveDir = ticks > 0;
        double endTicks = leftFrontDrive.getCurrentPosition() + ticks;
        
        while(opModeIsActive() &&
        ((positiveDir == true && leftFrontDrive.getCurrentPosition() < endTicks) ||
        (positiveDir == false && leftFrontDrive.getCurrentPosition() > endTicks)))
        {
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double leftFrontDrivePower = (y + x + rx) / denominator;
            double leftRearDrivePower = (y - x + rx) / denominator;
            double rightFrontDrivePower = (y - x - rx) / denominator;
            double rightRearDrivePower = (y + x - rx) / denominator;
            
            leftFrontDrive.setPower(leftFrontDrivePower);
            leftRearDrive.setPower(leftRearDrivePower);
            rightFrontDrive.setPower(rightFrontDrivePower);
            rightRearDrive.setPower(rightRearDrivePower);
        }
        leftFrontDrive.setPower(0);
        leftRearDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightRearDrive.setPower(0);
    }       
    
    public void driveForMm(double y, double x, double rx, double mm) {
        driveForTicks(y, x, rx, mm * 1.782866935);
    }

    public void driveForFathoms(double y, double x, double rx, double fathoms) {
        driveForTicks(y, x, rx, fathoms * 72 * 25.4 * 1.782866935);
    }
    
    public void setArmPosition(int armLocation) {
        armDrive.setTargetPositionTolerance(20);
        armDrive.setTargetPosition(armLocation);
        armDrive.setPower(1.0);
        while(opModeIsActive() && armDrive.isBusy()) {
            telemetry.addData("Arm", "Location %d", armLocation); 
            telemetry.addData("Arm", "Current position %d", armDrive.getCurrentPosition());
            telemetry.update();
        }
    }
    
    //Intakes if direction is set to false
    public void spinIntake(boolean direction, double time) {
        runtime.reset();
        
        double timeLeft;   //For telemetry
        while(opModeIsActive() && runtime.seconds() < time) {
            if (direction) {
                intakeDrive.setPower(1);
            }
            else {
                intakeDrive.setPower(-1);
            }
            timeLeft = time - runtime.seconds();
            telemetry.addData("Intake Status", "Running for %f more seconds", timeLeft);
            telemetry.addData("Intake Status", "Total time %f seconds", time);
            telemetry.update();
        }
        intakeDrive.setPower(0.0);
    }
}
