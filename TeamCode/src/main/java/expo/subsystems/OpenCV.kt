package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.Subsystem
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName
import org.opencv.core.Mat
import org.openftc.easyopencv.OpenCvCamera
import org.openftc.easyopencv.OpenCvCameraFactory
import org.openftc.easyopencv.OpenCvPipeline

//openCV class that uses the OpenCV library to perform image processing
class OpenCV : Subsystem {

    lateinit var camera: OpenCvCamera

    override fun initialize(opMode: LinearOpMode) {
        val cameraViewID = opMode.hardwareMap.appContext.resources.getIdentifier(
            "cameraMonitorViewId",
            "id",
            opMode.hardwareMap.appContext.packageName
        )
        val webcamName = opMode.hardwareMap.get(WebcamName::class.java, "Webcam 1")
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraViewID)
    }

    class BarcodePipeline : OpenCvPipeline() {
        override fun processFrame(input: Mat?): Mat {
            TODO("Not yet implemented")
        }
    }

}