/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package expo.subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvInternalCamera2;

import java.util.ArrayList;

import expo.Subsystem;
import expo.logger.Logger;
import expo.subsystems.AprilTagDetectionPipeline;

public class OpenCVAprilTag implements Subsystem {
	public OpenCvCamera camera;
	public AprilTagDetectionPipeline aprilTagDetectionPipeline;
	
	// Lens intrinsics
	// UNITS ARE PIXELS
	// NOTE: this calibration is for the C920 webcam at 800x448.
	// You will need to do your own calibration for other configurations!
	double fx = 578.272;
	double fy = 578.272;
	double cx = 402.145;
	double cy = 221.506;
	
	// UNITS ARE METERS
	double tagsize = 0.166;
	
	@Override
	public void initialize(LinearOpMode opMode) {
		int cameraMonitorViewId = opMode.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", opMode.hardwareMap.appContext.getPackageName());
		camera = OpenCvCameraFactory.getInstance().createWebcam(opMode.hardwareMap.get(WebcamName.class, "cam"), cameraMonitorViewId);
		aprilTagDetectionPipeline = new AprilTagDetectionPipeline(tagsize, fx, fy, cx, cy);
		
		camera.setPipeline(aprilTagDetectionPipeline);
		camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
			@Override
			public void onOpened () {
				camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
				Logger.getInstance().addLine("cam works").setRetained(true);
				Logger.getInstance().update();
			}
			
			@Override
			public void onError (int errorCode) {
				Logger.getInstance().addLine("cam doesnt work").setRetained(true);
				Logger.getInstance().update();
			}
		});
		
		FtcDashboard.getInstance().startCameraStream(camera,0);
	}
	
	@Override
	public void update () {
	
	}
}
