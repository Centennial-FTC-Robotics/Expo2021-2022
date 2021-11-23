package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import expo.PIDController;
import expo.Robot;
import expo.util.Button;
import expo.util.ExpoOpMode;
import expo.util.Vector;

/**
 * This opmode is used to calibrate the PIDControllers used for drivetrain movement.
 * The B button is used to switch what PIDController is being calibrated (X, Y, or angle).
 * The X button is used to switch which constant is being changed (P, I, or D).
 * The DPAD UP/DOWN buttons are used to increase/decrease the current value of the constant.
 * The A button is used to test the PIDControllers by driving the robot.
 * Pressing the Y button at the same time as the A button (press Y first) will test all PIDControllers at the same time (move diagonally and turn 90 degrees).
 * Pressing the back button during motion will cause the robot to stop moving.
 */
@TeleOp(name = "PID Controller Calibrator", group = "Calibrator")
public class PIDControllerCalibrator extends ExpoOpMode {
	private static final char[] VALUES = {'P', 'I', 'D'};
	
	@Override
	public void runOpMode () {
		PIDControllers controllers = PIDControllers.X;
		PIDController controller = controllers.getController();
		
		int pidIndex = 0;
		boolean atStart = true;
		
		super.runOpMode();
		controller1.registerPressedButton(Button.X);
		controller1.registerPressedButton(Button.A);
		controller1.registerPressedButton(Button.B);
		controller1.registerPressedButton(Button.UP);
		controller1.registerPressedButton(Button.DOWN);
		
		while (opModeIsActive()) {
			if (controller1.getPressedButton(Button.UP)) {
				controller.increase(pidIndex, 0.01);
			}
			if (controller1.getPressedButton(Button.DOWN)) {
				controller.increase(pidIndex, -0.01);
			}
			
			if (controller1.getPressedButton(Button.X)) {
				pidIndex = (pidIndex + 1) % VALUES.length;
			}
			
			if (controller1.getPressedButton(Button.B)) {
				if (controllers == PIDControllers.X) {
					controllers = PIDControllers.Y;
				} else if (controllers == PIDControllers.Y) {
					controllers = PIDControllers.ANGLE;
				} else {
					controllers = PIDControllers.X;
				}
				controller = controllers.getController();
			}
			
			if (controller1.getPressedButton(Button.A)) {
				if (atStart) {
					if (controller1.getButton(Button.Y)) {
						while (opModeIsActive()
								  && !controller1.getButton(Button.BACK)
								  && Robot.INSTANCE.getDrivetrain().moveToPosition(new Vector(48, 48), Math.toDegrees(Robot.INSTANCE.getOdometry().getHeading() + 90), PIDControllers.X.controller, PIDControllers.Y.controller, PIDControllers.ANGLE.controller))
							;
					} else if (controllers == PIDControllers.X) {
						while (opModeIsActive()
								  && !controller1.getButton(Button.BACK)
								  && Robot.INSTANCE.getDrivetrain().moveToPosition(new Vector(48, 0), 0, PIDControllers.X.controller, PIDControllers.Y.controller, PIDControllers.ANGLE.controller))
							;
					} else if (controllers == PIDControllers.Y) {
						while (opModeIsActive()
								  && !controller1.getButton(Button.BACK)
								  && Robot.INSTANCE.getDrivetrain().moveToPosition(new Vector(0, 48), 0, PIDControllers.X.controller, PIDControllers.Y.controller, PIDControllers.ANGLE.controller))
							;
					} else {
						while (opModeIsActive()
								  && !controller1.getButton(Button.BACK)
								  && Robot.INSTANCE.getDrivetrain().moveToPosition(new Vector(0, 0), Math.toDegrees(Robot.INSTANCE.getOdometry().getHeading() + 180), PIDControllers.X.controller, PIDControllers.Y.controller, PIDControllers.ANGLE.controller))
							;
					}
				} else {
					while (opModeIsActive()
							  && !controller1.getButton(Button.BACK)
							  && Robot.INSTANCE.getDrivetrain().moveToPosition(new Vector(0, 0), 0, PIDControllers.X.controller, PIDControllers.Y.controller, PIDControllers.ANGLE.controller))
						;
				}
				Robot.INSTANCE.getDrivetrain().setPowers(0, 0, 0, 0);
				atStart = !atStart;
			}
		}
		
		telemetry.addData("Current Controller", controllers.name());
		telemetry.addData("Current Coefficient", VALUES[pidIndex]);
		telemetry.addLine();
		telemetry.addData("kP", controller.getPID().getFirst());
		telemetry.addData("kI", controller.getPID().getSecond());
		telemetry.addData("kD", controller.getPID().getThird());
		telemetry.update();
	}
	
	private enum PIDControllers {
		X(new PIDController(0.001, 0.0, 0.0)),
		Y(new PIDController(0.001, 0.0, 0.0)),
		ANGLE(new PIDController(0.001, 0.0, 0.0));
		
		private PIDController controller;
		
		PIDControllers (PIDController controller) {
			this.controller = controller;
		}
		
		public PIDController getController () {
			return controller;
		}
	}
	
}
