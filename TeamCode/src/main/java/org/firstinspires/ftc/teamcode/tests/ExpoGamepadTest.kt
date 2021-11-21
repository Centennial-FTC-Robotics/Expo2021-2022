package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import expo.util.Button
import expo.util.ExpoOpMode

@TeleOp
class ExpoGamepadTest : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()

        controller1.rumble(5000)

        controller1.registerPressedButton(Button.A)
        while (opModeIsActive()) {
            updateGamepads()
            if(controller1.getPressedButton(Button.A)) {
                telemetry.addData("A IS PRESSED", null)
                telemetry.update()
                gamepad1.rumble(1000)
            }
            telemetry.update()
        }
    }
}