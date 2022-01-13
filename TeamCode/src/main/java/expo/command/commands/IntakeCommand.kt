package expo.command.commands

import com.qualcomm.robotcore.util.ElapsedTime
import expo.Robot
import expo.Subsystem
import expo.command.Command

class IntakeCommand(private var state: Int) : Command {
    override var isFinished: Boolean = false
    private var timer = ElapsedTime()

    constructor() : this(0)


    override fun update() {
        if (state == 0) {
            Robot.intake.setPower(.4)
            timer.reset()
            state++;
        } else if (state == 1) {
            if (timer.milliseconds() > 500) {
                Robot.intake.setPower(.15)
                Robot.intake.setJointPosition(1.0)
                state++
                timer.reset()
            }
        } else if (state == 2) {
            if (timer.milliseconds() > 1300) {
                Robot.intake.setPower(0.0)
                Robot.intake.setJointPosition(0.0)
                state++
                timer.reset()
            } else if (timer.milliseconds() > 500) {
                Robot.intake.setPower(0.45)
            }
        } else if (state == 3) {
            if (timer.milliseconds() > 500) {
                isFinished = true
            }
        }
    }

    override fun done() {

    }


    override fun requiredSubsystems(): MutableSet<Subsystem> {
        return mutableSetOf(Robot.intake)
    }
}