package expo.hardware;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

//bruh.
//enums are constants
public class Joint {
	double rest, left, right, alliance1, alliance2, alliance3;
	Servo joint;
	
	public Joint(double rest, double left, double right, double alliance1, double alliance2, double alliance3, LinearOpMode opMode, String name){
		joint = opMode.hardwareMap.get(Servo.class, name);
		this.rest = rest;
		this.left = left;
		this.right = right;
		this.alliance1 = alliance1;
		this.alliance2 = alliance2;
		this.alliance3 = alliance3;
	}
	
	public Joint(double rest, double left, double right, double alliance1, LinearOpMode opMode, String name){
		joint = opMode.hardwareMap.get(Servo.class, name);
		this.rest = rest;
		this.left = left;
		this.right = right;
		this.alliance1 = alliance1;
		this.alliance2 = alliance1;
		this.alliance3 = alliance1;
		//poggers
	}
	
	public void setPosition(Position pos){
		if(pos == Position.LEFT){
			joint.setPosition(left);
		}
		else if(pos == Position.RIGHT){
			joint.setPosition(right);
		}
		else if(pos == Position.REST){
			joint.setPosition(rest);
		}
		else if(pos == Position.ALLIANCE1){
			joint.setPosition(alliance1);
		}
		else if(pos == Position.ALLIANCE2){
			joint.setPosition(alliance2);
		}
		else{
			joint.setPosition(alliance3);
		}
		//poggers
	}
	
	
	public enum Position{
		REST, LEFT, RIGHT, ALLIANCE1, ALLIANCE2, ALLIANCE3;
	}
}
