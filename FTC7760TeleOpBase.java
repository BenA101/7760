
//DON'T put controller input in this file.
//Instead, have control mode files modify input variables.

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

//@TeleOp(name="FTC7760 Dual Controllers", group="Linear Opmode")
public class FTC7760TeleOpBase extends LinearOpMode {

    /*-------------------
    Never mess with these
    ---------------------*/
    
    public ElapsedTime runtime = new ElapsedTime();
    public DcMotor leftFrontDrive = null;
    public DcMotor leftRearDrive = null;
    public DcMotor rightFrontDrive = null;
    public DcMotor rightRearDrive = null;
    public DcMotorEx  duckDrive  = null;
    public DcMotorEx  armDrive  = null;
    public CRServo    intakeDrive = null;
    public BNO055IMU imu = null;
    
    /*-----------------------------------------------
    Constants that are not modified during the match:
    -------------------------------------------------*/
    
    //Currently does nothing
    public final boolean fieldCentricDriving = true;
    
    //The fast and slow speeds of the quack wheel
    public final int quackSlowSpeed = 500;
    public final int quackSuperSpeed = 10000;
    
    //Arm raises after intaking if set to true
    public final boolean armRaisesAfterIntaking = true;
    
    //The height the arm lifts after intaking, if armRaisesAfterIntaking is true
    public final int armDrivingHeight = -200;  // TODO: make all arm heights positive!!!
    
    //Arm minimum and maximum location, used to stop the arm from moving where it cannot move
    public final int armMinLocation = 0;
    public final int armMaxLocation = -4000;
    
    //Base speed of arm movement
    public final int armIncrement = 17;
    
    /*-------------------------------------------------------------------
    Variables used to control the robot that change throughout the match:
    ---------------------------------------------------------------------*/
    
    //Used to control the drive speed
    public double SPEED_REDUCER = 1.0;
    
    //Current quack wheel tick position. Used to spin off a single duck
    public int quackWheelTicks = 0;
    
    //Positions where the quack wheel starts spinning quickly and stops spinning;
    //used to spin off a single duck
    public int quackSuperSpeedTickValue = 400;
    public int quackStoppingPoint = 2000;
    
    //Current speed Quack Wheel is spinnig, if at all
    public int quackWheelSpeed = 0;
    
    //Set to true to ignore minimum arm location
    public boolean armMinLocationIgnore = true;
    
    //Used to tell the arm where to go to
    public int armLocation = armDrivingHeight;
    
    //Variable speed the arm is currently moving at
    public int armLocationDelta = armIncrement;
    
    //True if intake is intaking
    public boolean intakePullingIn = false;
    
    /*-------------
    Input variables
    ---------------*/
    
    public boolean quackWheelSuperSpeedEnabled = false;
    public boolean quackWheelManualBlue = false;
    public boolean quackWheelManualRed = false;
    public boolean quackWheelSingleBlue = false;
    public boolean quackWheelSingleRed = false;
    public boolean intakeIn = false;
    public boolean intakeOut = false;
    public boolean armHalfSpeed = false;
    
    
    public boolean armUp = false;
    public boolean armDown = false;
    
    @Override
    public void runOpMode() {}
    
    //Sets up TeleOp modes. Run at the beginning of every TeleOp mode.
    public void setupRobot() {
        
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
    
        duckDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
    }
    
    //Robot oriented drive function
    public void roboCentricDriving(double y, double x, double rx) {
        if (y < 0.1 && y > -0.1) {
            y = 0;
        }
        if (x < 0.1 && x > -0.1) {
            x = 0;
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
    }
    
    //Function for manually controlling the Quack Wheel
    public void quackWheelManual() {
        
        //Quack wheel spins quickly if quackWheelSuperSpeedEnabled is set to true
        //The quack wheel spins at a certain speed is quackWheelManualBlue or quackWheelManualRed is set to true
        
        //Calculates Speed
        if (quackWheelSuperSpeedEnabled) {
            quackWheelSpeed = quackSuperSpeed;
        } else {
            quackWheelSpeed = quackSlowSpeed;
        }
        
        //Calculates Direction
        if (quackWheelManualBlue) {                       //Normal direction (For red duck wheel I think)
            duckDrive.setVelocity(quackWheelSpeed);
        } else if (quackWheelManualRed) {                //Reverse direction (For blue duck wheel I think)
            duckDrive.setVelocity(-quackWheelSpeed);
        }
        else {
            duckDrive.setVelocity(0);
        }
    }
    
    //Function for spinning off a single duck
    public void quackWheelSingle() {
        
        //Spins off a single duck if quackWheelSingleBlue or quackWheelSingleRed is set to true
        //Setting quackWheelManualBlue or quackWheelManualRed overrides this
        
        //Set quackWheelSingleBlue or quackWheelSingleRed to true to spin off a single Quack
        if (quackWheelSingleBlue || quackWheelSingleRed) {
            duckDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                
            //Initial slow part
            while (Math.abs(quackWheelTicks) < quackSuperSpeedTickValue) {
                if (quackWheelManualBlue || quackWheelManualRed) {
                    break;
                }
                if (quackWheelSingleBlue) {
                    duckDrive.setVelocity(quackSlowSpeed);
                } else if (quackWheelSingleRed) {
                    duckDrive.setVelocity(-quackSlowSpeed);
                }
                quackWheelTicks = duckDrive.getCurrentPosition();
            }
            
            //Fast part at the end
            while ((Math.abs(quackWheelTicks) >= quackSuperSpeedTickValue) &&
                (Math.abs(quackWheelTicks) < quackStoppingPoint)
                ) {
                if (quackWheelManualBlue || quackWheelManualRed) {
                    break;
                }
                if (quackWheelSingleBlue) {
                    duckDrive.setVelocity(quackSuperSpeed);
                } else if (quackWheelSingleRed) {
                    duckDrive.setVelocity(-quackSuperSpeed);
                }
                quackWheelTicks = duckDrive.getCurrentPosition();
            }
        }
    }
    
    // Intake fun timez...    
    public void intake() {
        
        //Intake intakes if intakeIn is set to true
        //Intake reverse intakes if intakeOut is set to true
        
        if (intakeIn || intakeOut) {
            if (intakeOut) {
                intakeDrive.setPower(1);
            } else if (intakeIn) {
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
    }
    
    //Function which resets minimum arm position to current position
    public void armResetMin() {
        
        armDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        armDrive.setTargetPosition(0);
        armDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armDrive.setDirection(DcMotor.Direction.FORWARD); 
        armDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    
    //Function for manually raising the arm up and down by a certain increment
    public void armManual() {
        
        //Raises arm if armUp is set to true
        //Lowers arm if armDown is set to true
        
        armLocationDelta = armIncrement;
        if (armHalfSpeed) {
            armLocationDelta /= 2;
        }
        if (armDown) {
            armLocation += armLocationDelta;
        } else if (armUp) {
            armLocation -= armLocationDelta;
        } 
        
        if (armLocation < armMaxLocation) { 
            armLocation = armMaxLocation;
        } else if (armLocation > armMinLocation && !armMinLocationIgnore) {                  //Keeps the arm from moving too much
            armLocation = armMinLocation;
        }
        
        //Sets the arm to the correct position
        armDrive.setTargetPosition(armLocation);
        armDrive.setPower(1.0);
    }
    
    //Note: armAuto will eventually become a thing
    //armAuto will allow you to set the arm to a useful height at the press of a button
    
    //Function for displaying telemetry
    public void telemetry() {
        
        telemetry.addData("Duck Wheel Ticks", "%d", quackWheelTicks);
        
        telemetry.addData("Arm", "Location %d", armLocation); 
        telemetry.addData("Arm", "Current position %d", armDrive.getCurrentPosition()); 
        
        //Shows the elapsed game time and arm location. Currently not necessary    
        
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.update();
    }
}