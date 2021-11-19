package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import expo.Subsystem

class Outtake : Subsystem {
    private lateinit var linkage: Servo
    override fun initialize(opMode: LinearOpMode) {
        linkage = opMode.hardwareMap.get(Servo::class.java, "linkage")
        linkage.scaleRange(0.0, 0.8)
        linkage.direction = Servo.Direction.REVERSE
    }

    fun setLinkagePosition(position: Double) {
        linkage.position = position
    }
}