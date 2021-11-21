package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Servo
import expo.Subsystem

class Intake : Subsystem {
    //TODO: getCommand()
    private lateinit var mainMotor: DcMotor
    private lateinit var pusherMotor: DcMotor
    private lateinit var doorServo: Servo

    //test and adjust these as necessary
    private var mainMotorSpeed: Double = 0.05
    private var doorOpenPosition: Double = 0.5
    private var mainMovementDuration: Long = 250
    private var pause: Long = 500

    override fun initialize(opMode: LinearOpMode) {
        mainMotor = opMode.hardwareMap.dcMotor.get("intakeMainMotor")
        pusherMotor = opMode.hardwareMap.dcMotor.get("intakePusherMotor")
        doorServo = opMode.hardwareMap.servo.get("intakeDoorServo")

        mainMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        pusherMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    //make this a command cus thread.sleep() is a bad idea
    public fun intake() {
        togglePusher()
        Thread.sleep(pause)
        raiseIntake() //down by default
        Thread.sleep(pause)
        toggleDoor() //closed by default
        Thread.sleep(pause)
        lowerIntake()
        Thread.sleep(pause)
        togglePusher()
        Thread.sleep(pause)
        toggleDoor()
    }

    private fun togglePusher() {
        //change > to < if power should be negative
        pusherMotor.power = if (pusherMotor.power > 0) 0.0 else 0.1
    }

    private fun raiseIntake() {
        mainMotor.power = mainMotorSpeed
        Thread.sleep(mainMovementDuration)
        mainMotor.power = 0.0
    }

    private fun toggleDoor() {
        doorServo.position = if (doorServo.position > 0) 0.0 else doorOpenPosition
    }

    private fun lowerIntake() {
        //opposite of raiseIntake
        mainMotor.power = -mainMotorSpeed
        Thread.sleep(mainMovementDuration)
        mainMotor.power = 0.0
    }

    fun getCommand() {

    }
}