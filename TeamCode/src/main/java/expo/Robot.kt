package expo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.subsystems.CarouselSpinner
import expo.subsystems.Drivetrain
import expo.subsystems.Intake
import expo.subsystems.OutreachServo

class Robot {
    val drivetrain = Drivetrain()
    val intake = Intake()
    val spinner = CarouselSpinner()
    val IMU = expo.subsystems.IMU()
    val grabber = OutreachServo()

    private val subsystems = arrayOf(drivetrain, intake, spinner, IMU, grabber)

    fun initialize(opMode: LinearOpMode) {
        for (subsystem: Subsystem in subsystems)
            subsystem.initialize(opMode)
    }
}