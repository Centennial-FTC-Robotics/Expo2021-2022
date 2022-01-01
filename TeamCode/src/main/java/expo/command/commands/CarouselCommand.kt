package expo.command.commands

import com.qualcomm.robotcore.util.ElapsedTime
import expo.Robot
import expo.Subsystem
import expo.command.Command
import expo.util.ExpoOpMode

class CarouselCommand(val team: ExpoOpMode.Team = ExpoOpMode.Team.BLUE) : Command {
    private var timer = ElapsedTime()
    override var isFinished: Boolean = false

    override fun init() {
        timer.reset()
        if (team == ExpoOpMode.Team.BLUE)
            Robot.spinner.setPower(.5)
        else
            Robot.spinner.setPower(-.5)
    }

    override fun update() {
        if (timer.milliseconds() > 750) {
            Robot.spinner.setPower(0.0)
            isFinished = true
        }
    }

    override fun requiredSubsystems(): MutableSet<Subsystem> {
        return mutableSetOf(Robot.spinner)
    }
}