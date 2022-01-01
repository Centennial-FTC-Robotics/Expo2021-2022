package expo.gamepad

import expo.util.ExpoGamepad

class ButtonToggle(private val gamepad: ExpoGamepad, private val button: Button, private var toggle: Boolean) {
    private var current: Boolean = false
    private var last: Boolean = false

    constructor(gamepad: ExpoGamepad, button: Button) : this(gamepad, button, false)

    fun update() {
        last = current
        current = if (button == Button.A || button == Button.B)
            gamepad.getButton(button) && !gamepad.getButton(Button.START)
        else
            gamepad.getButton(button)
        //if the button is pressed
        //and it wasnt pressed before
        //then change the toggle

        if (!last && current) toggle = !toggle
    }

    fun getValue() = toggle
}