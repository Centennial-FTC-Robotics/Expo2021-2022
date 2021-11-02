package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import expo.Subsystem

class CarouselSpinner : Subsystem {
    private lateinit var carousel: DcMotor
    override fun initialize(opMode: LinearOpMode) {
        carousel = opMode.hardwareMap.dcMotor.get("carouselMotor")

    }

    fun setPower(power: Double) {
        carousel.power = power
    }
}