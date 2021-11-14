package expo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

interface Subsystem {
    fun initialize(opMode: LinearOpMode)
    fun update() {
        //Do nothing by default, but can be overridden to tell the command scheduler to run a command for this subsystem
        //This is useful for subsystems that need to run a command every loop, such as updating odometry position
    }
}
