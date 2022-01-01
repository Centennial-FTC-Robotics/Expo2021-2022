package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.Subsystem
import expo.hardware.LinearActuator

class OdometryLifter : Subsystem {
    lateinit var linearActuator: LinearActuator
    override fun initialize(opMode: LinearOpMode) {
        linearActuator = LinearActuator(opMode, "odo actuator")
        if(opMode.javaClass.isAnnotationPresent(TeleOp::class.java)) {
            linearActuator.setPosition(0.0)
        } else {
            linearActuator.setPosition(1.0)
        }
    }

    fun     setPosition(pos: Double) {
        linearActuator.setPosition(pos)
    }
}