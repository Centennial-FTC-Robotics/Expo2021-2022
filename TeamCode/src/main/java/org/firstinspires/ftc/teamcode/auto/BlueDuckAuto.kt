package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime
import expo.Robot
import expo.Subsystem
import expo.command.Command
import expo.command.CommandScheduler
import expo.command.ParallelCommandGroup
import expo.command.SequentialCommandGroup
import expo.command.commands.CarouselCommand
import expo.command.commands.MoveToPositionCommand
import expo.command.commands.MoveUntilWall
import expo.command.commands.OuttakeCommand
import expo.subsystems.OpenCVAprilTag
import expo.subsystems.Outtake
import expo.util.ExpoOpMode
import expo.util.Vector

@Autonomous(name = "Blue Duck Auto", group = "Blue", preselectTeleOp = "Main TeleOp")
class BlueDuckAuto : ExpoOpMode(Team.BLUE) {
    companion object {
        private val carousel = Vector(12.0, 0.0)
        private val allianceHub = Vector(18.0, 24.0 * 2.5)
        private val storageUnit = Vector(24.0 * 2.0, 0.0)
        private val warehouse = Vector(0.0, 107.0)

    }

    override fun runOpMode() {
        super.runOpMode()

        val pos = Robot.openCVAprilTag.pos
        var cycle: Command
        Robot.IMU.setStartAngle(90.0)
        Robot.odometry.setStartPos(0.0, 24.0 * 1.5, Math.PI / 2.0)

        when (pos) {
            OpenCVAprilTag.Position.CENTER -> {
                cycle = ParallelCommandGroup(
                    MoveToPositionCommand(allianceHub, 90.0),
                    OuttakeCommand(Outtake.OuttakePosition.SECOND)
                )
            }
            OpenCVAprilTag.Position.RIGHT -> {
                cycle = SequentialCommandGroup(
                    MoveToPositionCommand(allianceHub, 0.0),
                    object : Command {
                        val timer = ElapsedTime()
                        override var isFinished: Boolean = false
                        override fun init() {
                            Robot.intake.setJointPosition(1.0)
                            timer.reset()
                        }

                        override fun update() {
                            if (timer.milliseconds() > 1300) {
                                Robot.intake.setJointPosition(0.0)
                                isFinished = true
                            }
                        }

                        override fun requiredSubsystems(): MutableSet<Subsystem> {
                            return mutableSetOf(Robot.intake)
                        }
                    },
                    object : Command {
                        val timer = ElapsedTime()
                        override var isFinished: Boolean = false
                        override fun init() {
                            Robot.intake.setPower(-.4)
                            timer.reset()
                        }

                        override fun update() {
                            if (timer.milliseconds() > 1300) {
                                Robot.intake.setJointPosition(0.0)
                                isFinished = true
                            }
                        }

                        override fun requiredSubsystems(): MutableSet<Subsystem> {
                            return mutableSetOf(Robot.intake)
                        }
                    },

                    )
            }
            else -> {
                cycle = SequentialCommandGroup(
                    MoveToPositionCommand(allianceHub, 90.0),
                    OuttakeCommand(Outtake.OuttakePosition.MIDDLE)
                )
            }
        }

        SequentialCommandGroup(
            MoveToPositionCommand(Vector(16.0, 24.0 * 1.5), 90.0, maxSpeed = .3, maxAngleSpeed = .2, minSpeed = .03),
//            MoveUntilWall(1.0),
//            object : Command {
//                override var isFinished: Boolean = false
//                override fun update() {
//                    isFinished = true
//                    val pos = Robot.odometry.getPos()
//                    Robot.odometry.setStartPos(pos.getX(), 0.0, 90.0)
//                }
//            },
//            MoveToPositionCommand(carousel, 90.0),
//            CarouselCommand(Team.RED),
//            CarouselCommand(Team.RED),
//            CarouselCommand(Team.RED),
//            CarouselCommand(Team.RED),
//            CarouselCommand(Team.RED),
//            CarouselCommand(Team.RED),
//            CarouselCommand(Team.RED),
//            CarouselCommand(Team.RED),
//            cycle,
//            MoveToPositionCommand(
//                warehouse, 90.0
//            ),
        ).schedule()

        while (opModeIsActive() && CommandScheduler.instance.isRunning()) {
            Robot.update()
        }

        Robot.saveOdoData()

//        SequentialCommandGroup(
//            OuttakeCommand(Outtake.OuttakePosition.MIDDLE),
//            object : Command {
//                val timer = ElapsedTime()
//                override var isFinished: Boolean = false
//
//                override fun init() {
//                    timer.reset()
//                }
//
//                override fun update() {
//                    Robot.intake.setJointPosition(1.0)
//                    if (timer.milliseconds() > 1000) {
//                        isFinished = true
//                    }
//                }
//            },
//            OuttakeCommand(Outtake.OuttakePosition.REST),
//        ).schedule()
//
//        while (opModeIsActive() && CommandScheduler.instance.isRunning()) {
////            Logger.getInstance().addItem("Time spent", timer.seconds(), Int.MIN_VALUE)
//            Robot.update()
//        }
    }

}