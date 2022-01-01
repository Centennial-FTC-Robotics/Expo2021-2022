package expo

import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs

//pid controller with kP kI and kD in constructor
class PIDController(private var kP: Double, private var kI: Double, private var kD: Double) {
    private var lastError: Double = 0.0
    private var integral: Double = 0.0
    private var time: ElapsedTime = ElapsedTime()
    private var maxIntegral: Double = 0.0

    init {
        findMaxIntegral()
    }

    fun reset() {
        time.reset()
        lastError = 0.0
        integral = 0.0
    }

    fun changePID(kP: Double, kI: Double, kD: Double) {
        this.kP = kP
        this.kI = kI
        this.kD = kD
        findMaxIntegral()
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
        findMaxIntegral()
    }

    private fun findMaxIntegral() {
        //a good value for this is where the max value of the integral term * kI is .25
        //so maxIntegral * kI = .25
        //https://www.ctrlaltftc.com/practical-improvements-to-pid#integral-windup-and-mitigation-methods
        maxIntegral = .25 / kI
    }

    fun update(error: Double): Double {
        val dt = time.milliseconds()
        time.reset()
        integral += error * dt
        val derivative = (error - lastError) / dt

        //antiwindup
        if (error > 0 && lastError < 0) {
            integral = 0.0
        } else if (error < 0 && lastError > 0) {
            integral = 0.0
        }

        if (abs(integral) > maxIntegral) {
            integral = maxIntegral * if (integral > 0) 1 else -1
        }



        lastError = error
        return kP * error + kI * integral + kD * derivative
    }
}