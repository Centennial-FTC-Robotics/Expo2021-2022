package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import expo.PIDController;
import expo.Robot;
import expo.command.CommandScheduler;
import expo.command.commands.MoveToPositionCommand;
import expo.logger.Item;
import expo.logger.Logger;
import expo.gamepad.Button;
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
	private static final double DISTANCE = 24 * 2;
	@Override
	public void runOpMode () {
		PIDControllers controllers = PIDControllers.X;
		PIDController controller = controllers.getController();
		
		int pidIndex = 0;
		boolean atStart = true;
		
		MoveToPositionCommand command = null;
		
		super.runOpMode();
		
		Robot.INSTANCE.getOdometry().setStartPos(0, 0, 0);
		Robot.INSTANCE.getIMU().setStartAngle(0);
		controller1.registerPressedButton(Button.X);
		controller1.registerPressedButton(Button.A);
		controller1.registerPressedButton(Button.B);
		controller1.registerPressedButton(Button.UP);
		controller1.registerPressedButton(Button.DOWN);
		controller1.registerPressedButton(Button.START);
		
		while (opModeIsActive()) {
			Logger.getInstance().addItem(new Item("Current Controller", controllers.name()));
			Logger.getInstance().addItem(new Item("Current Coefficient", VALUES[pidIndex]));
			Logger.getInstance().addItem(new Item("kP", controller.getPID().getFirst()));
			Logger.getInstance().addItem(new Item("kI", controller.getPID().getSecond()));
			Logger.getInstance().addItem(new Item("kD", controller.getPID().getThird()));
			Logger.getInstance().addItem(new Item("Current Position", Robot.INSTANCE.getOdometry().getPos()));
			Logger.getInstance().addItem(new Item("Heading", Math.toDegrees(Robot.INSTANCE.getOdometry().getHeading())));
			
			Robot.INSTANCE.update();
			
			if(controller1.getPressedButton(Button.START) && command == null && atStart) {
				Robot.INSTANCE.getOdometry().setStartPos(0, 0, 0);
				Robot.INSTANCE.getIMU().setStartAngle(0);
			}
			
			if (controller1.getPressedButton(Button.UP)) {
				double amount = 0.001;
				if (controller1.getButton(Button.LEFT_BUMPER))
					amount = .0001;
				controller.increase(pidIndex, amount);
			}
			if (controller1.getPressedButton(Button.DOWN)) {
 				double amount = 0.001;
				if (controller1.getButton(Button.LEFT_BUMPER))
					amount = .0001;
				controller.increase(pidIndex, -amount);
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
			
			if (command != null && command.isFinished()) {
				command = null;
			}
			
			if (command != null && controller1.getButton(Button.BACK)) {
				CommandScheduler.Companion.getInstance().forceInterrupt(command);
				command = null;
			}
			
			if (controller1.getPressedButton(Button.A) && command == null) {
				if (atStart) {
					if (controller1.getButton(Button.Y)) {
						command = new MoveToPositionCommand(new Vector(DISTANCE, DISTANCE), 90, PIDControllers.X.controller, PIDControllers.Y.controller, PIDControllers.ANGLE.controller);
					} else if (controllers == PIDControllers.X) {
						command = new MoveToPositionCommand(new Vector(DISTANCE, 0), 0, PIDControllers.X.controller, PIDControllers.Y.controller, PIDControllers.ANGLE.controller);
					} else if (controllers == PIDControllers.Y) {
						command = new MoveToPositionCommand(new Vector(0, DISTANCE), 0, PIDControllers.X.controller, PIDControllers.Y.controller, PIDControllers.ANGLE.controller);
					} else {
						command = new MoveToPositionCommand(new Vector(0, 0), 90, PIDControllers.X.controller, PIDControllers.Y.controller, PIDControllers.ANGLE.controller);
					}
				} else {
					command = new MoveToPositionCommand(new Vector(0, 0), 0, PIDControllers.X.controller, PIDControllers.Y.controller, PIDControllers.ANGLE.controller);
				}
				command.schedule();
				atStart = !atStart;
			}
			
		}
	}
	
	private enum PIDControllers {
		X(new PIDController(.0186, 2.0E-4, .003)),
		Y(new PIDController(.0186, 2.0E-4, .003)),
		ANGLE(new PIDController(.002222, 0.001, 0));
		
		private PIDController controller;
		
		PIDControllers (PIDController controller) {
			this.controller = controller;
		}
		
		public PIDController getController () {
			return controller;
		}
	}
	
}
