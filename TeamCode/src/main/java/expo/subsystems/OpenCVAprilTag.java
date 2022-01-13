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
import expo.Subsystem;
import expo.command.CommandScheduler;
import expo.logger.Logger;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

import java.util.List;

public class OpenCVAprilTag implements Subsystem {
	@Override
	public void update () {
	}
	
	public enum Position {
		RIGHT, CENTER, LEFT, UNKNOWN
	}
	
	public OpenCvCamera camera;
	public AprilTagDetectionPipeline aprilTagDetectionPipeline;
	
	
	// Lens intrinsics
	// UNITS ARE PIXELS
	double fx = 822.317;
	double fy = 822.317;
	double cx = 319.495;
	double cy = 242.502;
	
	// UNITS ARE METERS
	double tagsize = 0.166;
	
	public Position getPos () {
		return aprilTagDetectionPipeline.getPos();
	}
	
	@Override
	public void initialize (LinearOpMode opMode) {
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
		CommandScheduler.Companion.getInstance().registerSubsystem(this);
	}
	
}
