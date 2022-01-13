package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DigitalChannel
import expo.Robot
import expo.Subsystem
import expo.command.CommandScheduler

class WallSensors : Subsystem {
    lateinit var blue: DigitalChannel
    lateinit var red: DigitalChannel
    override fun initialize(opMode: LinearOpMode) {
        blue = opMode.hardwareMap.get(DigitalChannel::class.java, "blue wall")
        red = opMode.hardwareMap.get(DigitalChannel::class.java, "red wall")

        blue.mode = DigitalChannel.Mode.INPUT
        red.mode = DigitalChannel.Mode.INPUT

        CommandScheduler.instance.registerSubsystem(this)
    }

        fun getBlue() = !blue.state

        fun getRed() = !red.state

    override fun update() {
//        if (getBlue()) {
//            Robot.odometry.setStartPos(0.0, Robot.odometry.getPos().getY(), Robot.odometry.getHeading())
//        } else if (getRed()) {
//            Robot.odometry.setStartPos(144.0, Robot.odometry.getPos().getY(), Robot.odometry.getHeading())
//        }
    }

}