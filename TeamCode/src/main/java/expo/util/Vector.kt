package expo.util

import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


class Vector(private var x: Double, private var y: Double) {
    private var theta = 0.0
    private var R = 0.0

    init {
        findTheta()
    }

    fun add(other: Vector) {
        x += other.getX()
        y += other.getY()
        findTheta()
    }

    fun sub(other: Vector) {
        x -= other.getX()
        y -= other.getY()
        findTheta()
    }


    fun rotate(radians: Double) {
        theta = (theta + radians) % (2 * Math.PI)
        findXY()
    }

    private fun findXY() {
        x = R * cos(theta)
        y = R * sin(theta)
    }

    private fun findTheta() {
        findXY()
        if (x == 0.0 && y == 0.0) {
            theta = 0.0
        } else if (x == 0.0) {
            theta = Math.PI / 2F
            if (y < 0) theta *= -1.0
        } else if (y == 0.0) {
            theta = Math.PI
        } else {
            theta = Math.atan(y / x)
            if (y > 0) {
                theta += Math.PI
            } else {
                theta -= Math.PI
            }
        }
    }

    fun findR() {
        R = sqrt(x.pow(2) + y.pow(2))
    }

    fun getX(): Double {
        return x
    }

    fun getY(): Double {
        return y
    }
}