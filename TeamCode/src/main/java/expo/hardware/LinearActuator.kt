package expo.hardware

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo

class LinearActuator(opMode: LinearOpMode, name: String) {
    val actuator: Servo

    init {
        actuator = opMode.hardwareMap.servo.get(name)
        actuator.scaleRange(.17, .82)
    }

    fun setPosition(position: Double) {
        val pos = if (position < .04)
            .04
        else
            position

        actuator.position = pos
    }

}