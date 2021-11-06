package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.util.Button
import expo.util.ExpoOpMode

@TeleOp(name = "Outreach Arm", group = "Outreach")
class OutreachServo : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()

        while (opModeIsActive()) {
            if (controller1.getButton(Button.A)) {
                robot.grabber.setPosition(0.0)
            }

            if (controller1.getButton(Button.B)) {
                robot.grabber.setPosition(1.0)
            }

        }
    }
}