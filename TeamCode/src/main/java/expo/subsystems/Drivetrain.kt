package expo.subsystems

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import expo.PIDController
import expo.Robot
import expo.Subsystem
import expo.commands.Command
import expo.util.Vector
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

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

    private var currentPos = Vector(0.0, 0.0)
    private var robotCentric = Vector(0.0, 0.0)
    private var power: Vector = Vector(0.0, 0.0)
    private var anglePower = 0.0

    override fun initialize(opMode: LinearOpMode) {
        this.opMode = opMode
        frontLeft = opMode.hardwareMap.dcMotor.get("frontLeft")
        frontRight = opMode.hardwareMap.dcMotor.get("frontRight")
        backLeft = opMode.hardwareMap.dcMotor.get("backLeft")
        backRight = opMode.hardwareMap.dcMotor.get("backRight")

        backRight.direction = DcMotorSimple.Direction.REVERSE
        frontLeft.direction = DcMotorSimple.Direction.REVERSE

        frontLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        frontRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        backLeft.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        backRight.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        motors = arrayOf(frontLeft, frontRight, backLeft, backRight)

    }

    //in the future this will be done by the command manager but for now its fine
    fun updatePos() {
        if (odoLoopCount % IMU_ANGLE_SYNC_RATE == 0) {
            val angle: Double = Robot.IMU.getAngle()
            Robot.odometry.setAngleCorrection(Math.toRadians(angle))
        }
        Robot.odometry.update()
        odoLoopCount++
    }

    fun setPowers(frontLeft: Double, frontRight: Double, backLeft: Double, backRight: Double) {
        this.frontLeft.power = frontLeft
        this.frontRight.power = frontRight
        this.backLeft.power = backLeft
        this.backRight.power = backRight
    }

    fun setPowers(
        frontLeft: Double,
        frontRight: Double,
        backLeft: Double,
        backRight: Double,
        factor: Double
    ) {
        this.frontLeft.power = frontLeft * factor
        this.frontRight.power = frontRight * factor
        this.backLeft.power = backLeft * factor
        this.backRight.power = backRight * factor
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
        while (opMode.opModeIsActive() && abs(error) > tolerance) {
            update()
            currentAngle = Math.toDegrees(Robot.odometry.getHeading())
            error = getAngleDist(currentAngle, targetAngle)
            direction = getAngleDir(currentAngle, targetAngle)
            turnRate = turnController.update(error)
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
        angleDifference = if (abs(angleDifference) > 180) {
            360 - abs(angleDifference)
        } else {
            abs(angleDifference)
        }
        return angleDifference
    }


    private fun getAngleDir(targetAngle: Double, currentAngle: Double): Int {
        val angleDifference = targetAngle - currentAngle
        var angleDir = (angleDifference / abs(angleDifference)).toInt()
        if (abs(angleDifference) > 180) {
            angleDir *= -1
        }
        return angleDir
    }


    fun clip(`val`: Double, max: Double, min: Double): Double {
        val sign: Int = if (`val` < 0) -1 else 1
        return if (abs(`val`) < min) {
            min * sign
        } else if (abs(`val`) > max) {
            max * sign
        } else {
            `val`
        }
    }

    fun resetEncoders() {
        for (motor in motors) {
            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
            motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        }
    }

    fun loopFollowPath(targets: Array<Vector>, headings: DoubleArray) {
        for (i in targets.indices) {
            loopMoveToPos(targets[i], headings[i])
        }
    }


    fun loopMoveToPos(
        target: Vector, heading: Double,
        x: PIDController = xController,
        y: PIDController = yController,
        angle: PIDController = angleController
    ) {
        while (opMode.opModeIsActive() && moveToPosition(target, heading, x, y, angle));
    }

    fun moveToPosition(
        targetPos: Vector, heading: Double,
        x: PIDController = xController,
        y: PIDController = yController,
        angle: PIDController = angleController
    ): Boolean {
        return moveToPosition(targetPos, heading, .5, .05, .3, 1.0, 1.5, x, y, angle)
    }

    fun getMoveToPositionCommand(targetPos: Vector, heading: Double): Command {
        return this.MoveToPositionCommand(targetPos, heading)
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
        headingTolerance: Double,
        x: PIDController = xController,
        y: PIDController = yController,
        angle: PIDController = angleController
    ): Boolean {
        currentPos = Robot.odometry.getPos()
        opMode.telemetry.addData("target", targetPos)
        opMode.telemetry.addData("current", currentPos)
        getMotorPowers(targetPos, heading, x, y, angle)
        var diag1: Double = power.getX()
        var diag2: Double = power.getY()

        val headingDiff = getAngleDist(heading, Math.toDegrees(Robot.odometry.getHeading()))
        val maxValue: Double = diag1.coerceAtLeast(diag2)
        if (maxValue > maxSpeed) {
            diag1 = diag1 / maxValue * maxSpeed
            diag2 = diag2 / maxValue * maxSpeed
        }
        diag1 = clip(diag1, maxSpeed, minSpeed)
        diag2 = clip(diag2, maxSpeed, minSpeed)
        opMode.telemetry.addData("raw angle power", anglePower)
        anglePower = clip(anglePower, maxAngleSpeed, 0.1)
        if (abs(headingDiff) <= headingTolerance) {
            anglePower = 0.0
        }
        opMode.telemetry.addData("mod angle power", anglePower)
        anglePower *= -getAngleDir(
            heading,
            Math.toDegrees(Robot.odometry.getHeading())
        ).toDouble()
        setMotorPowers(diag1, diag2, anglePower)
        val error: Vector = Vector.sub(targetPos, currentPos)
        val xDiff: Double = error.getX()
        val yDiff: Double = error.getY()
//        headingDiff = 0;
//        xDiff = 0;
//        opMode.telemetry.addData("diag1", diag1);
//        opMode.telemetry.addData("diag2", diag2);

        opMode.telemetry.addData("xdiff", xDiff)
        opMode.telemetry.addData("ydiff", yDiff)

        opMode.telemetry.addData("angle diff", headingDiff)
        opMode.telemetry.update()
        if (abs(xDiff) < tolerance && abs(yDiff) < tolerance && abs(headingDiff) < headingTolerance || !opMode.opModeIsActive()) {
            setMotorPowers(0.0, 0.0, 0.0)
            xController.reset()
            yController.reset()
            angleController.reset()
            return false
        }
        return true
    }

    fun getMotorPowers(
        targetPosition: Vector,
        targetAngle: Double,
        x: PIDController = xController,
        y: PIDController = yController,
        angle: PIDController = angleController
    ) {
        val error: Vector = Vector.sub(targetPosition, currentPos)
        opMode.telemetry.addData("field centric error", error)
        val heading: Double = Robot.odometry.getHeading()
        error.rotate(heading)
        opMode.telemetry.addData("robot centric error", error)
        opMode.telemetry.addData("current angle", Math.toDegrees(Robot.odometry.getHeading()))
        opMode.telemetry.addData("target angle", targetAngle)
        robotCentric = Vector(
            -x.update(error.getX()),
            y.update(error.getY())
        )
        opMode.telemetry.addData("robot centric powers", robotCentric)
        anglePower = angle.update(
            getAngleDist(
                targetAngle,
                Math.toDegrees(Robot.odometry.getHeading())
            )
        )
        val leftx: Double = robotCentric.getX()
        val lefty: Double = robotCentric.getY()
        val scalar: Double = abs(lefty - leftx)
            .coerceAtLeast(abs(lefty + leftx)) //scalar and magnitude scale the motor powers based on distance from joystick origin
        val magnitude: Double = sqrt(lefty.pow(2.0) + leftx.pow(2.0))
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
    fun findMotorPowers(leftX: Double, leftY: Double, rightX: Double): DoubleArray {
        val magnitude: Double = sqrt(leftX * leftX + leftY * leftY + rightX * rightX)
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
        var upleft: Double = diag1 + rotate
        var downleft: Double = diag2 + rotate
        var upright: Double = diag2 - rotate
        var downright: Double = diag1 - rotate
        val max: Double =
            abs(upleft.coerceAtLeast(upright).coerceAtLeast(downleft).coerceAtLeast(downright))
        if (max > 1) {
            upleft /= max
            upright /= max
            downleft /= max
            downright /= max
        }
        frontLeft.power = upleft
        frontRight.power = upright
        backLeft.power = downleft
        backRight.power = downright
    }

    inner class MoveToPositionCommand(private var targetPos: Vector, private var heading: Double) : Command {
        override var isFinished: Boolean = false

        override fun update() {
            isFinished = Drivetrain().moveToPosition(targetPos, heading)
        }
    }
}