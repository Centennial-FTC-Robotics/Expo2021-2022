package expo.command

import expo.Subsystem

interface Command {
    /**
     * Called once when command is first scheduled
     */
    fun init() {
        // Do nothing by default
    }

    /**
     * Called repeatedly while command has been scheduled
     */
    fun update()

    /**
     * Checks if the command has finished execution
     * @return true if command is done, false otherwise
     */

    val isFinished: Boolean
        get() = false //Return false (never stops) by default

    /**
     * Called to stop the command's execution early
     * Will be called if isCancelable() returns true
     */
    fun cancel() {
        done()
    }

    /**
     * Called once the command has finished execution
     */
    fun done() {
        // Do nothing by default
    }

    /**
     * Returns the set of subsystems this command requires
     * @return the set of subsystems this command requires
     */
    fun requiredSubsystems(): MutableSet<Subsystem> = mutableSetOf() //Return empty set by default

    /**
     * Checks if the command can be cancelled early
     * @return true if the command can be cancelled early, false otherwise
     */
    val isCancelable: Boolean
        get() = false //Return false by default

    /**
     * Schedules this command to be run by the scheduler
     */
    fun schedule() {
        CommandScheduler.instance.schedule(this)
    }
}