package expo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.subsystems.*

object Robot {
    val drivetrain = Drivetrain()
    val intake = Intake()
    val spinner = CarouselSpinner()
    val IMU = IMU()
    val openCV = OpenCV()
    val outtake = Outtake()
    val motorTester = MotorTester()

    private val subsystems = listOf(drivetrain, intake, spinner, IMU, openCV, outtake)

    fun initialize(opMode: LinearOpMode) {
        for (subsystem: Subsystem in subsystems)
            subsystem.initialize(opMode)
    }
}