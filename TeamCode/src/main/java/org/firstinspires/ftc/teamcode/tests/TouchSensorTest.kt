package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.util.ExpoOpMode

@TeleOp
class TouchSensorTest : ExpoOpMode() {
    @Override
    override fun runOpMode() {
        super.runOpMode()

        while (opModeIsActive()) {
            telemetry.addData("touch sensor", robot.touchSensor.getState())
            telemetry.update()


        }
    }


}