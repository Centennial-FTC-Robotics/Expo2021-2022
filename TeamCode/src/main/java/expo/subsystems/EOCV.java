package expo.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import expo.Subsystem;
import expo.logger.Logger;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.jetbrains.annotations.NotNull;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

public class EOCV implements Subsystem {
	@Override
	public void update () {
	
	}
	
	enum Position {
		RIGHT,
		CENTER,
		LEFT
	}
	
	public Position pos = Position.LEFT;
	public EPOCBarcodePipeLine pipeLine;
	private OpenCvCamera camera;
	
	@Override
	public void initialize (@NotNull LinearOpMode opMode) {
		WebcamName webcamName = opMode.hardwareMap.get(WebcamName.class, "cam");
		camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName);
		

		camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
			@Override
			public void onOpened () {
				camera.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);
				Logger.getInstance().addLine("EOCV Initialized").setRetained(true);
				Logger.getInstance().update();
			}
			
			@Override
			public void onError (int errorCode) {
				Logger.getInstance().addItem("EOCV Error", errorCode).setRetained(true);
				Logger.getInstance().update();
			}
		});
		
		pipeLine = new EPOCBarcodePipeLine();
		camera.setPipeline(pipeLine);
		
		
	}
	
	public class EPOCBarcodePipeLine extends OpenCvPipeline {
		public int REGION_WIDTH = 50;
		public int REGION_HEIGHT = 50;
		public Point R1TOP_LEFT = new Point(110, 190);
		public Point R2TOP_LEFT = new Point(110, 190);
		public Point R3TOP_LEFT = new Point(110, 190);
		public Point R1A = R1TOP_LEFT;
		public Point R2A = R2TOP_LEFT;
		public Point R3A = R3TOP_LEFT;
		
		public Point R1B = new Point(R1TOP_LEFT.x + REGION_WIDTH, R1TOP_LEFT.y + REGION_HEIGHT);
		public Point R2B = new Point(R2TOP_LEFT.x + REGION_WIDTH, R2TOP_LEFT.y + REGION_HEIGHT);
		public Point R3B = new Point(R3TOP_LEFT.x + REGION_WIDTH, R3TOP_LEFT.y + REGION_HEIGHT);
		
		public Mat r1, r2, r3;
		public double avg1, avg2, avg3;
		
		public Mat YCrCb, Cb;
		
		private void inputToCb (Mat input) {
			Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
			Core.extractChannel(YCrCb, Cb, 1);
		}
		
		@Override
		public void init (Mat mat) {
			inputToCb(mat);
			r1 = Cb.submat(new Rect(R1A, R1B));
			r2 = Cb.submat(new Rect(R2A, R2B));
			r3 = Cb.submat(new Rect(R3A, R3B));
			
		}
		
		@Override
		public Mat processFrame (Mat input) {
			inputToCb(input);
			r1 = Cb.submat(new Rect(R1A, R1B));
			r2 = Cb.submat(new Rect(R2A, R2B));
			r3 = Cb.submat(new Rect(R3A, R3B));
			
			avg1 = Core.mean(r1).val[0];
			avg2 = Core.mean(r1).val[0];
			avg3 = Core.mean(r1).val[0];
			
			double highest = Math.max(avg1, Math.max(avg2, avg3));
			
			if (highest == avg1) {
				pos = Position.LEFT;
			} else if (highest == avg2) {
				pos = Position.CENTER;
			} else {
				pos = Position.RIGHT;
			}
			
			Logger.getInstance().addItem("value1", avg1);
			Logger.getInstance().addItem("value2", avg2);
			Logger.getInstance().addItem("value3", avg3);
			Logger.getInstance().addItem("pos", pos);
			Logger.getInstance().addItem("FPS", camera.getFps());
			Logger.getInstance().update();
			
			return input;
		}
	}
}
