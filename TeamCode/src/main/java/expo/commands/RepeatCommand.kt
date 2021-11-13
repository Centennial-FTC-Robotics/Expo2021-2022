package expo.commands

import expo.Subsystem

class RepeatCommand(private val command: Command, private val runs: Int) : Command {
    private var run = 0

    override fun update() {
        if (command.isFinished) {
            run++
            command.done()
            if (!isFinished) {
                command.init()
                command.update()
            }
        } else {
            command.update()
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