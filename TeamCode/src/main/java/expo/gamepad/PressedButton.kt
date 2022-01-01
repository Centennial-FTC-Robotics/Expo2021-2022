package expo.gamepad

/**
 * This class is used to track the state of a button.
 * A buttons state is only true if it was false before and is now true.
 *
 * Use this to check if a button was just pressed for things that CANNOT run repeatedly
 * If it can run repeatedly  (like 500 times in one button press), then just grab it directly from the ExpoGamepad
 */
class PressedButton(private val gamepad: ExpoGamepad, private val button: Button) {
    private var lastState: Boolean = false

    fun getState(): Boolean {
        val state = gamepad.getButton(button)
        if (state != lastState) {
            lastState = state
            return state
        }
        return false
    }
}