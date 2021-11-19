package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.util.ExpoOpMode
import expo.util.Vector

@TeleOp(name = "Drivetrain Test", group = "Tests")
class DrivetrainTest : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        var controlVector: Vector
        while (opModeIsActive()) {
            updateGamepads()
            controlVector = controller1.getControllerVector()
            val powers =
                robot.drivetrain.findMotorPowers(
                    controller1.getLeftX(),
                    controller1.getLeftY(),
                    controller1.getRightX()
                )
            robot.drivetrain.setPowers(powers!![0], powers[1], powers[2], powers[3], .5)
            controller1.printControlVector(this)
            telemetry.update()
        }
    }
}