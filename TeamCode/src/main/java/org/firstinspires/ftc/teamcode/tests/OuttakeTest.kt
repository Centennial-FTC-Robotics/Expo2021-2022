package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.ServoImplEx
import expo.util.Button
import expo.util.ExpoOpMode

@TeleOp(name = "Outtake Test", group = "Tests")
class OuttakeTest : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()

        controller1.registerPressedButton(Button.UP)
        controller1.registerPressedButton(Button.DOWN)
        controller1.registerPressedButton(Button.LEFT_BUMPER)
        var linkagePos = 0.0
        while (opModeIsActive()) {
            updateGamepads()
            if (controller1.getPressedButton(Button.UP)) {
                linkagePos += 0.1
                if (controller1.getButton(Button.RIGHT_BUMPER))
                    linkagePos -= .05
            }
            if (controller1.getPressedButton(Button.DOWN)) {
                linkagePos -= 0.1
                if (controller1.getButton(Button.RIGHT_BUMPER))
                    linkagePos += .05
            }
            if (controller1.getPressedButton(Button.LEFT_BUMPER)) {
                linkagePos = if (linkagePos == 1.0)
                    0.0
                else
                    1.0
            }


            if (linkagePos > 1.0)
                linkagePos = 1.0
            if (linkagePos < 0)
                linkagePos = 0.0


            robot.outtake.setLinkagePosition(linkagePos)
            telemetry.addData("Linkage Pos", linkagePos)
            telemetry.update()
        }
    }
}