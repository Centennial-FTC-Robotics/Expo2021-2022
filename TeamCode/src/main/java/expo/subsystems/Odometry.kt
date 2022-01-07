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

    private val backDir = -1
    private val midDir = -1

    //TODO: these values
    private var backRadius = 7.9
    private var middleRadius = 4.528


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

    companion object {
        val ENCODER_COUNTS_PER_INCH = 8192.0 / (2 * Math.PI * 1.0)
    }


    private lateinit var opMode: LinearOpMode
    override fun initialize(opMode: LinearOpMode) {
        back = opMode.hardwareMap.dcMotor["back odo"]
        middle = opMode.hardwareMap.dcMotor["intake"]

        this.opMode = opMode

        back.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        middle.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        back.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        middle.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        x = 0.0
        y = 0.0

    }

    fun setStartPos(x: Double, y: Double, theta: Double) {
        back.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        middle.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        back.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        middle.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        this.x = x
        this.y = y
        this.theta = theta

        oldBack = 0
        oldMiddle = 0
        oldTheta = 0.0
        angleCorrection = 0.0
    }

    override fun update() {
        currentBack = back.currentPosition * backDir
        currentMiddle = middle.currentPosition * midDir
        currentTheta = normalizeRadians(Math.toRadians(Robot.IMU.getAngle()))

        val deltaBack = ((back.currentPosition * backDir) - oldBack).toDouble()
        val deltaMiddle = ((middle.currentPosition * midDir) - oldMiddle).toDouble()
        var deltaTheta = currentTheta - oldTheta

        oldBack = back.currentPosition * backDir
        oldMiddle = middle.currentPosition * midDir
        oldTheta = currentTheta

        theta = normalizeRadians(angleCorrection + deltaTheta)

        var deltaX: Double
        var deltaY: Double
        if (deltaTheta == 0.0) {
            deltaX = deltaBack
            deltaY = deltaMiddle
        } else {
            //if theres a programming god, i implore you to let this work
            val strafeRadius = ((deltaBack) / deltaTheta) - backRadius
            val turnRadius = ((deltaMiddle) / deltaTheta)


//            deltaX = turnRadius * sin(deltaTheta) + strafeRadius * (1 - cos(deltaTheta))
//            deltaY = turnRadius * (cos(deltaTheta) - 1) + strafeRadius * sin(deltaTheta)
            deltaX = turnRadius * (cos(deltaTheta) - 1) + strafeRadius * sin(deltaTheta)
            deltaY = turnRadius * sin(deltaTheta) + strafeRadius * (1 - cos(deltaTheta))
        }

        val fieldCentric = Vector(deltaX / ENCODER_COUNTS_PER_INCH, deltaY / ENCODER_COUNTS_PER_INCH)
        fieldCentric.rotate(-theta)

        x += -fieldCentric.getX()
        y += fieldCentric.getY()
    }

    fun calcLinearOdo() {
        currentBack = back.currentPosition * backDir
        currentMiddle = middle.currentPosition * midDir
        currentTheta = normalizeRadians(Math.toRadians(Robot.IMU.getAngle()))

        val deltaBack = ((back.currentPosition * backDir) - oldBack).toDouble()
        val deltaMiddle = ((middle.currentPosition * midDir) - oldMiddle).toDouble()
        val deltaTheta = currentTheta - oldTheta

        oldBack = back.currentPosition * backDir
        oldMiddle = middle.currentPosition * midDir
        oldTheta = currentTheta

        theta = normalizeRadians(angleCorrection + deltaTheta)
        val deltaX = deltaBack * cos(theta) - deltaMiddle * sin(theta)
        val deltaY = deltaBack * sin(theta) + deltaMiddle * cos(theta)

        val fieldCentric = Vector(deltaX / ENCODER_COUNTS_PER_INCH, deltaY / ENCODER_COUNTS_PER_INCH)
//        fieldCentric.rotate(theta)

        x += fieldCentric.getX()
        y += fieldCentric.getY()

    }

    fun setAngleCorrection(angleCorrection: Double) {
        this.angleCorrection = angleCorrection
    }

    fun getEncoders(): Pair<Int, Int> {
        return Pair(middle.currentPosition, back.currentPosition)
    }

    fun getHeading() = theta

    fun getPos() = Vector(x, y)

    fun getPairPos() = Pair(x, y)


    private fun normalizeRadians(angle: Double): Double {
        var angle = angle
        while (opMode.opModeIsActive() && angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI
        }
        while (opMode.opModeIsActive() && angle < 0.0) {
            angle += 2 * Math.PI
        }
        return angle
    }
}