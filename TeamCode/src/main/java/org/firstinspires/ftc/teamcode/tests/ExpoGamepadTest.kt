package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.util.Button
import expo.util.ExpoOpMode

@TeleOp
class ExpoGamepadTest : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()

        controller1.registerToggle(Button.A)

        waitForStart()

        while (opModeIsActive()) {
            controller1.update()

            telemetry.addData("A Toggle", controller1.getToggle(Button.A))
            telemetry.update()
        }
    }
}