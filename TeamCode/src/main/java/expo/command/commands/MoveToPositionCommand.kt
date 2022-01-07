package expo.command.commands

import expo.PIDController
import expo.Robot
import expo.Subsystem
import expo.subsystems.Drivetrain
import expo.command.Command
import expo.util.Vector

//        return moveToPosition(targetPos, heading, .5, .05, .3, 1.0, 1.5, x, y, angle)
open class MoveToPositionCommand @JvmOverloads constructor(
    var targetPos: Vector,
    val heading: Double,
    val maxSpeed: Double = .85,
    val minSpeed: Double = .05,
    val maxAngleSpeed: Double = .2,
    val tolerance: Double = 1.5,
    val headingTolerance: Double = 5.0,
    val x: PIDController = Drivetrain.xController,
    val y: PIDController = Drivetrain.yController,
    val angle: PIDController = Drivetrain.angleController
) : Command {
    constructor(
        targetPos: Vector,
        heading: Double,
        controller: PIDController,
        controller1: PIDController,
        controller2: PIDController
    ) : this(targetPos, heading, x = controller, y = controller1, angle = controller2) {

    }

    override var isFinished: Boolean = false

    override fun update() {
        isFinished = !Robot.drivetrain.moveToPosition(
            targetPos,
            heading,
            maxSpeed,
            minSpeed,
            maxAngleSpeed,
            tolerance,
            headingTolerance,
            x,
            y,
            angle
        )

    }

    override fun cancel() {
        done()
    }

    override fun done() {
        Robot.drivetrain.setPowers(0.0, 0.0, 0.0, 0.0)
    }

    override fun requiredSubsystems(): MutableSet<Subsystem> {
        return mutableSetOf(Robot.drivetrain)
    }

    override val isCancelable: Boolean
        get() = false
}