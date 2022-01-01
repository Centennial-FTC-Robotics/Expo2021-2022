package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import expo.Subsystem

class Intake : Subsystem {
    private lateinit var intakeMotor: DcMotor
    private lateinit var intakeJoint: Servo
    override fun initialize(opMode: LinearOpMode) {
        intakeMotor = opMode.hardwareMap.get(DcMotor::class.java, "intake")
        intakeJoint = opMode.hardwareMap.get(Servo::class.java, "intake servo")
        intakeJoint.direction = Servo.Direction.REVERSE
        intakeJoint.position = 0.0
    }

    fun setPower(power: Double) {
        intakeMotor.power = power
    }

    fun setJointPosition(position: Double) {
        intakeJoint.position = position
    }
}