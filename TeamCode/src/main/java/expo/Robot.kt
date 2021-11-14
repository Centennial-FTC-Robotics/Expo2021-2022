package expo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.subsystems.*

object Robot {
    val drivetrain = Drivetrain()
    val intake = Intake()
    val spinner = CarouselSpinner()
    val IMU = IMU()
    val openCV = OpenCV()
    val motorTester = MotorTester()
    val odometry = Odometry()

    private val subsystems = listOf(drivetrain, intake, spinner, IMU, openCV, odometry)
    fun initialize(opMode: LinearOpMode) {
        for (subsystem: Subsystem in subsystems)
            subsystem.initialize(opMode)
    }
}