package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.util.ExpoOpMode
import expo.util.Vector

@TeleOp(name = "Arc Odometry Tester", group = "Tests")
class ArcOdometryTest : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        var pos: Pair<Double, Double>
        var encoders: Pair<Int, Int>
        var control: Vector
        while (opModeIsActive()) {
            updateGamepads()
            robot.drivetrain.updatePos()
            pos = robot.odometry.getPairPos()
            encoders = robot.odometry.getEncoders()
            telemetry.addData("X", pos.first)
            telemetry.addData("Y", pos.second)
            telemetry.addData("Heading", Math.toDegrees(robot.odometry.getHeading()))
            telemetry.addData("IMU Heading", robot.IMU.getAngle())
            telemetry.addData("Middle Encoder", encoders.first)
            telemetry.addData("Back Encoder", encoders.second)

            val powers =
                robot.drivetrain.findMotorPowers(
                    controller1.getLeftX(),
                    controller1.getLeftY(),
                    controller1.getRightX()
                )
            robot.drivetrain.setPowers(powers!![0], powers[1], powers[2], powers[3], .4)

            telemetry.update()
        }
    }
}