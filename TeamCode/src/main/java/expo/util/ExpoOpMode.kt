package expo.util

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.Robot

abstract class ExpoOpMode : LinearOpMode() {
    protected val robot: Robot = Robot()
    protected lateinit var controller1 : ExpoGamepad
    protected lateinit var controller2: ExpoGamepad
    override fun runOpMode() {
        robot.initialize(this)
        controller1 = ExpoGamepad(gamepad1)
        controller2 = ExpoGamepad(gamepad2)
        waitForStart()
    }

    protected fun updateGamepads() {
        controller1.update()
        controller2.update()
    }

}