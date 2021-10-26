package expo.subsystems

import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import expo.Subsystem

class IMU : Subsystem {
    private var angle = 0.0
    private var startAngle = 0.0
    private lateinit var imu: BNO055IMU
    override fun initialize(opMode: LinearOpMode) {
        imu = opMode.hardwareMap.get(BNO055IMU::class.java, "imu")
        val parameters = BNO055IMU.Parameters()
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"
        parameters.loggingEnabled = true
        parameters.loggingTag = "IMU"
        parameters.accelerationIntegrationAlgorithm = JustLoggingAccelerationIntegrator()
        imu.initialize(parameters)

        while (!opMode.isStopRequested && !imu.isGyroCalibrated);

        opMode.telemetry.addLine("imu inited")
        opMode.telemetry.update()
    }

    fun setStartAngle(startAngle: Double) {
        this.startAngle = startAngle
    }

    fun getAngle(): Double {
        angle = imu.angularOrientation.firstAngle + startAngle
        return angle
    }

}