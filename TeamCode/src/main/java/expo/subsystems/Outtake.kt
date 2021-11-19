package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Servo
import expo.Subsystem

/* joint1/3:
    REST, LEFT, RIGHT, ALLIANCE(aka STRAIGHT)
   joint2:
    REST, LEFT, RIGHT, ALLIANCE3, ALLIANCE2, ALLIANCE1
 * MOVE JOINT 2 FIRST OR ELSE BAD THINGS MIGHT HAPPEN IM NOT SURE THO BUT JOHN SAID SO HES ALSO A LOSER
 * carriage:
    - 2 positions (IN, OUT)
 * slides:
    - ill think abt this later when i actually have a brain
 */

class Outtake : Subsystem {
    private lateinit var linkage: Servo

    override fun initialize(opMode: LinearOpMode) {
        linkage = opMode.hardwareMap.get(Servo::class.java, "linkage")
        linkage.scaleRange(0.0, 0.8)
        linkage.direction = Servo.Direction.REVERSE
    }

    fun setLinkagePosition(position: Double) {
        linkage.position = position
    }

}