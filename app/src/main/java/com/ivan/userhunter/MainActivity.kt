package com.ivan.userhunter

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var coins = 0
    private lateinit var tvCoins: TextView
    private lateinit var tvUsername: TextView
    private lateinit var rootLayout: ConstraintLayout

    // Список ников для выпадения
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

        // Слушаем тап по любому месту экрана
        rootLayout.setOnClickListener {
            // Выдаём случайный ник
            val randomUsername = usernames[Random.nextInt(usernames.size)]
            tvUsername.text = randomUsername
            
            // Начисляем монеты
            coins += 5
            tvCoins.text = "Монеты: $coins"
        }
    }
}
