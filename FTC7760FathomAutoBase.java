package org.firstinspires.ftc.teamcode;

// This is the base class for all of our autonomous modes. It adds methods to drive, move the arm
// and intake, etc. to our normal opmode base.

public abstract class FTC7760FathomAutoBase extends FTC7760OpBase {

    // We're using the following motors from goBILDA for our drivetrain:
    //   - 5203 Series Yellow Jacket Planetary Gear Motor (19.2:1 Ratio, 24mm Length 8mm REX Shaft, 312 RPM, 3.3 - 5V Encoder)
    //   - https://www.gobilda.com/5203-series-yellow-jacket-planetary-gear-motor-19-2-1-ratio-24mm-length-8mm-rex-shaft-312-rpm-3-3-5v-encoder/
    //
    //   From the website:
    //     - Encoder Resolution: 537.7 PPR at the Output Shaft (PPR is Pulses Per Revolution, i.e., encoder ticks)
    //
    // Our mecanum wheels are 96mm diameter, thus the equation below.
    public static final double TICKS_PER_MM = 537.7 / (96 * Math.PI);

    // Default to the highest position for the best score if the camera fails, or we don't use one.
    protected TSEDetector.TSEPosition tseStartingPosition = TSEDetector.TSEPosition.RIGHT;
    private final String cameraName;

    // Setup the auto mode with no camera
    public FTC7760FathomAutoBase() {
        cameraName = null;
    }

    // Setup the auto mode, selecting a camera to use
    public FTC7760FathomAutoBase(String cameraName) {
        this.cameraName = cameraName;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        setupRobot();

        if (cameraName != null) {
            TSEDetector tseDetector = new TSEDetector(hardwareMap);
            tseDetector.setupCamera(cameraName);

            // Display the TSE position while we're waiting to start the opmode.
            while (!isStarted()) {
                telemetry.addData("TSE Detector", "Camera %s", cameraName);
                telemetry.addData("TSE Detector", "Position %s", tseDetector.getPosition());
                telemetry.update();

                // Sleep a bit so we don't waste battery while waiting. 20 times a second is plenty.
                sleep(50);
            }

            // Remember the position of the TSE before we start moving the bot!
            tseStartingPosition = tseDetector.getPosition();
        } else {
            waitForStart();
        }

        runAuto();
        rememberFinalHeading();
    }

    abstract public void runAuto() throws InterruptedException;

    public void setStartingHeading(int degrees) {
        AutoToTeleStorage.startingHeadingDegrees = degrees;
    }

    public void rememberFinalHeading() throws InterruptedException {
        AutoToTeleStorage.finalAutoHeading = getHeading();
    }

    public void driveForTime(double y, double x, double rx, double time) {
        runtime.reset();

        while (opModeIsActive() && runtime.seconds() < time) {
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

    //Power to positive y moves the robot forward. Power to positive x moves the robot right.
    //Rx is rotation power.
    //In order to reverse the direction, you must multiply both the power and distance by -1.
    public void driveForTicks(double y, double x, double rx, double ticks) {
        boolean positiveDir = ticks > 0;
        double endTicks = leftFrontDrive.getCurrentPosition() + ticks;

        while (opModeIsActive() &&
                ((positiveDir && leftFrontDrive.getCurrentPosition() < endTicks) ||
                        (!positiveDir && leftFrontDrive.getCurrentPosition() > endTicks))) {
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
        driveForTicks(y, x, rx, mm * TICKS_PER_MM);
    }

    public void driveForFathoms(double y, double x, double rx, double fathoms) {
        driveForTicks(y, x, rx, fathoms * 72 * 25.4 * TICKS_PER_MM);
    }


    public void rotateDegrees(double degrees, double rx) {
        double x = 0;
        double y = 0;

        double endAngle = Math.toRadians(degrees);
        boolean positiveDir = getHeading() < degrees;

        while (opModeIsActive() &&
                ((positiveDir && getHeading() < endAngle) ||
                        (!positiveDir && getHeading() > endAngle))) {
            telemetry.addData("Bot Heading", "%f", getHeading());
            telemetry.addData("Destination Heading", "%f", endAngle);
            telemetry.update();

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

    // Intakes if direction is set to false
    public void spinIntake(boolean direction, double time) {
        runtime.reset();

        double timeLeft;   // For telemetry
        while (opModeIsActive() && runtime.seconds() < time) {
            if (direction) {
                intakeDrive.setPower(1);
            } else {
                intakeDrive.setPower(-1);
            }
            timeLeft = time - runtime.seconds();
            telemetry.addData("Intake Status", "Running for %f more seconds", timeLeft);
            telemetry.addData("Intake Status", "Total time %f seconds", time);
            telemetry.update();
        }
        intakeDrive.setPower(0.0);
    }

    // Waits for the arm to get to it's desired position. Call this after asking it to go to a
    // preset.
    public void waitForArm() {
        while (opModeIsActive() && armDrive.isBusy()) {
            telemetry.addData("Arm", "Desired position %d", armLocation);
            telemetry.addData("Arm", "Current position %d", armDrive.getCurrentPosition());
            telemetry.update();
        }
    }
    
    public void moveArmForTSE(){
        if (tseStartingPosition == TSEDetector.TSEPosition.RIGHT) {
            armPresetHigh(); 
        } else if (tseStartingPosition == TSEDetector.TSEPosition.CENTER) {
            armPresetMiddle();
        } else {
            armPresetLow();
        }  
    }
}
