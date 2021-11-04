package expo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.subsystems.CarouselSpinner
import expo.subsystems.Drivetrain
import expo.subsystems.Intake

class Robot {
    companion object {
        @JvmField
        val drivetrain = Drivetrain()

        @JvmField
        val intake = Intake()

        @JvmField
        val spinner = CarouselSpinner()

        @JvmField
        val IMU = expo.subsystems.IMU()

        @JvmField
        val outreachServo = expo.subsystems.OutreachServo()

        private val subsystems = arrayOf(drivetrain, intake, spinner, IMU, outreachServo)
    }

    fun initialize(opMode: LinearOpMode) {
        for (subsystem: Subsystem in subsystems)
            subsystem.initialize(opMode)
    }
}