package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.ServoImplEx
import expo.util.Button
import expo.util.ExpoOpMode
import org.firstinspires.ftc.teamcode.R

@TeleOp(name = "Intake Test", group = "Tests")
class IntakeTest : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()

        controller1.registerPressedButton(Button.UP)
        controller1.registerPressedButton(Button.DOWN)
        controller1.registerPressedButton(Button.Y)
        controller1.registerPressedButton(Button.A)
        controller1.registerPressedButton(Button.X)
        controller1.registerPressedButton(Button.LEFT_BUMPER)
        controller1.registerToggle(Button.A)
        controller1.registerToggle(Button.B)
        var intakePos = 0.0
        var intakePower = 0.0
        while (opModeIsActive()) {
            updateGamepads()
            if (controller1.getPressedButton(Button.UP)) {
                intakePos += 0.1
                if (controller1.getButton(Button.RIGHT_BUMPER))
                    intakePos -= .05
            }
            if (controller1.getPressedButton(Button.DOWN)) {
                intakePos -= 0.1
                if (controller1.getButton(Button.RIGHT_BUMPER))
                    intakePos += .05
            }

            if (controller1.getPressedButton(Button.Y)) {
                intakePower += 0.1
                if (controller1.getButton(Button.RIGHT_BUMPER))
                    intakePos -= .05
            }
            if (controller1.getPressedButton(Button.A)) {
                intakePower -= 0.1
                if (controller1.getButton(Button.RIGHT_BUMPER))
                    intakePower += .05
            }

            if (controller1.getPressedButton(Button.LEFT_BUMPER)) {
                intakePos = if (intakePos == 1.0)
                    0.0
                else
                    1.0
            }


            if (intakePos > 1.0)
                intakePos = 1.0
            if (intakePos < 0)
                intakePos = 0.0

            if (intakePower > 1.0)
                intakePower = 1.0
            if (intakePower < -1.0)
                intakePower = -1.0

            if (controller1.getToggle(Button.B))
                robot.intake.setIntakePower(intakePower)
            else
                robot.intake.setIntakePower(0.0)

            if (controller1.getPressedButton(Button.X)) {
                if (robot.intake.intakeServo is ServoImplEx) {
                    if ((robot.intake.intakeServo as ServoImplEx).isPwmEnabled)
                        (robot.intake.intakeServo as ServoImplEx).setPwmDisable()
                    else
                        (robot.intake.intakeServo as ServoImplEx).setPwmEnable()
                }

            }
            if ((robot.intake.intakeServo as ServoImplEx).isPwmEnabled)
                robot.intake.setIntakePosition(intakePos)
            telemetry.addData("Intake Pos", intakePos)
            telemetry.addData("Motor Power", intakePower)
            telemetry.update()
        }
    }
}