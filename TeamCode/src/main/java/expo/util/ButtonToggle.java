package expo.util;

public class ButtonToggle {
    private Button button;
    private ExpoGamepad gamepad;

    private boolean toggle;
    private boolean current;
    private boolean last;

    public ButtonToggle(ExpoGamepad gamepad, Button button, boolean toggle) {
        this.gamepad = gamepad;
        this.button = button;
        this.toggle = toggle;
    }

    public ButtonToggle(ExpoGamepad gamepad, Button button) {
        this(gamepad, button, false);
    }


    public void update() {
        last = current;
        current = gamepad.getButton(button);

        //if the button is pressed
        //and it wasnt pressed before
        //then change the toggle
        if (current && !last) {
            toggle = !toggle;
        }
    }

    public boolean getValue() {
        return toggle;
    }
}
