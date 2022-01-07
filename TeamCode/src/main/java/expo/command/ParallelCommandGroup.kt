package expo.command

import expo.Subsystem
import java.util.HashSet

/**
 * A [CommandGroup] that runs in parallel, meaning every command will run at the same time.
 * It will only count as finished when all commands are finished.
 * The counterpart to this is [SequentialCommandGroup], which runs in sequence.
 */
class   ParallelCommandGroup(vararg commands: Command) : CommandGroup {
    private val commands: MutableSet<Command> = HashSet()
    private val requirements: MutableSet<Subsystem> = HashSet()
    override var isCancelable = true
        private set
    private var running = false


    override fun addCommands(vararg commands: Command) {
        check(!running) { "Cannot add commands to a running ParallelCommandGroup" }
        for (command in commands) {
            this.commands.add(command)
            requirements.addAll(command.requiredSubsystems())
            if (!command.isCancelable) {
                isCancelable = false
            }

        }
    }

    override fun init() {
        for (command in commands) {
            command.init()
        }
    }

    override fun update() {
        if (!running) {
            running = true
        }
        val finishedCommands: MutableSet<Command> = HashSet()
        for (command in commands) {
            if (command.isFinished) {
                finishedCommands.add(command)
                command.done()
            } else {
                command.update()
            }
        }
        commands.removeAll(finishedCommands)
    }

    override val isFinished: Boolean
        get() = commands.isEmpty()

    override fun cancel() {
        for (command in commands) {
            command.cancel()
        }
        commands.clear()
    }

    override fun requiredSubsystems(): MutableSet<Subsystem> {
        return requirements
    }

    init {
        addCommands(*commands)
    }
}