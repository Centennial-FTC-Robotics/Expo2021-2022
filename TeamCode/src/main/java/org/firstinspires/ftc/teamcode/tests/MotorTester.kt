package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import expo.util.Button
import expo.util.ExpoOpMode

@TeleOp(name = "Motor Tester", group = "Test")
class MotorTester : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        var power = 0.0
        waitForStart()

        controller1.registerPressedButton(Button.UP)
        controller1.registerPressedButton(Button.DOWN)
        while (opModeIsActive()) {
            updateGamepads()
            if (controller1.getPressedButton(Button.UP))
                power += .2

            if (controller1.getPressedButton(Button.DOWN))
                power -= .2

            if (controller1.getButton(Button.A))
                robot.motorTester.setPower(power)
            else
                robot.motorTester.setPower(0.0)

            telemetry.addData("Power", power)
            telemetry.update()
        }
    }
}