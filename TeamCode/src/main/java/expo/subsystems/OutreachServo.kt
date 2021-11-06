package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import expo.Subsystem

class OutreachServo : Subsystem {
    private lateinit var servo: Servo
    override fun initialize(opMode: LinearOpMode) {
        servo = opMode.hardwareMap.servo.get("outreach_servo")
    }

    fun setPosition(position: Double) {
        servo.position = position
    }
}