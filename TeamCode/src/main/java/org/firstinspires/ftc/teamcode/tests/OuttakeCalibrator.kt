package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.Robot
import expo.gamepad.Button
import expo.logger.Logger
import expo.util.ExpoOpMode

@TeleOp(name = "Outtake Calibrator", group = "Calibrator")
class OuttakeCalibrator : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        val positions = arrayOf(0.04, 1.0, .59, 0.0)
        var index = 0
        controller1.registerPressedButton(Button.Y)
        controller1.registerPressedButton(Button.A)
        controller1.registerPressedButton(Button.UP)
        controller1.registerPressedButton(Button.DOWN)

        while (opModeIsActive()) {
            if (controller1.getPressedButton(Button.Y)) {
                index = (index + 1) % positions.size
            }

            if (controller1.getPressedButton(Button.A)) {
                index = (index - 1) % positions.size
            }

            if (controller1.getPressedButton(Button.UP)) {
                if (controller1.getButton(Button.LEFT_BUMPER)) {
                    positions[index] += 0.01
                } else {
                    positions[index] += 0.1
                }
            }

            if (controller1.getPressedButton(Button.DOWN)) {
                if (controller1.getButton(Button.LEFT_BUMPER)) {
                    positions[index] -= 0.01
                } else {
                    positions[index] -= 0.1
                }
            }

            //limit position to -1 and 1
            positions[index] = Math.max(-1.0, Math.min(1.0, positions[index]))

            Robot.outtake.setJoint1(positions[0])
            Robot.outtake.setJoint2(positions[1])
            Robot.outtake.setJoint3(positions[2])
            Robot.outtake.setCarriagePosition(positions[3])


            Logger.getInstance().addItem("Current Joint", index + 1)
            Logger.getInstance().addItem("Joint 1", positions[0])
            Logger.getInstance().addItem("Joint 2", positions[1])
            Logger.getInstance().addItem("Joint 3", positions[2])
            Logger.getInstance().addItem("Carriage", positions[3])
            Robot.update()

        }

    }


}