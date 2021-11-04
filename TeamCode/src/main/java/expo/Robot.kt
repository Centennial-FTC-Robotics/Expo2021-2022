package expo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.subsystems.CarouselSpinner
import expo.subsystems.Drivetrain
import expo.subsystems.Intake

class Robot {
    private val drivetrain = Drivetrain()
    private val intake = Intake()
    private val spinner = CarouselSpinner()
    private val IMU = expo.subsystems.IMU()

    private val subsystems = arrayOf(drivetrain, intake, spinner, IMU)

    fun initialize(opMode: LinearOpMode) {
        for (subsystem: Subsystem in subsystems)
            subsystem.initialize(opMode)
    }
}