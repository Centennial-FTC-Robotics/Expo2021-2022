package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime
import expo.Robot
import expo.command.Command
import expo.command.CommandScheduler
import expo.command.SequentialCommandGroup
import expo.command.commands.CarouselCommand
import expo.command.commands.MoveToPositionCommand
import expo.command.commands.OuttakeCommand
import expo.logger.Logger
import expo.subsystems.Outtake
import expo.util.ExpoOpMode
import expo.util.Vector

@Autonomous(name = "Blue Park Auto", group = "Blue", preselectTeleOp = "Main TeleOp")
class BlueParkAuto : ExpoOpMode(Team.BLUE) {
    companion object {
        private val carousel = Vector(0.0, 24.0 * .5)
        private val allianceHub = Vector(24.0 * 1.5, 24.0 * .5)
        private val storageUnit = Vector(24.0 * 2.0, 0.0)
    }

    override fun runOpMode() {
        super.runOpMode()
        Robot.IMU.setStartAngle(90.0)
        Robot.odometry.setStartPos(0.0, 24.0 * 1.5, Math.PI / 2.0)


        SequentialCommandGroup(
//            MoveToPositionCommand(carousel, 90.0),
//            CarouselCommand(),
//            CarouselCommand(),
            MoveToPositionCommand(allianceHub, 0.0),
            OuttakeCommand(Outtake.OuttakePosition.MIDDLE),
            MoveToPositionCommand(storageUnit, 90.0),
        ).schedule()

        waitForStart()

        while (opModeIsActive() && CommandScheduler.instance.isRunning()) {
            Robot.update()
        }

        Robot.saveOdoData()

        SequentialCommandGroup(
            OuttakeCommand(Outtake.OuttakePosition.MIDDLE),
            object : Command {
                val timer = ElapsedTime()
                override var isFinished: Boolean = false

                override fun init() {
                    timer.reset()
                }

                override fun update() {
                    Robot.intake.setJointPosition(1.0)
                    if (timer.milliseconds() > 1000) {
                        isFinished = true
                    }
                }
            },
            OuttakeCommand(Outtake.OuttakePosition.REST),
        ).schedule()

        while (opModeIsActive() && CommandScheduler.instance.isRunning()) {
//            Logger.getInstance().addItem("Time spent", timer.seconds(), Int.MIN_VALUE)
            Robot.update()
        }
    }

}