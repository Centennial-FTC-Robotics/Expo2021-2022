package org.firstinspires.ftc.teamcode.tests

import android.util.Log
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.Robot
import expo.gamepad.Button
import expo.logger.Logger
import expo.subsystems.OpenCV
import expo.util.ExpoOpMode

@TeleOp(name = "OpenCV Tuner", group = "Calibrator")
class OpenCVTuner : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        var edits = arrayListOf("width", "height", "reg1x", "reg1y", "reg2x", "reg2y", "reg3x", "reg3y")
        var index = 0
        controller1.registerPressedButton(Button.A)
        controller1.registerPressedButton(Button.Y)
        controller1.registerPressedButton(Button.B)
        controller1.registerPressedButton(Button.UP)
        controller1.registerPressedButton(Button.DOWN)

        waitForStart()
        while (opModeIsActive()) {
            if (controller1.getPressedButton(Button.Y)) {
                index = (index + 1) % edits.size
            }
            if (controller1.getPressedButton(Button.A)) {
                index = (index - 1 + edits.size) % edits.size
            }

            if (controller1.getPressedButton(Button.UP)) {
//                changeVal(index, 1)
            }

            if (controller1.getPressedButton(Button.DOWN)) {
//                changeVal(index, -1)
            }

            Logger.getInstance().addItem("FPS", OpenCV().camera.fps)
            Logger.getInstance().update()
        }

    }

//    fun changeVal(index: Int, value: Int) {
//        if (index == 0)
//            OpenCV.BarcodePipeline.REGION_WIDTH += value
//        else if (index == 1)
//            OpenCV.BarcodePipeline.REGION_HEIGHT += value
//        else if (index == 2)
//            OpenCV.BarcodePipeline.REGION1_TOPLEFT_ANCHOR_POINT.x += value
//        else if (index == 3)
//            OpenCV.BarcodePipeline.REGION1_TOPLEFT_ANCHOR_POINT.y += value
//        else if (index == 4)
//            OpenCV.BarcodePipeline.REGION2_TOPLEFT_ANCHOR_POINT.x += value
//        else if (index == 5)
//            OpenCV.BarcodePipeline.REGION2_TOPLEFT_ANCHOR_POINT.y += value
//        else if (index == 6)
//            OpenCV.BarcodePipeline.REGION3_TOPLEFT_ANCHOR_POINT.x += value
//        else if (index == 7)
//            OpenCV.BarcodePipeline.REGION3_TOPLEFT_ANCHOR_POINT.y += value
//    }
}