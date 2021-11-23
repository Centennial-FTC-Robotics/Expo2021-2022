package expo

import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs

//pid controller with kP kI and kD in constructor
class PIDController(private var kP: Double, private var kI: Double, private var kD: Double) {
    private var lastError: Double = 0.0
    private var integral: Double = 0.0
    private var time: ElapsedTime = ElapsedTime()

    fun reset() {
        time.reset()
        lastError = 0.0
        integral = 0.0
    }

    fun changePID(kP: Double, kI: Double, kD: Double) {
        this.kP = kP
        this.kI = kI
        this.kD = kD
    }

    fun getPID(): Triple<Double, Double, Double> {
        return Triple(kP, kI, kD)
    }

    fun increase(index: Int, amount: Double) {
        when (index) {
            0 -> kP += amount
            1 -> kI += amount
            2 -> kD += amount
        }
    }

    fun update(error: Double): Double {
        val dt = time.milliseconds()
        time.reset()
        integral += error * dt
        val derivative = (error - lastError) / dt
        lastError = error

        //antiwindup
        if (abs(integral) > 1.0) {
            integral = 0.0
        }

        return kP * error + kI * integral + kD * derivative
    }
}