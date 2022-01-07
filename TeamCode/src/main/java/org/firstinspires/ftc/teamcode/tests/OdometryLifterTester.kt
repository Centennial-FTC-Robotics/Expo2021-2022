package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.gamepad.Button
import expo.util.ExpoOpMode

@TeleOp(name = "Odometry Lifter Tester", group = "Tester")
class OdometryLifterTester : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        var actuatorPos = 0.0
        controller1.registerPressedButton(Button.UP)
        controller1.registerPressedButton(Button.DOWN)
        controller1.registerPressedButton(Button.LEFT_BUMPER)


        while (opModeIsActive()) {
            updateGamepads()
            if (controller1.getPressedButton(Button.UP)) {
                actuatorPos += 0.1
                if (controller1.getButton(Button.RIGHT_BUMPER))
                    actuatorPos -= .08
            }
            if (controller1.getPressedButton(Button.DOWN)) {
                actuatorPos -= 0.1
                if (controller1.getButton(Button.RIGHT_BUMPER))
                    actuatorPos += .08
            }


            if (actuatorPos > 1.0)
                actuatorPos = 1.0
            if (actuatorPos < 0)
                actuatorPos = 0.0

            if (controller1.getPressedButton(Button.LEFT_BUMPER)) {
                actuatorPos = if (actuatorPos == 1.0)
                    0.0
                else
                    1.0
            }

            robot.odoLifter.setPosition(actuatorPos)
            telemetry.addData("pos", actuatorPos)
            telemetry.update()
        }
    }
}