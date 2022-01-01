package expo.command.commands

import expo.Robot
import expo.command.Command
import expo.subsystems.Drivetrain

class TurnCommand(private val heading: Double, private val tolerance: Double = 1.5) : Command {
    override var isFinished = false

    override fun init() {
        Drivetrain.angleController.reset()
    }

    override fun update() {
        isFinished = !Robot.drivetrain.turnAbsolute(heading, tolerance = tolerance)
    }

    override fun done() {
        Robot.drivetrain.setMotorPowers(0.0, 0.0, 0.0)
    }

}