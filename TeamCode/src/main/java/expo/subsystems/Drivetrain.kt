package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import expo.Subsystem

class Drivetrain : Subsystem {
    private lateinit var frontLeft: DcMotor
    private lateinit var frontRight: DcMotor
    private lateinit var backLeft: DcMotor
    private lateinit var backRight: DcMotor

    override fun initialize(opMode: LinearOpMode) {
        frontLeft = opMode.hardwareMap.dcMotor.get("frontLeft")
        frontRight = opMode.hardwareMap.dcMotor.get("frontRight")
        backLeft = opMode.hardwareMap.dcMotor.get("backLeft")
        backRight = opMode.hardwareMap.dcMotor.get("backRight")

        frontLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        frontRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        backLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        backRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    public fun setMotorPowers(frontLeft: Double, frontRight: Double, backLeft: Double, backRight: Double) {
        this.frontLeft.power = frontLeft
        this.frontRight.power = frontRight
        this.backLeft.power = backLeft
        this.backRight.power = backRight
    }
}