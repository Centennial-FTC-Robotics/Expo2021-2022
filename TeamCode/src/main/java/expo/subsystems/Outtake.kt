package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import expo.Subsystem
import expo.hardware.Joint

class Outtake : Subsystem {
    private lateinit var linkage: Servo
    private lateinit var carriage: Servo
    private lateinit var joint1: Joint
    private lateinit var joint2: Joint
    private lateinit var weedJoke3: Joint

    override fun initialize(opMode: LinearOpMode) {
        linkage = opMode.hardwareMap.get(Servo::class.java, "linkage")
        linkage.scaleRange(0.0, 0.8)
        linkage.direction = Servo.Direction.REVERSE

        carriage = opMode.hardwareMap.get(Servo::class.java, "carriage")

        joint1 = Joint(0.0, 0.0, 0.0, 0.0, opMode, "joint1");
        joint2 = Joint(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, opMode, "joint2");
        weedJoke3 = Joint(0.0, 0.0, 0.0, 0.0, opMode, "weedJoke3");
    }

    fun setJoint1(position: Joint.Position) {
        joint1.setPosition(position);
    }

    fun setJoint2(position: Joint.Position) {
        joint2.setPosition(position);
    }

    fun setJoint3(position: Joint.Position) {
        weedJoke3.setPosition(position);
    }

    fun setLinkagePosition(position: Double) {
        linkage.position = position
    }

    fun setCarriagePosition(position: Double) {
        carriage.position = position
    }
}