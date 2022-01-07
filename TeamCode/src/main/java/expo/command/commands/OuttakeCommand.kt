package expo.command.commands

import com.qualcomm.robotcore.util.ElapsedTime
import expo.Robot
import expo.command.Command
import expo.subsystems.Outtake

class OuttakeCommand(private val position: Outtake.OuttakePosition) : Command {
    override var isFinished: Boolean = false
    private var stage = -1
    private var timer = ElapsedTime()

    override fun update() {
        when (position) {
            Outtake.OuttakePosition.REST -> {
                if (stage == -1 || stage == -2) {
                    Robot.outtake.setCarriagePosition(.1)
                    Robot.outtake.setJoint1(.04)
                    Robot.outtake.setJoint3(.57)
                    stage++
                    timer.reset()
                } else if (stage == 0) {
                    if (timer.milliseconds() > 300) {
                        timer.reset()
                        Robot.outtake.setJoint2(1.0)
                        stage++
                    }
                } else if (stage == 1) {
                    if (timer.milliseconds() > 400)
                        isFinished = true
                }
            }
            Outtake.OuttakePosition.LEFT -> {
                if (stage == -1) {
                    Robot.outtake.setCarriagePosition(.6)
                    Robot.outtake.setJoint3(.57)
                    Robot.outtake.setJoint2(.15)
                    timer.reset()
                    stage++
                } else if (stage == 0) {
                    if (timer.milliseconds() > 400) {
                        Robot.outtake.setJoint3(0.0)
                        timer.reset()
                        stage++
                    }
                } else if (stage == 1) {
                    if (timer.milliseconds() > 1500) {
                        isFinished = true
                    } else if (timer.milliseconds() > 600) {
                        Robot.outtake.setCarriagePosition(.1)
                    }
                }
            }
            Outtake.OuttakePosition.MIDDLE -> {
                if (stage == -1) {
                    Robot.outtake.setCarriagePosition(.6)
                    Robot.outtake.setJoint3(.57)
                    Robot.outtake.setJoint2(.15)
                    timer.reset()
                    stage++
                } else if (stage == 0) {
                    if (timer.milliseconds() > 1500) {
                        isFinished = true
                    } else if (timer.milliseconds() > 600) {
                        Robot.outtake.setCarriagePosition(.1)
                    }
                }
            }
            Outtake.OuttakePosition.RIGHT -> {
                if (stage == -1) {
                    Robot.outtake.setCarriagePosition(.6)
                    Robot.outtake.setJoint2(.1)
                    timer.reset()
                    stage++
                } else if (stage == 0) {
                    if (timer.milliseconds() > 200) {
                        stage++
                        Robot.outtake.setJoint1(.54)
                        timer.reset()
                    }
                } else if (stage == 1) {
                    if (timer.milliseconds() > 1500) {
                        isFinished = true
                        Robot.outtake.setCarriagePosition(.1)
                    } else if (timer.milliseconds() > 600) {
                        Robot.outtake.setCarriagePosition(.1)
                    }
                }
            }

        }
    }

    override fun done() {
        if (position != Outtake.OuttakePosition.REST) {
//            OuttakeCommand(Outtake.OuttakePosition.REST).schedule()
        }
    }

}