package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.Robot
import expo.command.SequentialCommandGroup
import expo.command.commands.IntakeCommand
import expo.command.commands.OuttakeCommand
import expo.gamepad.Button
import expo.logger.Logger
import expo.subsystems.Outtake
import expo.util.ExpoOpMode

@TeleOp(name="Intake & Outtake Tester", group = "Tester")
class IntakeOuttakeTester : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        controller1.registerPressedButton(Button.UP)
        controller1.registerPressedButton(Button.DOWN)
        controller1.registerPressedButton(Button.A)
        controller1.registerPressedButton(Button.B)
        controller1.registerPressedButton(Button.Y)

        val positions = Outtake.OuttakePosition.values()
        var index = 0

        while (opModeIsActive()) {
            if (controller1.getPressedButton(Button.UP)) {
                index = (index + 1) % positions.size
            }
            if (controller1.getPressedButton(Button.DOWN)) {
                index = (index - 1) % positions.size
            }

            if(controller1.getPressedButton(Button.A)) {
                OuttakeCommand(positions[index]).schedule()
            }

            if(controller1.getPressedButton(Button.B)) {
                IntakeCommand().schedule()
            }

            if(controller1.getPressedButton(Button.Y)) {
                SequentialCommandGroup(
                    IntakeCommand(),
                    OuttakeCommand(positions[index])
                ).schedule()
            }

            Logger.getInstance().addItem("Outtake Position", positions[index])
            Robot.update()
        }
    }
}