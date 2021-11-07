package expo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.subsystems.CarouselSpinner
import expo.subsystems.Drivetrain
import expo.subsystems.Intake
import expo.subsystems.MotorTester

class Robot {
    val drivetrain = Drivetrain()
    val intake = Intake()
    val spinner = CarouselSpinner()
    val IMU = expo.subsystems.IMU()
    val motorTester = MotorTester()

    private val subsystems = emptyList<Subsystem>()

    fun initialize(opMode: LinearOpMode) {
        for (subsystem: Subsystem in subsystems)
            subsystem.initialize(opMode)
    }
}