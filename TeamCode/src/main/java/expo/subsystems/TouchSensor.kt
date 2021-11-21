package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DigitalChannel
import expo.Subsystem

class TouchSensor : Subsystem {
    lateinit var touchSensor: DigitalChannel
    override fun initialize(opMode: LinearOpMode) {
        touchSensor = opMode.hardwareMap.get(DigitalChannel::class.java, "touch_sensor")
        touchSensor.mode = DigitalChannel.Mode.INPUT
    }

    fun getState() = !touchSensor.state
}