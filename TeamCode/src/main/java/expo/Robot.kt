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
    val odoLifter = OdometryLifter()

    private val subsystems = listOf(drivetrain, intake, spinner, IMU, openCV, odometry, odoLifter)
    fun initialize(opMode: LinearOpMode) {
        for (subsystem: Subsystem in subsystems)
            subsystem.initialize(opMode)
    }
}