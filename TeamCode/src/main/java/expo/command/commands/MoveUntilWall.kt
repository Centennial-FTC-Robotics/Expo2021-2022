package expo.command.commands

import expo.Robot
import expo.command.Command
import expo.logger.Logger
import expo.util.ExpoOpMode

open class MoveUntilWall : Command {
    override var isFinished: Boolean = false
    var modifier = 1.0

    init {
        modifier = if (Robot.opMode.team == ExpoOpMode.Team.BLUE) 1.0 else -1.0
        Logger.getInstance().addItem("MoveUntilWall modifier", modifier).isRetained = true
    }

    override fun update() {
        Robot.drivetrain.setMotorPowers(-.5 * modifier, .9 * modifier, 0.0)
        isFinished = Robot.wallSensors.getBlue() || Robot.wallSensors.getRed()
    }

    override fun done() {
        Robot.drivetrain.setPowers(0.0, 0.0, 0.0, 0.0)
    }

}