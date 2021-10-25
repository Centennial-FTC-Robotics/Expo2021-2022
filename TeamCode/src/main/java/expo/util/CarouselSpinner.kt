package expo.util

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor

class CarouselSpinner {
    private lateinit var carousel: DcMotor
    fun initialize(opMode: LinearOpMode) {
        carousel = opMode.hardwareMap.dcMotor.get("carouselMotor")

    }

    public fun setPower(power: Double) {
        carousel.power = power
    }
}