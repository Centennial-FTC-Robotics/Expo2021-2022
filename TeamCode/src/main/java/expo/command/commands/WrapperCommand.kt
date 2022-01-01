package expo.command.commands

import expo.Subsystem
import expo.command.Command

class WrapperCommand(private val command: Command) : Command {
    override fun init() {
        command.init()
    }

    override fun update() {
        command.update()
    }

    override fun requiredSubsystems(): MutableSet<Subsystem> {
        return command.requiredSubsystems()
    }

    override val isCancelable: Boolean = command.isCancelable

    override fun cancel() {
        command.cancel()
    }

    override fun done() {
        super.done()
    }


    override var isFinished = command.isFinished
}