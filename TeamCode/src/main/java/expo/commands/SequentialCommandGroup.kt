package expo.commands

import expo.Subsystem
import java.util.*

/**
 * A [CommandGroup] that runs sequentially, meaning that each command will run one after the other.
 * It will only count as finished once all of the commands have finished.
 * The counterpart of [ParallelCommandGroup]
 */
class SequentialCommandGroup(vararg commands: Command) : CommandGroup {
    //for anyone who doesnt know what a Queue is, essentially a list but you can only access the front
    //so when you add stuff to it, it adds to the end of the list, and you can get/remove the object in the front
    //so it works in FIFO (First In First Out) order
    private val commands: Queue<Command> = LinkedList()
    private val requirements: MutableSet<Subsystem> = HashSet()
    override var isCancelable = true
    private var running = false

    init {
        addCommands(*commands)
        for (command in this.commands) {
            requirements.addAll(command.requiredSubsystems())
            if (!command.isCancelable) isCancelable = false
        }
    }

    override fun addCommands(vararg commands: Command) {
        check(!running) { "Cannot add commands to a running command group" }
        this.commands.addAll(listOf(*commands))
    }

    override fun init() {
        //only initialize the first command, the others are initialized in the update method once the previous one is finished
        if (!commands.isEmpty()) {
            val currentCommand = commands.element()
            currentCommand.init()
        }
    }

    override fun update() {
        if (!running) running = true
        if (!commands.isEmpty()) {
            var currentCommand = commands.element()
            if (currentCommand.isFinished) {
                commands.remove()
                currentCommand.done()
                if (!commands.isEmpty()) {
                    currentCommand = commands.element()
                    currentCommand.init()
                }
            }
            if (!commands.isEmpty()) {
                currentCommand.update()
            }

        }
        if (commands.isEmpty()) running = false
    }

    override val isFinished: Boolean
        get() = commands.isEmpty()

    override fun cancel() {
        for (command in commands) {
            command.cancel()
        }
    }

    override fun requiredSubsystems(): MutableSet<Subsystem> {
        return requirements
    }

}