package expo;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class Robot {
    private Subsystem[] subsystems = {};

    public void initalize(LinearOpMode opMode) {
        for(Subsystem subsystem : subsystems) {
            subsystem.initalize(opMode);
        }
    }
}
