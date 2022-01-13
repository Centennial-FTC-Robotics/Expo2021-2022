package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import expo.Robot
import expo.Subsystem

class Intake : Subsystem {
    private lateinit var intakeMotor: DcMotor
    private lateinit var intakeJoint: Servo
    private var turnedOn = false
    override fun initialize(opMode: LinearOpMode) {
        if (opMode.javaClass.isAnnotationPresent(TeleOp::class.java)) {
            initialize()
        }
    }

    private fun initialize() {
        intakeMotor = Robot.opMode.hardwareMap.get(DcMotor::class.java, "intake")
        Robot.opMode.hardwareMap.get(Servo::class.java, "intake servo").also { intakeJoint = it }
        intakeJoint.direction = Servo.Direction.REVERSE
        intakeJoint.position = 0.0
        turnedOn = true

    }

    fun setPower(power: Double) {
        if (!turnedOn) {
            initialize()
        }
        intakeMotor.power = power
    }

    fun setJointPosition(position: Double) {
        if (!turnedOn) {
            initialize()
        }
        intakeJoint.position = position
    }
}