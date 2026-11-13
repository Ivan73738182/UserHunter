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

class MainActivity : AppCompatActivity() {

    private var coins = 0
    private lateinit var tvCoins: TextView
    private lateinit var tvUsername: TextView
    private lateinit var rootLayout: ConstraintLayout
    private lateinit var floatingContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvCoins = findViewById(R.id.tvCoins)
        tvUsername = findViewById(R.id.tvUsername)
        rootLayout = findViewById(R.id.rootLayout)
        floatingContainer = findViewById(R.id.floatingContainer)

        rootLayout.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // 1. Получаем ник из генератора
                val generated = UsernameGenerator.generate()

                // 2. Анимация пружинки
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

                // 3. Обновляем текст и цвет (в зависимости от редкости)
                tvUsername.text = generated.name
                tvUsername.setTextColor(generated.rarity.color)

                // 4. Начисляем монеты
                coins += generated.rarity.reward
                tvCoins.text = "Монеты: $coins"

                // 5. Показываем всплывающее "+N" с цветом редкости
                showFloatingText(event.x, event.y, generated.rarity.reward, generated.rarity.color)

                // 6. Вибрация (сильнее для редких)
                vibrate(generated.rarity)
            }
            true
        }
    }

    private fun showFloatingText(x: Float, y: Float, reward: Int, color: Int) {
        val tv = TextView(this)
        tv.text = "+$reward"
        tv.setTextColor(color)
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

        tv.animate()
            .translationY(-150f)
            .alpha(0f)
            .setDuration(700)
            .withEndAction { floatingContainer.removeView(tv) }
            .start()
    }

    private fun vibrate(rarity: Rarity) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        val duration = when (rarity) {
            Rarity.COMMON -> 20L
            Rarity.RARE -> 50L
            Rarity.LEGENDARY -> 100L
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(duration)
        }
    }
}
