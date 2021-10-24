package expo.util

import com.qualcomm.robotcore.hardware.Gamepad
import java.util.*

class ExpoGamepad(private val gamepad: Gamepad) {
    var toggles = HashMap<Button, ButtonToggle>()

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

    fun getToggle(button: Button): Boolean {
        val toggle: ButtonToggle = toggles[button] ?: return false
        return toggle.getValue()
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
        return gamepad.left_stick_y.toDouble()
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
}