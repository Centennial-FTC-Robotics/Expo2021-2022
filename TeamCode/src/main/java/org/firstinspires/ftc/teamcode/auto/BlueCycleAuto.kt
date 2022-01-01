package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.util.ElapsedTime
import expo.Robot
import expo.command.Command
import expo.command.CommandScheduler
import expo.command.ParallelCommandGroup
import expo.command.SequentialCommandGroup
import expo.command.commands.*
import expo.logger.Logger
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
        val timer = ElapsedTime()
        Robot.IMU.setStartAngle(90.0)
        Robot.odometry.setStartPos(0.0, 24.0 * 2.5, Math.PI / 2.0)

        val cycles = SequentialCommandGroup()

        for (i in 0..1) {
            if (i > 0) {
                cycles.addCommands(
                    MoveToPositionCommand(
                        Vector(0.0, allianceHub.getY()),
                        0.0,
                        headingTolerance = 5.0,
                        tolerance = 1.5
                    ),
                )
            }
            var yMod = 0.0
            var xMod = 0.0
            if (i == 1) {
//                yMod = 5.0
                xMod = 10.0
            }
            cycles.addCommands(
                ParallelCommandGroup(
                    //move to alliance hub and get ready to outtake
                    MoveToPositionCommand(
                        Vector(allianceHub.getX() + xMod, allianceHub.getY() - yMod),
                        90.0,
                        headingTolerance = 5.0,
                        tolerance = 1.5
                    ),
//                    SlidesCommand(650),
                ),
                //outtake once we get there
                OuttakeCommand(Outtake.OuttakePosition.MIDDLE),
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