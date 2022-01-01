package expo.command

import expo.Subsystem

class RepeatCommand(private val command: Command, private val runs: Int) : Command {
    private var run = 0

    init {
        CommandScheduler.instance.schedule(command)
    }

    override fun update() {
        if (command.isFinished) {
            run++
            if (!isFinished) {
                CommandScheduler.instance.scheduleLate(command)
            }
        }
    }

    override val isCancelable: Boolean
        get() = command.isCancelable

    override val isFinished: Boolean
        get() = run >= runs

    override fun requiredSubsystems(): MutableSet<Subsystem> {
        return command.requiredSubsystems()
    }
}