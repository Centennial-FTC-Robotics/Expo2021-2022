package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.Robot
import expo.command.commands.OuttakeCommand
import expo.gamepad.Button
import expo.logger.Logger
import expo.subsystems.Outtake
import expo.util.ExpoOpMode

@TeleOp(name = "Outtake Tester", group = "Tester")
class OuttakeTester : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        controller1.registerPressedButton(Button.UP)
        controller1.registerPressedButton(Button.DOWN)
        controller1.registerPressedButton(Button.A)

        var command: OuttakeCommand? = null

        val positions = Outtake.OuttakePosition.values()
        var index = 0

        while (opModeIsActive()) {
            if (command != null && command.isFinished) {
                command = null
            }
            if (command != null) {
                Logger.getInstance().addItem("Currently", "Moving")
            }

            if (command == null) {
                Logger.getInstance().addItem("Currently", "Not Moving")
                if (controller1.getPressedButton(Button.A)) {
                    command = OuttakeCommand(positions[index])
                    command.schedule()

                }
                if (controller1.getPressedButton(Button.UP)) {
                    index = (index + 1) % positions.size
                }
                if (controller1.getPressedButton(Button.DOWN)) {
                    index = (index - 1) % positions.size
                }
            }

            Logger.getInstance().addItem("Current Position", positions[index].name)
            Robot.update()
        }
    }
}