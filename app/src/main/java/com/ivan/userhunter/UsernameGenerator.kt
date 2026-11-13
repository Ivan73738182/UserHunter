package com.ivan.userhunter

import kotlin.random.Random

enum class Rarity(val color: Int, val reward: Int) {
    COMMON(0xFFAAAAAA.toInt(), 5),    // Серый
    RARE(0xFF2196F3.toInt(), 25),     // Синий
    LEGENDARY(0xFFFFD700.toInt(), 100) // Золотой
}

data class Username(
    val name: String,
    val rarity: Rarity
)

object UsernameGenerator {

    // Части для генерации
    private val prefixes = listOf("", "", "", "mr_", "real_", "the_", "super_", "pro_", "max_", "_")
    private val words = listOf("ivan", "cool", "gamer", "moscow", "star", "cat", "dog", "fox", "king", "hunter", "user", "boss", "ninja", "wolf", "dev", "god")
    private val suffixes = listOf("", "", "_2009", "_2024", "_99", "_x", "_pro", "_1", "_007")

    // Легендарные ники (короткие и красивые)
    private val legendaryNames = listOf(
        "admin", "god", "love", "money", "durov", "boss", "king", "star", "moon", "sun", "zero", "one", "top", "vip", "hacker", "ghost"
    )

    fun generate(): Username {
        val roll = Random.nextInt(100)

        return when {
            roll < 5 -> {
                // 5% шанс на легендарный ник (без префиксов и цифр)
                val name = legendaryNames.random()
                Username("@$name", Rarity.LEGENDARY)
            }
            roll < 25 -> {
                // 20% шанс на редкий ник (слово + маленький суффикс)
                val word = words.random()
                val suffix = listOf("", "_x", "_pro", "_1").random()
                Username("@$word$suffix", Rarity.RARE)
            }
            else -> {
                // 75% шанс на обычный ник (с префиксом, цифрами)
                val prefix = prefixes.random()
                val word = words.random()
                val suffix = suffixes.random()
                val digits = if (Random.nextBoolean()) Random.nextInt(10, 9999).toString() else ""
                Username("@$prefix$word$suffix$digits", Rarity.COMMON)
            }
        }
    }
}
