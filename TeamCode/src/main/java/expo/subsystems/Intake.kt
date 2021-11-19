package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import expo.Subsystem

class Intake : Subsystem {
    lateinit var intakeServo: Servo
    lateinit var intakeMotor: DcMotor
    override fun initialize(opMode: LinearOpMode) {
        intakeServo = opMode.hardwareMap.servo.get("intake servo")
        intakeMotor = opMode.hardwareMap.dcMotor.get("intake motor")
        intakeMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        intakeServo.direction = Servo.Direction.REVERSE
        intakeServo.scaleRange(.55, 1.0)
    }

    fun setIntakePower(power: Double) {
        intakeMotor.power = power
    }

    fun setIntakePosition(position: Double) {
        intakeServo.position = position
    }
}