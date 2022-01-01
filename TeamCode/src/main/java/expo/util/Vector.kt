package expo.util

import kotlin.math.*


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
        findR()
        if (x == 0.0 && y == 0.0) {
            theta = 0.0
        } else {
            if (x == 0.0) {
                theta = Math.PI / 2
                if (y < 0) {
                    theta *= -1.0
                }
            } else {
                theta = atan(y / x)
                if (x < 0) {
                    if (y >= 0) {
                        theta += Math.PI
                    } else if (y < 0) {
                        theta -= Math.PI
                    } else {
                        theta = Math.PI
                    }
                }
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

    override fun toString(): String {
        return "Vector(x=$x, y=$y)"
    }

    companion object {
        fun add(v1: Vector, v2: Vector): Vector {
            val copy = Vector(v1.getX(), v1.getY())
            copy.add(v2)
            return copy
        }

        fun sub(v1: Vector, v2: Vector): Vector {
            val copy = Vector(v1.getX(), v1.getY())
            copy.sub(v2)
            return copy
        }

    }
}