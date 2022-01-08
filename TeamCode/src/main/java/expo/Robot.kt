package expo

import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.util.ReadWriteFile
import expo.command.CommandScheduler
import expo.logger.Item
import expo.logger.Logger
import expo.subsystems.*
import expo.util.ExpoOpMode
import expo.util.Vector
import org.firstinspires.ftc.robotcore.internal.system.AppUtil
import java.io.File


object Robot {
    val drivetrain = Drivetrain()
    val IMU = IMU()
    val motorTester = MotorTester()
    val odometry = Odometry()
    val odoLifter = OdometryLifter()
    val hubs = ArrayList<LynxModule>()
    val intake = Intake()
    val outtake = Outtake()
    val spinner = CarouselSpinner()
    val wallSensors = WallSensors()
    val openCVAprilTag = OpenCVAprilTag()

    lateinit var opMode: ExpoOpMode

    private val odoData: File = AppUtil.getInstance().getSettingsFile("odoData.txt")


    private val subsystems = listOf(drivetrain, IMU, odometry, odoLifter, intake, outtake, spinner, wallSensors, openCVAprilTag)
    fun initialize(opMode: ExpoOpMode) {
        this.opMode = opMode
        hubs.addAll(opMode.hardwareMap.getAll(LynxModule::class.java))
        CommandScheduler.reset()
        Logger.reset()
        for (subsystem: Subsystem in subsystems)
            subsystem.initialize(opMode)

        update()
        while (CommandScheduler.instance.isRunning() && opMode.opModeIsActive()) {
            update()
        }

        Logger.getInstance().addLine(Item.Line("Robot initialized"))
        Logger.getInstance().update()
    }

    fun update() {
        for (hub in opMode.hardwareMap.getAll(LynxModule::class.java)) {
            hub.clearBulkCache()
        }
        CommandScheduler.instance.update()
        Logger.getInstance().update()
    }

    fun saveOdoData() {
        var data = ""
        val pos: Vector = odometry.getPos()
        val angle: Double = Math.toDegrees(odometry.getHeading())
        data += pos.getX().toString() + "," + pos.getY().toString() + "," + angle.toString() + "," + opMode.team.name
        ReadWriteFile.writeFile(odoData, data)
    }

    fun readOdoData() {
        val rawData = ReadWriteFile.readFile(odoData)
        val rawDataArr = rawData.split(",").toTypedArray()
        try {
            val x = rawDataArr[0].toDouble()
            val y = rawDataArr[1].toDouble()
            val heading = rawDataArr[2].toFloat()
            val team = ExpoOpMode.Team.valueOf(rawDataArr[3])
            odometry.setStartPos(x, y, Math.toRadians(heading.toDouble()))
            if (heading > 180) {
                var diff = heading - 180
                diff -= 180f
                IMU.setStartAngle(diff.toDouble())
            } else {
                IMU.setStartAngle(heading.toDouble())
            }
            opMode.team = team
        } catch (e: NumberFormatException) {
            odometry.setStartPos(0.0, 0.0, 0.0)
            IMU.setStartAngle(0.0)
        } catch (e: ArrayIndexOutOfBoundsException) {
            odometry.setStartPos(0.0, 0.0, 0.0)
            IMU.setStartAngle(0.0)
        }
    }
}