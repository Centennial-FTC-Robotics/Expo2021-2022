package expo.util

import android.graphics.Color
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.qualcomm.ftccommon.SoundPlayer
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.BNO055IMUImpl
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Blinker
import com.qualcomm.robotcore.hardware.ServoImplEx
import com.qualcomm.robotcore.hardware.configuration.ServoFlavor
import expo.Robot
import java.util.concurrent.TimeUnit

abstract class ExpoOpMode : LinearOpMode() {
    protected lateinit var controller1: ExpoGamepad
    protected lateinit var controller2: ExpoGamepad
    protected lateinit var robot: Robot
    override fun runOpMode() {
        robot = Robot
        val hubs: List<LynxModule> = hardwareMap.getAll(LynxModule::class.java)
        for (hub in hubs) {
            hub.pattern = listOf(
                Blinker.Step(Color.RED, 1000, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.BLUE, 1000, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.GREEN, 1000, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.YELLOW, 1000, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.CYAN, 1000, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.MAGENTA, 1000, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.WHITE, 1000, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.BLACK, 1000, TimeUnit.MILLISECONDS)
            )
        }
        robot.initialize(this)
        controller1 = ExpoGamepad(gamepad1)
        controller2 = ExpoGamepad(gamepad2)
//        playSound("bruh")
        waitForStart()
    }

    protected fun updateGamepads() {
        controller1.update()
        controller2.update()
    }

    fun playSound(name: String) {
        val id = hardwareMap.appContext.resources.getIdentifier(name, "raw", hardwareMap.appContext.packageName)
        val found = SoundPlayer.getInstance().preload(hardwareMap.appContext, id)
        if (found) {
            SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, id)
            val item = telemetry.addData(">", "Playing")
            item.setRetained(true)
        }

    }


}