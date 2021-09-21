package expo.util;

import java.util.Arrays;

public class Vector {
    private double x;
    private double y;
    private double theta;
    private double R;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;

        findTheta();
    }

    public void add(Vector other) {
        x += other.getX();
        y += other.getY();

        findTheta();
    }

    public void sub(Vector other) {
        x -= other.getX();
        y -= other.getY();

        findTheta();
    }


    public void rotate(double radians) {
        theta = (theta + radians) % (2 * Math.PI);
        findXY();
    }

    public void findXY() {
        x = R * Math.cos(theta);
        y = R * Math.sin(theta);
    }

    public void findTheta() {
        findXY();

        if (x == 0 && y == 0) {
            theta = 0;
        } else if (x == 0) {
            theta = Math.PI / 2;
            if (y < 0)
                theta *= -1;
        } else if (y == 0) {
            theta = Math.PI;
        } else {
            theta = Math.atan(y / x);
            if (y > 0) {
                theta += Math.PI;
            } else {
                theta -= Math.PI;
            }
        }
    }

    public void findR() {
        R = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
