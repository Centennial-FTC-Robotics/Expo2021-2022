package expo.gamepad

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Gamepad
import expo.gamepad.ButtonToggle
import expo.gamepad.PressedButton
import expo.util.Vector
import java.util.*
import kotlin.collections.HashMap

class ExpoGamepad(private val gamepad: Gamepad) {
    var toggles = HashMap<Button, ButtonToggle>()
    var pressedButtons = HashMap<Button, PressedButton>()

    fun update() {
        for (entry: Map.Entry<Button, ButtonToggle> in toggles.entries) {
            val toggle: ButtonToggle = entry.value
            toggle.update()
        }

    }

    fun registerToggle(button: Button) {
        registerToggle(button, false)
    }

    fun registerToggle(button: Button, state: Boolean) {
        toggles[button] = ButtonToggle(this, button, state)
    }

    fun registerPressedButton(button: Button) {
        pressedButtons[button] = PressedButton(this, button)
    }

    fun getPressedButton(button: Button) = pressedButtons.getValue((button)).getState()


    fun getToggle(button: Button): Boolean {
        return toggles.getValue(button).getValue()
    }

    fun getButton(button: Button): Boolean {
        return when (button) {
            Button.A -> gamepad.a && !gamepad.start
            Button.B -> gamepad.b && !gamepad.start
            Button.X -> gamepad.x
            Button.Y -> gamepad.y
            Button.START -> gamepad.start
            Button.BACK -> gamepad.back
            Button.DOWN -> gamepad.dpad_down
            Button.LEFT -> gamepad.dpad_left
            Button.RIGHT -> gamepad.dpad_right
            Button.UP -> gamepad.dpad_up
            Button.LEFT_BUMPER -> gamepad.left_bumper
            Button.RIGHT_BUMPER -> gamepad.right_bumper
            Button.LEFT_STICK -> gamepad.left_stick_button
            Button.RIGHT_STICK -> gamepad.right_stick_button
        }
    }

    fun getLeftX(): Double {
        return gamepad.left_stick_x.toDouble()
    }

    fun getLeftY(): Double {
        return -gamepad.left_stick_y.toDouble()
    }

    fun getRightX(): Double {
        return gamepad.right_stick_x.toDouble()
    }

    fun getRightY(): Double {
        return gamepad.right_stick_y.toDouble()
    }

    fun getControllerVector(): Vector = Vector(getLeftX(), getLeftY())

    fun getControllerVector(angle: Double): Vector {
        val controlVector = Vector(getLeftX(), getLeftY())
        controlVector.rotate(angle)
        return controlVector
    }

    fun rumble(left: Double, right: Double, duration: Int) {
        gamepad.rumble(left, right, duration)
    }

    fun rumble(duration: Int) {
        gamepad.rumble(duration)
    }

    fun rumbleBlips(count: Int) {
        gamepad.rumbleBlips(count)
    }

    fun rumbleCustom(customEffect: Gamepad.RumbleEffect) {
        gamepad.runRumbleEffect(customEffect)
    }

    fun printControlVector(opMode: LinearOpMode) {
        val vector = getControllerVector()
        opMode.telemetry.addData("leftX", vector.getX())
        opMode.telemetry.addData("leftY", vector.getY())

    }
}