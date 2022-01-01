package expo.command.commands

import com.qualcomm.robotcore.util.ElapsedTime
import expo.command.Command

class WaitCommand(private val time: Double) : Command {
    private val timer: ElapsedTime = ElapsedTime()
    override var isFinished: Boolean = false

    override fun init() {
        timer.reset()
    }

    override fun update() {
        isFinished = timer.milliseconds() >= time
    }
}