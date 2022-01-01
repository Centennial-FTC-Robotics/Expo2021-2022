package expo.util

import android.graphics.Color
import com.qualcomm.ftccommon.SoundPlayer
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Blinker
import expo.Robot
import expo.gamepad.ExpoGamepad
import java.util.concurrent.TimeUnit

abstract class ExpoOpMode(var team: Team) : LinearOpMode() {
    protected lateinit var controller1: ExpoGamepad
    protected lateinit var controller2: ExpoGamepad
    protected lateinit var robot: Robot

    constructor() : this(Team.BLUE)

    override fun runOpMode() {
        robot = Robot
        val hubs: List<LynxModule> = hardwareMap.getAll(LynxModule::class.java)
        for (hub in hubs) {
            //morse code for end me
            hub.pattern = listOf(
                Blinker.Step(Color.MAGENTA, 250 * 1, TimeUnit.MILLISECONDS), //e
                Blinker.Step(Color.BLACK, 250 * 3, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.MAGENTA, 250 * 3, TimeUnit.MILLISECONDS), //n1
                Blinker.Step(Color.BLACK, 250 * 1, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.MAGENTA, 250 * 1, TimeUnit.MILLISECONDS), //n2
                Blinker.Step(Color.BLACK, 250 * 3, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.MAGENTA, 250 * 3, TimeUnit.MILLISECONDS), //d1
                Blinker.Step(Color.BLACK, 250 * 3, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.MAGENTA, 250 * 1, TimeUnit.MILLISECONDS), //d2
                Blinker.Step(Color.BLACK, 250 * 3, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.MAGENTA, 250 * 1, TimeUnit.MILLISECONDS), //d3
                Blinker.Step(Color.BLACK, 250 * 7, TimeUnit.MILLISECONDS),   //space
                Blinker.Step(Color.MAGENTA, 250 * 3, TimeUnit.MILLISECONDS), //m1
                Blinker.Step(Color.BLACK, 250 * 1, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.MAGENTA, 250 * 3, TimeUnit.MILLISECONDS), //m2
                Blinker.Step(Color.BLACK, 250 * 1, TimeUnit.MILLISECONDS),
                Blinker.Step(Color.MAGENTA, 250 * 1, TimeUnit.MILLISECONDS), //e
                Blinker.Step(Color.BLACK, 250 * 7, TimeUnit.MILLISECONDS),   //space
            )
            //enable bulk reads
            hub.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL
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

    public enum class Team {
        BLUE, RED
    }


}