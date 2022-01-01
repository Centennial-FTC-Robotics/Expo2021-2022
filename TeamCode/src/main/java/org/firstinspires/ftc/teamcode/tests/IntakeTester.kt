package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.Robot
import expo.command.commands.IntakeCommand
import expo.gamepad.Button
import expo.logger.Item
import expo.logger.Logger
import expo.util.ExpoOpMode

@TeleOp(name = "Intake Tester", group = "Tester")
class IntakeTester : ExpoOpMode() {
    override fun runOpMode() {
        var power = 0.0
        var pos = 0.0
        super.runOpMode()
        controller1.registerPressedButton(Button.Y)
        controller1.registerPressedButton(Button.A)
        controller1.registerPressedButton(Button.B)
        controller1.registerPressedButton(Button.UP);
        controller1.registerPressedButton(Button.DOWN);


        var intakeCommand: IntakeCommand? = null
        while (opModeIsActive()) {
            if (intakeCommand == null) {
                if (controller1.getPressedButton(Button.Y)) {
                    power += 0.1
                }
                if (controller1.getPressedButton(Button.A)) {
                    power -= 0.1
                }

                if (controller1.getPressedButton(Button.UP)) {
                    pos += 0.1
                }

                if (controller1.getPressedButton(Button.DOWN)) {
                    pos -= 0.1
                }

                if (controller1.getPressedButton(Button.B)) {
                    power = 0.0
                    pos = 0.0
                    intakeCommand = IntakeCommand()
                    intakeCommand.schedule()
                }

                //limit power to -1 and 1
                //and limit pos to 0 and 1
                power = Math.min(Math.max(power, -1.0), 1.0)
                pos = Math.min(Math.max(pos, 0.0), 1.0)

                Robot.intake.setJointPosition(pos)
                Robot.intake.setPower(power)
                Logger.getInstance().addItem(Item("Power", power))
                Logger.getInstance().addItem(Item("Position", pos))
            } else {
                if (intakeCommand.isFinished) {
                    intakeCommand = null
                }
            }
            Robot.update()
        }
    }
}