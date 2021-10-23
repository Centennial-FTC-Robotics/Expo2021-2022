package expo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

interface Subsystem {
    fun initialize(opMode: LinearOpMode)
}