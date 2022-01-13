package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime
import expo.Robot
import expo.Subsystem
import expo.command.Command
import expo.command.CommandScheduler
import expo.command.ParallelCommandGroup
import expo.command.SequentialCommandGroup
import expo.command.commands.*
import expo.logger.Logger
import expo.subsystems.OpenCVAprilTag
import expo.subsystems.Outtake
import expo.util.ExpoOpMode
import expo.util.Vector

@Autonomous(name = "Blue Cycle Auto", group = "Blue", preselectTeleOp = "Blue TeleOp")
class BlueCycleAuto : ExpoOpMode(Team.BLUE) {
    companion object {
        private val allianceHub = Vector(18.0, 24.0 * 2.5)
        private val warehouse = Vector(0.0, 107.0)
    }

    override fun runOpMode() {
        super.runOpMode()
        val pos = Robot.openCVAprilTag.pos
        var cycle: Command


        when (pos) {
            OpenCVAprilTag.Position.CENTER -> {
                cycle = ParallelCommandGroup(
                    MoveToPositionCommand(
                        Vector(0.0, allianceHub.getY()),
                        0.0,
                        headingTolerance = 5.0,
                        tolerance = 1.5
                    ),
                    OuttakeCommand(Outtake.OuttakePosition.SECOND)
                )
            }
            OpenCVAprilTag.Position.RIGHT -> {

                cycle = SequentialCommandGroup(
                    MoveToPositionCommand(
                        Vector(0.0, allianceHub.getY()),
                        0.0,
                        headingTolerance = 5.0,
                        tolerance = 1.5
                    ),
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
                    MoveToPositionCommand(
                        Vector(0.0, allianceHub.getY()),
                        0.0,
                        headingTolerance = 5.0,
                        tolerance = 1.5
                    ),
                    OuttakeCommand(Outtake.OuttakePosition.MIDDLE)
                )
            }
        }

        val timer = ElapsedTime()
        Robot.IMU.setStartAngle(90.0)
        Robot.odometry.setStartPos(0.0, 24.0 * 3.5, Math.PI / 2.0)

        val cycles = SequentialCommandGroup()

        for (i in 0..1) {
            cycles.addCommands(
                cycle
            )

            var yMod = 0.0
            var xMod = 0.0
            if (i == 1) {
//                yMod = 5.0
                xMod = 10.0
            }
            cycles.addCommands(
                OuttakeCommand(Outtake.OuttakePosition.REST),
                //rotate to face the warehouse
                ParallelCommandGroup(
//                    SlidesCommand(0),
                    TurnCommand(0.0, 3.0),
                ),
            )

            if (i == 0) {
                cycles.addCommands(
                    object : MoveUntilWall() {
                        override fun init() {
                            Robot.intake.setPower(.3)
                        }
                    },
                )
            } else {
                cycles.addCommands(MoveUntilWall())
            }

            cycles.addCommands(
                //+ (i * 1.15)
                MoveToPositionCommand(
                    Vector(warehouse.getX(), warehouse.getY()), 0.0, headingTolerance = .0,
                    tolerance = 1.5
                ),
            )
            //and then flip intake
            if (i == 0) {
                cycles.addCommands(IntakeCommand())
            }

        }

        waitForStart()

        cycles.schedule()

        while (opModeIsActive() && CommandScheduler.instance.isRunning()) {
            Logger.getInstance().addItem("Time spent", timer.seconds(), Int.MIN_VALUE)
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