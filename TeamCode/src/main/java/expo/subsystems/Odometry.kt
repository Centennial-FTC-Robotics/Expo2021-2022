package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import expo.Robot
import expo.Subsystem
import expo.util.Vector
import kotlin.math.cos
import kotlin.math.sin

class Odometry : Subsystem {
    private lateinit var back: DcMotor
    private lateinit var middle: DcMotor

    //TODO: these values
    private var backRadius = 0.0
    private var middleRadius = 0.0

    private var oldBack = 0
    private var oldMiddle = 0
    private var oldTheta = 0.0

    private var currentBack = 0
    private var currentMiddle = 0
    private var currentTheta = 0.0

    private var x = 0.0
    private var y = 0.0
    private var theta = 0.0

    private var angleCorrection = 0.0

    override fun initialize(opMode: LinearOpMode) {
        back = opMode.hardwareMap.dcMotor["back"]
        middle = opMode.hardwareMap.dcMotor["middle"]
    }

    fun setStartPos(x: Double, y: Double, theta: Double) {
        this.x = x
        this.y = y
        this.theta = theta
    }

    override fun update() {
        currentBack = back.currentPosition
        currentMiddle = middle.currentPosition
        currentTheta = Robot.IMU.getAngle()

        val deltaBack = (currentBack - oldBack).toDouble()
        val deltaMiddle = (currentMiddle - oldMiddle).toDouble()
        val deltaTheta = currentTheta - oldTheta

        theta += deltaTheta + angleCorrection

        val deltaX: Double
        val deltaY: Double

        oldBack = currentBack
        oldMiddle = currentMiddle
        oldTheta = currentTheta

        if (deltaTheta == 0.0) {
            deltaX = deltaBack
            deltaY = deltaMiddle
        } else {
            val turnRadius = deltaBack / deltaTheta - backRadius
            val strafeRadius = deltaMiddle / deltaTheta - middleRadius

            deltaX = turnRadius * sin(deltaTheta) + strafeRadius * (1 - cos(deltaTheta))
            deltaY = turnRadius * (cos(deltaTheta) - 1) + strafeRadius * sin(deltaTheta)
        }

        val fieldCentric = Vector(deltaX, deltaY)
        fieldCentric.rotate(theta)

        x += fieldCentric.getX()
        y += fieldCentric.getY()
    }

    fun setAngleCorrection(angleCorrection: Double) {
        this.angleCorrection = angleCorrection
    }

    fun getHeading() = theta

    fun getPos() = Vector(x, y)
}