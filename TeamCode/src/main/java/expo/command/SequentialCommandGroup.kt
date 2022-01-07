package expo.command

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
    private val commands: MutableList<Command> = LinkedList()
    private val requirements: MutableSet<Subsystem> = HashSet()
    private var index = 0
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
        index = 0
        //only initialize the first command, the others are initialized in the update method once the previous one is finished
        if (commands.isNotEmpty()) {
            val currentCommand = commands[index]
            currentCommand.init()
        }
    }

    override fun update() {
        if (!running) running = true
        if (index < commands.size) {
            var currentCommand = commands[index]
            if (currentCommand.isFinished) {
                index++
                currentCommand.done()
                if (index < commands.size) {
                    currentCommand = commands[index]
                    currentCommand.init()
                }
            }
            if (index < commands.size) {
                currentCommand.update()
            }

        }
        if (commands.isEmpty()) running = false
    }

    override val isFinished: Boolean
        get() = index >= commands.size

    override fun cancel() {
        for (command in commands) {
            command.cancel()
        }
    }

    override fun requiredSubsystems(): MutableSet<Subsystem> {
        return requirements
    }

}