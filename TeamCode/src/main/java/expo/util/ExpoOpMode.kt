package expo.util

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.Robot

abstract class ExpoOpMode : LinearOpMode() {
    protected lateinit var controller1: ExpoGamepad
    protected lateinit var controller2: ExpoGamepad
    protected lateinit var robot: Robot
    override fun runOpMode() {
        Robot.motorTester.setMotorName("intake test")
        Robot.motorTester.initialize(this)
        Robot.initialize(this)
        controller1 = ExpoGamepad(gamepad1)
        controller2 = ExpoGamepad(gamepad2)
        waitForStart()
        robot = Robot
    }

    protected fun updateGamepads() {
        controller1.update()
        controller2.update()
    }

}