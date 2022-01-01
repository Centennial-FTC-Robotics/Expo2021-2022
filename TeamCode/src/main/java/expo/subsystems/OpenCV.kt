package expo.subsystems


import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.Subsystem
import expo.gamepad.Button
import expo.logger.Logger
import expo.subsystems.OpenCVVals.*
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCamera.AsyncCameraOpenListener
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvCameraRotation
import org.openftc.easyopencv.OpenCvPipeline


//openCV class that uses the OpenCV library to perform image processing
class OpenCV : Subsystem {
    enum class Position {
        RIGHT, CENTER, LEFT
    }

    var pos = Position.LEFT

    lateinit var camera: OpenCvCamera
    lateinit var opMode: LinearOpMode

    override fun initialize(opMode: LinearOpMode) {
        this.opMode = opMode
        val cameraViewID = opMode.hardwareMap.appContext.resources.getIdentifier(
            "cameraMonitorViewId",
            "id",
            opMode.hardwareMap.appContext.packageName
        )
//        val viewportContainerIds = OpenCvCameraFactory.getInstance()
//            .splitLayoutForMultipleViewports(cameraViewID, 2, OpenCvCameraFactory.ViewportSplitMethod.VERTICALLY)

        val webcamName = opMode.hardwareMap.get(WebcamName::class.java, "cam")
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraViewID)
        camera.setPipeline(BarcodePipeline())

        camera.openCameraDeviceAsync(object : AsyncCameraOpenListener {
            override fun onOpened() {
                camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT)
                Logger.getInstance().addLine("OpenCV Initialized").isRetained = true
                Logger.getInstance().update()

            }

            override fun onError(errorCode: Int) {
                Logger.getInstance().addItem("OpenCV camera error", errorCode).isRetained = true
                Logger.getInstance().update()
            }
        })

        FtcDashboard.getInstance().startCameraStream(camera, 0.0)

    }

    class BarcodePipeline : OpenCvPipeline() {
        companion object {
            var REGION1_TOPLEFT_ANCHOR_POINT: Point = Point(R1_X, Y)
            var REGION2_TOPLEFT_ANCHOR_POINT: Point = Point(R2_X, Y)
            var REGION3_TOPLEFT_ANCHOR_POINT: Point = Point(R3_X, Y)

            var region1_pointA: Point = Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x,
                REGION1_TOPLEFT_ANCHOR_POINT.y
            )
            var region1_pointB: Point = Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
            )
            var region1_Cb: Mat? = null

            var region2_pointA: Point = Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x,
                REGION2_TOPLEFT_ANCHOR_POINT.y
            )
            var region2_pointB: Point = Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION2_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
            )
            var region2_Cb: Mat? = null
            var region3_pointA: Point = Point(
                REGION3_TOPLEFT_ANCHOR_POINT.x,
                REGION3_TOPLEFT_ANCHOR_POINT.y
            )
            var region3_pointB: Point = Point(
                REGION3_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION3_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
            )
            var region3_Cb: Mat? = null
        }


        val RED = Scalar(255.0, 0.0, 0.0)
        val GREEN = Scalar(0.0, 255.0, 0.0)
        val BLUE = Scalar(0.0, 0.0, 255.0)


        //copy paste from here

        var YCrCb = Mat()
        var Cb = Mat()
        var avg1 = 0
        var avg2 = 0;
        var avg3 = 0;

        fun inputToCb(input: Mat?) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb)
            Core.extractChannel(YCrCb, Cb, 1)
        }

        override fun init(firstFrame: Mat) {
            setupRegions()
            inputToCb(firstFrame)
            //copy paste this 2 times
            region1_Cb = Cb.submat(Rect(region1_pointA, region1_pointB))
            region2_Cb = Cb.submat(Rect(region2_pointA, region2_pointB))
            region3_Cb = Cb.submat(Rect(region3_pointA, region3_pointB))
        }

        override fun processFrame(input: Mat): Mat {
            setupRegions()
            inputToCb(input)

            //loop thru this for each region (3 total)
            region1_Cb = Cb.submat(Rect(region1_pointA, region1_pointB))
            avg1 = Core.mean(region1_Cb).`val`[0].toInt()
            Imgproc.rectangle(
                input,  // Buffer to draw on
                region1_pointA,  // First point which defines the rectangle
                region1_pointB,  // Second point which defines the rectangle
                RED,  // The color the rectangle is drawn in
                2
            )
            region2_Cb = Cb.submat(Rect(region2_pointA, region2_pointB))
            avg2 = Core.mean(region2_Cb).`val`[0].toInt()
            Imgproc.rectangle(
                input,  // Buffer to draw on
                region2_pointA,  // First point which defines the rectangle
                region2_pointB,  // Second point which defines the rectangle
                GREEN,  // The color the rectangle is drawn in
                2
            )
            region3_Cb = Cb.submat(Rect(region3_pointA, region3_pointB))
            avg3 = Core.mean(region3_Cb).`val`[0].toInt()
            Imgproc.rectangle(
                input,  // Buffer to draw on
                region3_pointA,  // First point which defines the rectangle
                region3_pointB,  // Second point which defines the rectangle
                BLUE,  // The color the rectangle is drawn in
                2
            )

            if (avg1 > avg2 && avg1 > avg3) {
                OpenCV().pos = Position.LEFT
            }
            if (avg2 > avg1 && avg2 > avg3) {
                OpenCV().pos = Position.CENTER
            }
            if (avg3 > avg2 && avg3 > avg1) {
                OpenCV().pos = Position.RIGHT
            }


//            copy paste the line below 2 times
            Logger.getInstance().addItem("value1", avg1)
            Logger.getInstance().addItem("value2", avg2)
            Logger.getInstance().addItem("value3", avg3)
            Logger.getInstance().addItem("pos", OpenCV().pos)
//            Logger.getInstance().addItem("FPS", OpenCV().camera.fps)
            Logger.getInstance().update()

            return input
        }

        fun setupRegions() {
            REGION1_TOPLEFT_ANCHOR_POINT = Point(R1_X, Y)
            REGION2_TOPLEFT_ANCHOR_POINT = Point(R1_X, Y)
            REGION3_TOPLEFT_ANCHOR_POINT = Point(R1_X, Y)

            region1_pointA = Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x,
                REGION1_TOPLEFT_ANCHOR_POINT.y
            )
            region1_pointB = Point(
                REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
            )
            region1_Cb = null

            region2_pointA = Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x,
                REGION2_TOPLEFT_ANCHOR_POINT.y
            )
            region2_pointB = Point(
                REGION2_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION2_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
            )
            region2_Cb = null
            region3_pointA = Point(
                REGION3_TOPLEFT_ANCHOR_POINT.x,
                REGION3_TOPLEFT_ANCHOR_POINT.y
            )
            region3_pointB = Point(
                REGION3_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
                REGION3_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
            )
            region3_Cb = null

        }
    }

}