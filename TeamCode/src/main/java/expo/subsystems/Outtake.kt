package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import expo.Subsystem
import expo.command.commands.OuttakeCommand

class Outtake : Subsystem {
    private lateinit var linkage: Servo
    private lateinit var carriage: Servo
    lateinit var joint1: Servo
    lateinit var joint2: Servo
    lateinit var weedJoke3: Servo

    override fun initialize(opMode: LinearOpMode) {
        //the legend of zelda refe
        linkage = opMode.hardwareMap.get(Servo::class.java, "hero chosen by the goddess")
        linkage.scaleRange(0.0, 0.8)
        linkage.direction = Servo.Direction.REVERSE
        linkage.position = .4

        carriage = opMode.hardwareMap.get(Servo::class.java, "carriage")
        joint1 = opMode.hardwareMap.get(Servo::class.java, "joint1")
        joint2 = opMode.hardwareMap.get(Servo::class.java, "joint2")
        weedJoke3 = opMode.hardwareMap.get(Servo::class.java, "weedJoke3")

        OuttakeCommand(OuttakePosition.REST).schedule()
    }

    fun setJoint1(position: Double) {
        joint1.position = position;
    }

    fun setJoint2(position: Double) {
        joint2.position = position;
    }

    fun setJoint3(position: Double) {
        weedJoke3.position = position;
    }

    fun setLinkagePosition(position: Double) {
        linkage.position = position
    }

    fun setCarriagePosition(position: Double) {
        carriage.position = position
    }

    enum class OuttakePosition {
        REST, RIGHT, MIDDLE, LEFT
    }
}