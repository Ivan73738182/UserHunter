package com.ivan.userhunter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.Gravity
import android.view.MotionEvent
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var coins = 0
    private lateinit var tvCoins: TextView
    private lateinit var tvUsername: TextView
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var floatingContainer: FrameLayout

    private val usernames = listOf(
        "@user_123", "@ivan_2009", "@cool_guy", "@moscow", "@gamer_99",
        "@ivan", "@admin", "@god", "@durov", "@love", "@star"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCoins = findViewById(R.id.tvCoins)
        tvUsername = findViewById(R.id.tvUsername)
        rootLayout = findViewById(R.id.rootLayout)
        floatingContainer = findViewById(R.id.floatingContainer)

        rootLayout.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // 1. Анимация пружинки для ника
                tvUsername.animate()
                    .scaleX(0.85f)
                    .scaleY(0.85f)
                    .setDuration(80)
                    .withEndAction {
                        tvUsername.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(150)
                            .setInterpolator(OvershootInterpolator())
                            .start()
                    }
                    .start()

                // 2. Выдаём случайный ник
                tvUsername.text = usernames[Random.nextInt(usernames.size)]

                // 3. Начисляем монеты
                coins += 5
                tvCoins.text = "Монеты: $coins"

                // 4. Показываем всплывающее "+5"
                showFloatingText(event.x, event.y)

                // 5. Вибрация
                vibrate()
            }
            true
        }
    }

    private fun showFloatingText(x: Float, y: Float) {
        val tv = TextView(this)
        tv.text = "+5"
        tv.setTextColor(0xFFFFD700.toInt())
        tv.textSize = 28f
        tv.setShadowLayer(4f, 0f, 0f, 0xFF000000.toInt())

        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        params.leftMargin = (x - 40).toInt()
        params.topMargin = (y - 40).toInt()
        params.gravity = Gravity.TOP or Gravity.START
        tv.layoutParams = params

        floatingContainer.addView(tv)

        // Анимация: улетает вверх и растворяется
        tv.animate()
            .translationY(-150f)
            .alpha(0f)
            .setDuration(700)
            .withEndAction { floatingContainer.removeView(tv) }
            .start()
    }

    private fun vibrate() {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(30, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(30)
        }
    }
}
