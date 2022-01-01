import expo.commands.CommandScheduler

<<<<<<< HEAD
package expo.commands
=======
package expo.command
>>>>>>> 89340ee... -tried to fix repeat command

import expo.Subsystem

class RepeatCommand(private val command: Command, private val runs: Int) : Command {
    private var run = 0

<<<<<<< HEAD
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
=======
    init {
        CommandScheduler.instance.schedule(command)
    }

    override fun update() {
        if (command.isFinished) {
            run++
            if (!isFinished) {
                CommandScheduler.instance.scheduleLate(command)
            }
>>>>>>> 89340ee... -tried to fix repeat command
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