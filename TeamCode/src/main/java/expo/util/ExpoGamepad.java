package expo.util;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpoGamepad {
    private Gamepad gamepad;
    private Map<Button, ButtonToggle> toggles = new HashMap<>();
    public ExpoGamepad(Gamepad gamepad) {
        this.gamepad = gamepad;
    }

    //if someone can think of a better way to do this
    //by all means go ahead
    //i kinda dont like using hashmaps but only way i can think of
    public void update() {
        for (Map.Entry<Button, ButtonToggle> entry : toggles.entrySet()) {
            ButtonToggle toggle = entry.getValue();
            toggle.update();
        }
    }

    public void registerToggle(Button button) {
        registerToggle(button, false);
    }
    public void registerToggle(Button button, boolean state) {
        toggles.put(button, new ButtonToggle(this,button, state));
    }

    public boolean getToggle(Button button) {
        ButtonToggle toggle = toggles.get(button);
        if(toggle == null)
            return false;
        return toggle.getValue();
    }


    public boolean getButton(Button button) {
        switch (button) {
            case A:
                return gamepad.a;
            case B:
                return gamepad.b;
            case X:
                return gamepad.x;
            case Y:
                return gamepad.y;
            case START:
                return gamepad.start;
            case BACK:
                return gamepad.back;
            case DOWN:
                return gamepad.dpad_down;
            case LEFT:
                return gamepad.dpad_left;
            case RIGHT:
                return gamepad.dpad_right;
            case UP:
                return gamepad.dpad_up;
            case LEFT_BUMPER:
                return gamepad.left_bumper;
            case RIGHT_BUMPER:
                return gamepad.right_bumper;
            case LEFT_STICK:
                return gamepad.left_stick_button;
            case RIGHT_STICK:
                return gamepad.right_stick_button;
        }
        //this will never run and if it does u have bigger problems
        return false;
    }

    public double getLeftX() {
        return gamepad.left_stick_x;
    }

    public double getLeftY() {
        return gamepad.left_stick_y;
    }

    public double getRightX() {
        return gamepad.right_stick_x;
    }

    public double getRightY() {
        return gamepad.right_stick_y;
    }
}
