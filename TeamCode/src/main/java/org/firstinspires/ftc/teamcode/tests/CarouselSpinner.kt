package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.Robot
import expo.command.commands.CarouselCommand
import expo.gamepad.Button
import expo.util.ExpoOpMode

@TeleOp(name = "Carousel Tester", group = "Tester")
class CarouselSpinner: ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        controller1.registerPressedButton(Button.A)

        while (opModeIsActive()) {
            if(controller1.getPressedButton(Button.A)) {
                CarouselCommand().schedule()
            }

            Robot.update()
        }
    }
}