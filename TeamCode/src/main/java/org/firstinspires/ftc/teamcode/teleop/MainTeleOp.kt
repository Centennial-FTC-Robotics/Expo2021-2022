package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime
import expo.Robot
import expo.Subsystem
import expo.command.Command
import expo.command.CommandScheduler
import expo.command.SequentialCommandGroup
import expo.command.commands.CarouselCommand
import expo.command.commands.OuttakeCommand
import expo.gamepad.Button
import expo.subsystems.Drivetrain
import expo.subsystems.Outtake
import expo.util.ExpoOpMode

@TeleOp(name = "Main TeleOp", group = "Actual OpModes")
class MainTeleOp : ExpoOpMode() {
    override fun runOpMode() {
        super.runOpMode()
        controller1.registerPressedButton(Button.A)
        controller1.registerPressedButton(Button.B)
        controller1.registerPressedButton(Button.X)
        controller1.registerPressedButton(Button.Y)
        controller1.registerPressedButton(Button.UP)

        controller2.registerPressedButton(Button.A)
        controller2.registerPressedButton(Button.B)
        controller2.registerPressedButton(Button.X)
        controller2.registerPressedButton(Button.LEFT_BUMPER)
        controller2.registerPressedButton(Button.RIGHT_BUMPER)

        Robot.readOdoData()


        if (Robot.opMode.team == Team.BLUE) {
            Robot.odometry.setStartPos(
                Robot.odometry.getPos().getX(),
                Robot.odometry.getPos().getY(),
                Robot.odometry.getHeading() + (Math.PI / 2)
            )
            Robot.IMU.setStartAngle(Robot.IMU.getAngle() + 90)
        } else {
            Robot.odometry.setStartPos(
                Robot.odometry.getPos().getX(),
                Robot.odometry.getPos().getY(),
                Robot.odometry.getHeading() + (3 * Math.PI / 2)
            )
            Robot.IMU.setStartAngle(Robot.IMU.getAngle() + 270)
        }


        var intakePowerCommand: Command? = null
        var carouselPowerCommand: Command? = null
        var outtakePos = Outtake.OuttakePosition.MIDDLE
        var factor: Double
        while (opModeIsActive()) {
            Robot.update()
            updateGamepads()

            //drivetrain
            var power = 0.0
            if (controller1.getButton(Button.Y)) {
                power = Robot.drivetrain.turnJustPValue(0.0)
            } else if (controller1.getButton(Button.X)) {
                power = Robot.drivetrain.turnJustPValue(90.0)
            } else if (controller1.getButton(Button.A)) {
                power = Robot.drivetrain.turnJustPValue(180.0)
            } else if (controller1.getButton(Button.B)) {
                power = Robot.drivetrain.turnJustPValue(270.0)
            } else {
                power = 0.0
                Drivetrain.angleController.reset()
            }

            val cVector = controller1.getControllerVector()

            factor = if (gamepad1.right_trigger > 0.0 || gamepad1.left_trigger > 0.0) {
                1.0
            } else {
                .75
            }

            Robot.drivetrain.findAndSetMotorPowers(
                cVector.getX(),
                cVector.getY(),
                controller1.getRightX() + power,
                factor = factor
            )

            if (controller1.getPressedButton(Button.UP)) {
                Robot.IMU.setStartAngle(0.0)
                Robot.odometry.setStartPos(0.0, 0.0, 0.0)
            }

            //intake
            if (gamepad2.right_trigger > 0.0) {
                if (intakePowerCommand == null) {
                    intakePowerCommand = object : Command {
                        override fun update() {
                            Robot.intake.setPower(.3)
                        }

                        override fun done() {
                            Robot.intake.setPower(0.0)
                        }

                        override val isCancelable: Boolean
                            get() = true

                    }
                    intakePowerCommand.schedule()
                }
            } else if (gamepad2.left_trigger > 0.0) {
                if (intakePowerCommand == null) {
                    intakePowerCommand = object : Command {
                        override fun update() {
                            Robot.intake.setPower(-.3)
                        }

                        override fun done() {
                            Robot.intake.setPower(0.0)
                        }

                        override val isCancelable: Boolean
                            get() = true
                    }
                    intakePowerCommand.schedule()
                }
            } else {
                if (intakePowerCommand != null) {
                    CommandScheduler.instance.forceInterrupt(intakePowerCommand)
                    intakePowerCommand = null
                }
            }




            if (controller2.getPressedButton(Button.A)) {
//                IntakeCommand(1).schedule()
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

                }.schedule()
            }

            if (controller2.getButton(Button.LEFT)) {
                outtakePos = Outtake.OuttakePosition.LEFT
            } else if (controller2.getButton(Button.RIGHT)) {
                outtakePos = Outtake.OuttakePosition.RIGHT
            } else if (controller2.getButton(Button.UP)) {
                outtakePos = Outtake.OuttakePosition.MIDDLE
            }

            //outtake
            if (controller2.getPressedButton(Button.B)) {
                SequentialCommandGroup(
                    OuttakeCommand(outtakePos),
                    OuttakeCommand(Outtake.OuttakePosition.REST)
                ).schedule()
            }

            //carousel
            if (controller2.getPressedButton(Button.X)) {
                CarouselCommand(Team.RED).schedule()
            }

            if (controller2.getPressedButton(Button.LEFT_BUMPER)) {
                CarouselCommand(Team.BLUE).schedule()
            }

            if (controller2.getButton(Button.RIGHT_BUMPER)) {
                if (carouselPowerCommand == null) {
                    carouselPowerCommand = object : Command {
                        override fun update() {
                            Robot.spinner.setPower(-.5)
                        }

                        override fun done() {
                            Robot.spinner.setPower(0.0)
                        }

                        override val isCancelable: Boolean
                            get() = true

                        override val isFinished: Boolean
                            get() = false
                    }
                    carouselPowerCommand.schedule()
                }
            } else {
                if (carouselPowerCommand != null) {
                    CommandScheduler.instance.forceInterrupt(carouselPowerCommand)
                    carouselPowerCommand = null
                }
            }

        }
    }
}