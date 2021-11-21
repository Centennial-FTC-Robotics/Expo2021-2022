package org.firstinspires.ftc.teamcode.tests

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import expo.Robot
import expo.Subsystem
import expo.commands.Command
import expo.commands.CommandScheduler
import expo.commands.SequentialCommandGroup
import expo.util.Button
import expo.util.ExpoOpMode

@TeleOp(name="Command Tester", group = "Tests")
class CommandTest : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()


        controller1.registerPressedButton(Button.A)
        while (opModeIsActive()) {
            CommandScheduler.instance.update()
            if (controller1.getPressedButton(Button.A)) {
                CommandScheduler.instance.schedule(SequentialCommandGroup(IntakeCommand(), IntakeHingeCommand()))
            }

        }
    }

    class IntakeCommand : Command {
        private val time = ElapsedTime()
        private var finished = false
        override fun init() {
            time.reset()
        }

        override fun update() {
            if (time.seconds() < 2.0) {
                Robot.intake.setIntakePower(.2)
            } else {
                Robot.intake.setIntakePower(0.0)
                finished = true
            }
        }

        override val isFinished: Boolean
            get() = finished

        override fun requiredSubsystems(): MutableSet<Subsystem> {
            return mutableSetOf(Robot.intake)
        }
    }

    class IntakeHingeCommand : Command {
        private val time = ElapsedTime()
        override fun init() {
            time.reset()
        }

        override fun update() {
            Robot.intake.setIntakePosition(.5)
        }

        override val isFinished: Boolean
            get() = time.seconds() > 1.0

        override fun requiredSubsystems(): MutableSet<Subsystem> {
            return mutableSetOf(Robot.intake)
        }
    }


}