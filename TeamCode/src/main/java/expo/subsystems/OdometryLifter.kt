package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.Subsystem
import expo.hardware.LinearActuator

class OdometryLifter : Subsystem {
    lateinit var linearActuator: LinearActuator
    override fun initialize(opMode: LinearOpMode) {
        linearActuator = LinearActuator(opMode, "odo actuator")
    }

    fun setPosition(pos: Double) {
        linearActuator.setPosition(pos)
    }
}