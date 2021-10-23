package expo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode

class Robot {
    private val subsystems = emptyArray<Subsystem>()
//    val subsystems = arrayOf()  <- put subsystems in the ()

    fun initialize(opMode: LinearOpMode) {
        for (subsystem: Subsystem in subsystems)
            subsystem.initialize(opMode)
    }
}