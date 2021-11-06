package expo.commands

import expo.Subsystem
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.jvm.JvmOverloads

class CommandScheduler {
    private val subsystemsInUse: MutableMap<Subsystem, Command> = HashMap()
    private val scheduledCommands: MutableSet<Command> = HashSet()
    private val registeredSubsystems: MutableSet<Subsystem> = HashSet()

    /**
     * Runs update on all of the commands that are scheduled.
     * If a command is finished, it will be removed from the scheduled commands list and the command's done() method will be called
     */
    fun update() {
        //first it gets all the commands that are finished
        val finished: MutableSet<Command> = HashSet()
        for (scheduledCommand in scheduledCommands) {
            if (scheduledCommand.isFinished) {
                finished.add(scheduledCommand)
            }
        }
        //and calls done, removes them from the scheduled commands list, and frees up their subsystems
        for (command in finished) {
            command.done()
            scheduledCommands.remove(command)
            subsystemsInUse.keys.removeAll(command.requiredSubsystems())
        }

        //and calls update on everything that is still running
        for (registeredSubsystem in registeredSubsystems) {
            registeredSubsystem.update()
        }
        for (scheduledCommand in scheduledCommands) {
            scheduledCommand.update()
        }
    }

    /**
     * Schedules a command. command.init() will be called immediately,
     * then command.update() will be repeatedly called until command.isFinished() returns true.
     * If a subsystem that a command requires is already in use by another command, the command will not be scheduled unless
     * the command using up the subsystem is cancelable
     *
     *
     * Note that commands added to the scheduler will not be run until the scheduler is updated.
     * Commands that are not apart of a group are run in parallel. Use a command group to run them in a specific sequence.
     *
     * @param command The command to schedule
     */
    fun schedule(command: Command) {
        val requirements = command.requiredSubsystems()
        if (!Collections.disjoint(requirements, subsystemsInUse.keys)) {
            for (subsystem in requirements) {
                val usingSubsystem = subsystemsInUse[subsystem]
                if (usingSubsystem != null && !usingSubsystem.isCancelable) {
                    return
                }
            }
            for (subsystem in requirements) {
                if (subsystemsInUse.containsKey(subsystem)) {
                    val usingSubsystem = subsystemsInUse.getValue(subsystem)
                    usingSubsystem.cancel()
                    subsystemsInUse.remove(subsystem)
                    scheduledCommands.remove(usingSubsystem)
                }
            }
        }
        for (subsystem in requirements) {
            subsystemsInUse[subsystem] = command
        }
        command.init()
        scheduledCommands.add(command)
    }

    /**
     * Schedules a list of commands. If one or more of the commands cannot be scheduled, the others are still scheduled.
     * If the commands rely on each other to all be scheduled, do not use this. Use a command group instead.
     *
     * @param commands: The commands to schedule
     */
    fun schedule(vararg commands: Command) {
        for (command in commands) {
            schedule(command)
        }
    }

    /**
     * Registers a subsystem's update() method to be called by the scheduler.
     *
     * @param subsystem: The subsystem to register
     */
    fun registerSubsystem(subsystem: Subsystem) {
        registeredSubsystems.add(subsystem)
    }

    /**
     * Force interrupts the given command.
     * This will run even if command.isCancelable() is false.
     *
     * @param command: The command to force interrupt
     */
    fun forceInterrupt(command: Command) {
        command.cancel()
    }

    /**
     * Frees up the given subsystem by canceling the command using it. If the command is not interruptable, it will not be canceled.
     *
     * @param subsystem: The command to free up
     * @param force: If true, the command will be forced to interrupt even if it is not cancelable
     * @return true if the subsystem was freed up or if it was not in use, false if it was not freed up due to not being interruptable
     */
    @JvmOverloads
    fun freeSubsystem(subsystem: Subsystem, force: Boolean = false): Boolean {
        if (!subsystemsInUse.containsKey(subsystem)) {
            return true
        }
        val command: Command = subsystemsInUse.getValue(subsystem)
        if (force || command.isCancelable) {
            command.cancel()
            return true
        }
        return false
    }

    companion object {
        var instance: CommandScheduler = CommandScheduler()
            private set
    }
}