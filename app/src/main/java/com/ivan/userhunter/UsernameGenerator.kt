package com.ivan.userhunter

import kotlin.random.Random

enum class Rarity(val color: Int, val reward: Int) {
    COMMON(0xFFAAAAAA.toInt(), 5),      // Серый
    RARE(0xFF2196F3.toInt(), 15),       // Синий
    EPIC(0xFF9C27B0.toInt(), 50),       // Фиолетовый
    LEGENDARY(0xFFFFD700.toInt(), 150), // Золотой
    ABSOLUTE(0xFFFF0055.toInt(), 1000)  // Неоново-красный
}

data class Username(
    val name: String,
    val rarity: Rarity
)

object UsernameGenerator {

    private val prefixes = listOf("", "", "", "mr_", "real_", "the_", "super_", "pro_", "max_", "_")
    private val words = listOf("ivan", "cool", "gamer", "moscow", "star", "cat", "dog", "fox", "king", "hunter", "user", "boss", "ninja", "wolf", "dev", "god")
    private val suffixes = listOf("", "", "_2009", "_2024", "_99", "_x", "_pro", "_1", "_007")

    // Эпические (короткие, но с цифрами)
    private val epicNames = listOf("ivan1", "gamer99", "moscow_x", "hunter007", "wolf_1", "ninja_2", "boss_3", "dev_4")

    // Легендарные (чистые слова)
    private val legendaryNames = listOf("admin", "god", "love", "money", "durov", "boss", "king", "star", "moon", "sun", "zero", "one", "top", "vip", "hacker", "ghost")

    // Абсолютные (самые редкие и крутые)
    private val absoluteNames = listOf("ivan", "alex", "max", "artem", "danil", "timur", "telegram", "hunter", "user", "legend")

    fun generate(): Username {
        // Генерируем число от 0.0 до 100.0
        val roll = Random.nextDouble(0.0, 100.0)

        return when {
            roll < 0.1 -> {
                // 0.1% шанс на Абсолютный
                val name = absoluteNames.random()
                Username("@$name", Rarity.ABSOLUTE)
            }
            roll < 10.0 -> {
                // 9.9% шанс на Легендарный
                val name = legendaryNames.random()
                Username("@$name", Rarity.LEGENDARY)
            }
            roll < 25.0 -> {
                // 15% шанс на Эпический
                val name = epicNames.random()
                Username("@$name", Rarity.EPIC)
            }
            roll < 55.0 -> {
                // 30% шанс на Редкий
                val word = words.random()
                val suffix = listOf("", "_x", "_pro", "_1").random()
                Username("@$word$suffix", Rarity.RARE)
            }
            else -> {
                // 45% шанс на Обычный
                val prefix = prefixes.random()
                val word = words.random()
                val suffix = suffixes.random()
                val digits = if (Random.nextBoolean()) Random.nextInt(10, 9999).toString() else ""
                Username("@$prefix$word$suffix$digits", Rarity.COMMON)
            }
        }
    }
}
