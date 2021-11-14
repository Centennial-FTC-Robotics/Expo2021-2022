package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import expo.Subsystem

class MotorTester : Subsystem {
    private lateinit var motor: DcMotor
    private var motorName = "motor"
    override fun initialize(opMode: LinearOpMode) {
        motor = opMode.hardwareMap.dcMotor.get(motorName)
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun setMotorName(name: String) {
        motorName = name
    }

    fun setPower(power: Double) {
        motor.power = power
    }


}