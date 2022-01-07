package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.Robot
import expo.util.ExpoOpMode

@TeleOp(name = "Arc Odometry Tester", group = "Tests")
class ArcOdometryTest : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        robot.IMU.setStartAngle(0.0)
        robot.odometry.setStartPos(0.0, 0.0, 0.0)
        var pos: Pair<Double, Double>
        var encoders: Pair<Int, Int>
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

            val controlVector = controller1.getControllerVector()
            telemetry.addData("Left Stick X", controlVector.getX())
            telemetry.addData("Left Stick Y", controlVector.getY())
            robot.drivetrain.findAndSetMotorPowers(
                controlVector.getX(),
                controlVector.getY(),
                controller1.getRightX(),
                .3
            )
            telemetry.update()
        }
    }
}