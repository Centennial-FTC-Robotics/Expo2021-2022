package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import expo.Robot;
import expo.util.ExpoOpMode;

@TeleOp(name = "EOCV test")
public class EOCVPipelineTester extends ExpoOpMode {
	@Override
	public void runOpMode () {
		super.runOpMode();
		
		while (opModeIsActive()) {
			Robot.INSTANCE.update();
		
		}
	}
}
