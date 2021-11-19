package expo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.subsystems.*

object Robot {
    val drivetrain = Drivetrain()
    val intake = Intake()
    val spinner = CarouselSpinner()
    val IMU = IMU()
    val openCV = OpenCV()
    val touchSensor = TouchSensor()
    val odoLifter = OdometryLifter()
    val odometry = Odometry()
    val outtake = Outtake()

    private val subsystems = listOf(intake, drivetrain, touchSensor, odoLifter, odometry, outtake, IMU)

    fun initialize(opMode: LinearOpMode) {
        for (subsystem: Subsystem in subsystems)
            subsystem.initialize(opMode)
    }
}