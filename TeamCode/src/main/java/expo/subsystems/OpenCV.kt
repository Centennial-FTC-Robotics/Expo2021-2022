package expo.subsystems


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.Subsystem
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvPipeline
import kotlin.math.max


//openCV class that uses the OpenCV library to perform image processing
class OpenCV : Subsystem {
    public enum class Position {
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
        val webcamName = opMode.hardwareMap.get(WebcamName::class.java, "cam")
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraViewID)
    }

    class BarcodePipeline : OpenCvPipeline() {
        companion object {
            const val REGION_WIDTH = 50
            const val REGION_HEIGHT = 50
        }

        val BLUE = Scalar(0.0, 0.0, 255.0)
        val GREEN = Scalar(0.0, 255.0, 0.0)
        var REGION1_TOPLEFT_ANCHOR_POINT: Point = Point(110.0, 190.0)
        val FOUR_RING_THRESHOLD = 130
        val ONE_RING_THRESHOLD = 129
        //copy paste from here
        var region1_pointA: Point = Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x,
            REGION1_TOPLEFT_ANCHOR_POINT.y
        )
        var region1_pointB: Point = Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
        )
        var region1_Cb: Mat? = null
        //ends there ^^^

        var region2_pointA: Point = Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x,
            REGION1_TOPLEFT_ANCHOR_POINT.y
        )
        var region2_pointB: Point = Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
        )
        var region2_Cb: Mat? = null
        var region3_pointA: Point = Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x,
            REGION1_TOPLEFT_ANCHOR_POINT.y
        )
        var region3_pointB: Point = Point(
            REGION1_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            REGION1_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT
        )
        var region3_Cb: Mat? = null

        var YCrCb = Mat()
        var Cb = Mat()
        var avg1 = 0
        var avg2 = 0;
        var avg3=0;

        fun inputToCb(input: Mat?) {
            Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb)
            Core.extractChannel(YCrCb, Cb, 1)
        }

        override fun init(firstFrame: Mat) {
            inputToCb(firstFrame)
            //copy paste this 2 times
            region1_Cb = Cb.submat(Rect(region1_pointA, region1_pointB))
            region2_Cb = Cb.submat(Rect(region1_pointA, region1_pointB))
            region3_Cb = Cb.submat(Rect(region1_pointA, region1_pointB))
        }

        override fun processFrame(input: Mat): Mat {
            inputToCb(input)

            //loop thru this for each region (3 total)
            region1_Cb = Cb.submat(Rect(region1_pointA, region1_pointB))
            avg1 = Core.mean(region1_Cb).`val`[0].toInt()
            Imgproc.rectangle(
                input,  // Buffer to draw on
                region1_pointA,  // First point which defines the rectangle
                region1_pointB,  // Second point which defines the rectangle
                BLUE,  // The color the rectangle is drawn in
                2
            )
            region2_Cb = Cb.submat(Rect(region2_pointA, region2_pointB))
            avg2 = Core.mean(region2_Cb).`val`[0].toInt()
            Imgproc.rectangle(
                input,  // Buffer to draw on
                region2_pointA,  // First point which defines the rectangle
                region2_pointB,  // Second point which defines the rectangle
                BLUE,  // The color the rectangle is drawn in
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

            if(avg1 > avg2 && avg1 > avg3){
                OpenCV().pos = Position.LEFT
            }
            if(avg2 > avg1 && avg2 > avg3){
                OpenCV().pos = Position.CENTER
            }
            if(avg3 > avg2 && avg3 > avg1){
                OpenCV().pos = Position.RIGHT
            }

            //if avg1 is highest then set OpenCV().pos = Position.LEFT
            //if 2 is highest then OpenCV().pos = CENTER
            //if 3 then RIGHT

//
            if (OpenCV().opMode.opModeIsActive()) {
                //copy paste the line below 2 times
                OpenCV().opMode.telemetry.addData("value1", avg1)
                OpenCV().opMode.telemetry.addData("pos", OpenCV().pos)
                OpenCV().opMode.telemetry.update()
            }
// Negative thickness means solid fill
            //            Mat testMat = new Mat();
//            Imgproc.cvtColor(input,testMat,Imgproc.COLOR_RGB2YCrCb);
//
//            Core.extractChannel(testMat,input,1);
            return input
        }


    }

}