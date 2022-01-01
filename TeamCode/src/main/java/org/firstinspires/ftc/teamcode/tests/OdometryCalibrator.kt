package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import expo.Robot
import expo.subsystems.Odometry
import expo.gamepad.Button
import expo.util.ExpoOpMode

@TeleOp(name = "Odometry Calibrator", group = "Calibrator")
class OdometryCalibrator : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        Robot.IMU.setStartAngle(0.0)
        Robot.odometry.setStartPos(0.0, 0.0, 0.0)
        Robot.update()
        var power = .4
        controller1.registerPressedButton(Button.UP)
        controller1.registerPressedButton(Button.DOWN)
        controller1.registerPressedButton(Button.A)
        while (opModeIsActive()) {
            if (controller1.getPressedButton(Button.UP)) {
                power += .05
                telemetry.addData("Power", power)
                telemetry.update()
            }
            if (controller1.getPressedButton(Button.DOWN)) {
                power -= .05
                telemetry.addData("Power", power)
                telemetry.update()
            }

            if (controller1.getPressedButton(Button.A)) {
                Robot.update()
                val oldAngle = Robot.odometry.getHeading()
                val oldMid = Robot.odometry.getEncoders().first
                val oldBack = Robot.odometry.getEncoders().second
                robot.drivetrain.setMotorPowers(0.0, 0.0, power)
                val time = ElapsedTime()
                while (time.milliseconds() < 2000 && opModeIsActive());
                robot.drivetrain.setMotorPowers(0.0, 0.0, 0.0)
                Robot.update()
                val angle = Robot.odometry.getHeading() - oldAngle
                val mid = (Robot.odometry.getEncoders().first - oldMid) / Odometry.ENCODER_COUNTS_PER_INCH
                val back = (Robot.odometry.getEncoders().second - oldBack) / Odometry.ENCODER_COUNTS_PER_INCH

                telemetry.addData("angle", Math.toDegrees(angle))
                telemetry.addData("mid", mid)
                telemetry.addData("back", back)
                telemetry.addLine()
                telemetry.addData("mid distance", mid / angle)
                telemetry.addData("back distance", back / angle)
                telemetry.update()
            }
        }
    }

    class SimpleTurnCommand(val power: Double)

}