package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import expo.Robot
import expo.logger.Logger
import expo.util.ExpoOpMode
import org.openftc.apriltag.AprilTagDetection


@TeleOp(name = "April Tag Tester", group = "Tests")
class AprilTagTest: ExpoOpMode() {
    val FEET_PER_METER = 3.28084

    var numFramesWithoutDetection = 0

    val DECIMATION_HIGH = 3f
    val DECIMATION_LOW = 2f
    val THRESHOLD_HIGH_DECIMATION_RANGE_METERS = 1.0f
    val THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION = 4

    override fun runOpMode() {
        super.runOpMode()
        while (opModeIsActive()) {
            // Calling getDetectionsUpdate() will only return an object if there was a new frame
            // processed since the last time we called it. Otherwise, it will return null. This
            // enables us to only run logic when there has been a new frame, as opposed to the
            // getLatestDetections() method which will always return an object.
            val detections: ArrayList<AprilTagDetection>? =
                Robot.openCVAprilTag.aprilTagDetectionPipeline.getDetectionsUpdate()

            // If there's been a new frame...
            if (detections != null) {
                Logger.getInstance().addItem("FPS", Robot.openCVAprilTag.camera.getFps())
                Logger.getInstance().addItem("Overhead ms", Robot.openCVAprilTag.camera.getOverheadTimeMs())
                Logger.getInstance().addItem("Pipeline ms", Robot.openCVAprilTag.camera.getPipelineTimeMs())
                Logger.getInstance().addItem("Position", Robot.openCVAprilTag.pos, -1)

                // If we don't see any tags
                if (detections.size == 0) {
                    numFramesWithoutDetection++

                    // If we haven't seen a tag for a few frames, lower the decimation
                    // so we can hopefully pick one up if we're e.g. far back
                    if (numFramesWithoutDetection >= THRESHOLD_NUM_FRAMES_NO_DETECTION_BEFORE_LOW_DECIMATION) {
                        Robot.openCVAprilTag.aprilTagDetectionPipeline.setDecimation(DECIMATION_LOW)
                    }
                } else {
                    numFramesWithoutDetection = 0

                    // If the target is within 1 meter, turn on high decimation to
                    // increase the frame rate
                    if (detections[0].pose.z < THRESHOLD_HIGH_DECIMATION_RANGE_METERS) {
                        Robot.openCVAprilTag.aprilTagDetectionPipeline.setDecimation(DECIMATION_HIGH)
                    }
                    for (detection in detections) {
                        Logger.getInstance().addLine(String.format("\nDetected tag ID=%d", detection.id))
                        Logger.getInstance().addLine(
                            java.lang.String.format(
                                "Translation X: %.2f feet",
                                detection.pose.x * FEET_PER_METER
                            )
                        )
                        Logger.getInstance().addLine(
                            java.lang.String.format(
                                "Translation Y: %.2f feet",
                                detection.pose.y * FEET_PER_METER
                            )
                        )
                        Logger.getInstance().addLine(
                            java.lang.String.format(
                                "Translation Z: %.2f feet",
                                detection.pose.z * FEET_PER_METER
                            )
                        )
                        Logger.getInstance().addLine(
                            String.format(
                                "Rotation Yaw: %.2f degrees",
                                Math.toDegrees(detection.pose.yaw)
                            )
                        )
                        Logger.getInstance().addLine(
                            String.format(
                                "Rotation Pitch: %.2f degrees",
                                Math.toDegrees(detection.pose.pitch)
                            )
                        )
                        Logger.getInstance().addLine(
                            String.format(
                                "Rotation Roll: %.2f degrees",
                                Math.toDegrees(detection.pose.roll)
                            )
                        )
                    }
                }
                Logger.getInstance().update()
            }
        }
    }
}