package expo.commands

import expo.Subsystem

interface Command {
    /**
     * Called once when command is first scheduled
     */
    fun init()

    /**
     * Called repeatedly while command has been scheduled
     */
    fun update()

    /**
     * Checks if the command has finished execution
     * @return true if command is done, false otherwise
     */
    val isFinished: Boolean

    /**
     * Called to stop the command's execution early
     * Will be called if isCancelable() returns true
     */
    fun cancel()

    /**
     * Called once the command has finished execution
     */
    fun done()

    /**
     * Returns the set of subsystems this command requires
     * @return the set of subsystems this command requires
     */
    fun requiredSubsystems(): MutableSet<Subsystem>

    /**
     * Checks if the command can be cancelled early
     * @return true if the command can be cancelled early, false otherwise
     */
    val isCancelable: Boolean

    /**
     * Schedules this command to be run by the scheduler
     */
    fun schedule() {
        CommandScheduler.instance.schedule(this)
    }
}