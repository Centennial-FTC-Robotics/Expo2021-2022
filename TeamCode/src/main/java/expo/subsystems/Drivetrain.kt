package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import expo.PIDController
import expo.Robot
import expo.Subsystem
import expo.util.Vector
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs

class Drivetrain : Subsystem {
    private lateinit var frontLeft: DcMotor
    private lateinit var frontRight: DcMotor
    private lateinit var backLeft: DcMotor
    private lateinit var backRight: DcMotor
    private var odoLoopCount = 0
    private val IMU_ANGLE_SYNC_RATE: Int = 3
    private lateinit var opMode: LinearOpMode
    private lateinit var motors: Array<DcMotor>

    private val ticksPerRotation = 537.6
    private val wheelDiameter = 3.937
    private val ticksPerWheelRotation = ticksPerRotation
    private val distanceInWheelRotation: Double = wheelDiameter * Math.PI
    private val ticksPerInch = distanceInWheelRotation / ticksPerWheelRotation

    private val xController: PIDController = PIDController(.12, .01, 0.0)
    private val yController: PIDController = PIDController(.12, .007, 0.01)
    private val angleController: PIDController = PIDController(.06, 0.008, 0.12)

    private val turnController: PIDController = PIDController(1.0 / 150, 0.006, 0.001)

    private var currentPos: Vector? = null
    private var robotCentric: Vector? = null
    private var power: Vector? = null
    private var anglePower = 0.0

    private var item: Telemetry.Item? = null

    override fun initialize(opMode: LinearOpMode) {
        this.opMode = opMode
        frontLeft = opMode.hardwareMap.dcMotor.get("frontLeft")
        frontRight = opMode.hardwareMap.dcMotor.get("frontRight")
        backLeft = opMode.hardwareMap.dcMotor.get("backLeft")
        backRight = opMode.hardwareMap.dcMotor.get("backRight")

        backRight.setDirection(DcMotorSimple.Direction.REVERSE)
        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE)

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE)

        motors = arrayOf(frontLeft, frontRight, backLeft, backRight)

    }

    //in the future this will be done by the command manager but for now its fine
    fun updatePos() {
        if (odoLoopCount % IMU_ANGLE_SYNC_RATE == 0) {
            val angle: Double = Robot.IMU.getAngle()
            Robot.odometry.setAngleCorrection(Math.toRadians(angle))
        }
        Robot.odometry.calcLinearOdo()
        odoLoopCount++
    }

    fun updateArcPos() {
        if (odoLoopCount % IMU_ANGLE_SYNC_RATE == 0) {
            val angle: Double = Robot.IMU.getAngle()
            Robot.odometry.setAngleCorrection(Math.toRadians(angle))
        }
        Robot.odometry.update()
        odoLoopCount++
    }

    fun setPowers(frontLeft: Double, frontRight: Double, backLeft: Double, backRight: Double) {
        this.frontLeft.setPower(frontLeft)
        this.frontRight.setPower(frontRight)
        this.backLeft.setPower(backLeft)
        this.backRight.setPower(backRight)
    }

    fun setPowers(
        frontLeft: Double,
        frontRight: Double,
        backLeft: Double,
        backRight: Double,
        factor: Double
    ) {
        this.frontLeft.setPower(frontLeft * factor)
        this.frontRight.setPower(frontRight * factor)
        this.backLeft.setPower(backLeft * factor)
        this.backRight.setPower(backRight * factor)
    }


    private fun convertInchToEncoder(inches: Double): Int {
        return (inches / ticksPerInch).toInt()
    }

    private fun convertEncoderToInch(encoder: Int): Double {
        return ticksPerInch / encoder
    }

    fun turnRelative(targetAngle: Double) {
        turnAbsolute(AngleUnit.normalizeDegrees(targetAngle + Math.toDegrees(Robot.odometry.getHeading())))
    }

    fun turnAbsolute(targetAngle: Double) {
        var currentAngle: Double
        var direction: Int
        var turnRate: Double
        val minSpeed = 0.1
        val maxSpeed = 0.5
        val tolerance = .4
        var error = Double.MAX_VALUE
        turnController.reset()
        while (opMode.opModeIsActive() && Math.abs(error) > tolerance) {
            update()
            currentAngle = Math.toDegrees(Robot.odometry.getHeading())
            error = getAngleDist(currentAngle, targetAngle)
            direction = getAngleDir(currentAngle, targetAngle)
            turnRate = turnController.update(error.toDouble())
            turnRate = clip(turnRate, maxSpeed, minSpeed)
            opMode.telemetry.addData("error", error)
            opMode.telemetry.addData("turnRate", turnRate)
            opMode.telemetry.addData("current", currentAngle)
            opMode.telemetry.addData("dir", direction)
            opMode.telemetry.update()
            setMotorPowers(0.0, 0.0, turnRate * direction)
        }
        setMotorPowers(0.0, 0.0, 0.0)
    }

    private fun getAngleDist(targetAngle: Double, currentAngle: Double): Double {
        var angleDifference = currentAngle - targetAngle
        if (Math.abs(angleDifference) > 180) {
            angleDifference = 360 - Math.abs(angleDifference)
        } else {
            angleDifference = Math.abs(angleDifference)
        }
        return angleDifference
    }


    private fun getAngleDir(targetAngle: Double, currentAngle: Double): Int {
        val angleDifference = targetAngle - currentAngle
        var angleDir = (angleDifference / Math.abs(angleDifference)) as Int
        if (Math.abs(angleDifference) > 180) {
            angleDir *= -1
        }
        return angleDir
    }


    fun clip(`val`: Double, max: Double, min: Double): Double {
        val sign: Int
        sign = if (`val` < 0) -1 else 1
        return if (abs(`val`) < min) min * sign else if (Math.abs(`val`) > max) max * sign else `val`
    }

    fun resetEncoders() {
        for (motor in motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER)
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER)
        }
    }

    fun loopFollowPath(targets: Array<Vector>, headings: DoubleArray) {
        for (i in targets.indices) {
            loopMoveToPos(targets[i], headings[i])
        }
    }

    fun loopMoveToPos(target: Vector, heading: Double) {
        while (opMode.opModeIsActive() && moveToPosition(target, heading));
    }

    fun moveToPosition(targetPos: Vector, heading: Double): Boolean {
        return moveToPosition(targetPos, heading, .5, .05, .3, 1.0, 1.5)
    }

    fun moveToPosition(
        targetPos: Vector,
        heading: Double,
        maxSpeed: Double,
        minSpeed: Double,
        tolerance: Double,
        headingTolerance: Double
    ): Boolean {
        return moveToPosition(
            targetPos,
            heading,
            maxSpeed,
            minSpeed,
            .2,
            tolerance,
            headingTolerance
        )
    }

    fun moveToPosition(
        targetPos: Vector,
        heading: Double,
        maxSpeed: Double,
        minSpeed: Double,
        maxAngleSpeed: Double,
        tolerance: Double,
        headingTolerance: Double
    ): Boolean {
        update()
        currentPos = Robot.odometry.getPos()
        opMode.telemetry.addData("target", targetPos)
        opMode.telemetry.addData("current", currentPos)
        val ydiff: Double = Math.abs(targetPos.getY() - currentPos!!.getY())
        val xdiff: Double = Math.abs(targetPos.getX() - currentPos!!.getX())
        getMotorPowers(targetPos, heading)
        var diag1: Double = power!!.getX()
        var diag2: Double = power!!.getY()
        val headingDiff = getAngleDist(heading, Math.toDegrees(Robot.odometry.getHeading()))
        val maxValue: Double = Math.max(diag1, diag2)
        if (maxValue > maxSpeed) {
            diag1 = diag1 / maxValue * maxSpeed
            diag2 = diag2 / maxValue * maxSpeed
        }
        diag1 = clip(diag1, maxSpeed, minSpeed)
        diag2 = clip(diag2, maxSpeed, minSpeed)
        opMode.telemetry.addData("raw angle power", anglePower)
        anglePower = clip(anglePower, maxAngleSpeed, 0.1)
        if (Math.abs(headingDiff) <= headingTolerance) {
            anglePower = 0.0
        }
        opMode.telemetry.addData("mod angle power", anglePower)
        anglePower *= -getAngleDir(
            heading,
            Math.toDegrees(Robot.odometry.getHeading())
        ).toDouble()
        setMotorPowers(diag1, diag2, anglePower)
        val error: Vector = Vector.sub(targetPos, currentPos!!)
        val xDiff: Double = error.getX()
        val yDiff: Double = error.getY()
        //        headingDiff = 0;
//        xDiff = 0;
//        opMode.telemetry.addData("diag1", diag1);
//        opMode.telemetry.addData("diag2", diag2);
        opMode.telemetry.addData("xdiff", xDiff)
        item = if (item == null) {
            opMode.telemetry.addData("ydiff", yDiff)
        } else {
            item!!.setRetained(false)
            opMode.telemetry.addData("ydiff", yDiff)
        }
        item!!.setRetained(true)
        opMode.telemetry.addData("angle diff", headingDiff)
        opMode.telemetry.update()
        if (Math.abs(xDiff) < tolerance && Math.abs(yDiff) < tolerance && Math.abs(headingDiff) < headingTolerance || !opMode.opModeIsActive()) {
            setMotorPowers(0.0, 0.0, 0.0)
            xController.reset()
            yController.reset()
            angleController.reset()
            return false
        }
        return true
    }

    fun getMotorPowers(targetPosition: Vector?, targetAngle: Double) {
        val error: Vector = Vector.sub(targetPosition!!, currentPos!!)
        opMode.telemetry.addData("field centric error", error)
        val heading: Double = Robot.odometry.getHeading()
        error.rotate(heading)
        opMode.telemetry.addData("robot centric error", error)
        opMode.telemetry.addData("current angle", Math.toDegrees(Robot.odometry.getHeading()))
        opMode.telemetry.addData("target angle", targetAngle)
        robotCentric = Vector(
            -xController.update(error.getX()),
            yController.update(error.getY())
        )
        opMode.telemetry.addData("robot centric powers", robotCentric)
        anglePower = angleController.update(
            getAngleDist(
                targetAngle,
                Math.toDegrees(Robot.odometry.getHeading())
            )
        )
        val leftx: Double = robotCentric!!.getX()
        val lefty: Double = robotCentric!!.getY()
        val scalar: Double = Math.max(
            Math.abs(lefty - leftx),
            Math.abs(lefty + leftx)
        ) //scalar and magnitude scale the motor powers based on distance from joystick origin
        val magnitude: Double = Math.sqrt(Math.pow(lefty, 2.0) + Math.pow(leftx, 2.0))
        power = Vector((lefty + leftx) * magnitude / scalar, (lefty - leftx) * magnitude / scalar)
        //
        opMode.telemetry.addData("x power", leftx)
        opMode.telemetry.addData("y power", lefty)
    }


    //0 - leftup, 1 - rightup, 2 - rightdown, 3 - leftdown
    //double frontLeft,
    // double frontRight,
    // double backLeft,
    // double backRight
    fun findMotorPowers(leftX: Double, leftY: Double, rightX: Double): DoubleArray? {
        val magnitude: Double = Math.sqrt(leftX * leftX + leftY * leftY + rightX * rightX)
        val powers = DoubleArray(4)
        if (magnitude > 1) {
            powers[0] = (leftX + leftY - rightX) / magnitude
            powers[1] = (-leftX + leftY + rightX) / magnitude
            powers[2] = (-leftX + leftY - rightX) / magnitude
            powers[3] = (leftX + leftY + rightX) / magnitude
        } else {
            powers[0] = leftX + leftY - rightX
            powers[1] = -leftX + leftY + rightX
            powers[2] = -leftX + leftY - rightX
            powers[3] = leftX + leftY + rightX
        }
        return powers
    }

    fun setMotorPowers(diag1: Double, diag2: Double, rotate: Double) {
        var upleft: Double
        var upright: Double
        var downleft: Double
        var downright: Double
        upleft = diag1 + rotate
        downleft = diag2 + rotate
        upright = diag2 - rotate
        downright = diag1 - rotate
        val max: Double =
            Math.abs(Math.max(Math.max(Math.max(upleft, upright), downleft), downright))
        if (max > 1) {
            upleft /= max
            upright /= max
            downleft /= max
            downright /= max
        }
        frontLeft.setPower(upleft)
        frontRight.setPower(upright)
        backLeft.setPower(downleft)
        backRight.setPower(downright)
    }
}