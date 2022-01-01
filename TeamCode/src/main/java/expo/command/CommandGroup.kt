package expo.command

interface CommandGroup : Command {
    /**
     * Adds commands to the group
     * @param commands The commands to add
     */
    fun addCommands(vararg commands: Command)
    override fun done() {
        //nothing cus we call done in update when that command is finished
        //this method would be called by the command scheduler when all commands are finished
        //so we don't need to do anything here since we would want to call done() for each command in the group
        //once it finishes
    }
}