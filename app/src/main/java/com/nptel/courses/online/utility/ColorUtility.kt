package com.nptel.courses.online.utility

import android.graphics.Color
import java.util.*

object ColorUtility {

    var CHOCOLATE = "#D2691E"
    var CORAL = "#FF7F50"
    var CRIMSON = "#DC143C"
    var FOREST_GREEN = "#228B22"
    var GOLD = "#FFD700"
    var HOT_PINK = "#FF69B4"
    var LIGHT_CORAL = "#F08080"
    var LIGHT_SEA_GREEN = "#20B2AA"
    var LIME_GREEN = "#32CD32"
    var MEDIUM_PURPLE = "#9370DB"
    var ORANGE_RED = "#FF4500"
    var SLATE_BLUE = "#6A5ACD"
    var STEEL_BLUE = "#4682B4"
    var TOMATO = "#FF6347"
    var VIOLATE = "#EE82EE"

    fun getRandomColor(redBound: Int, greenBound: Int, blueBound: Int, dark: Boolean): Int {
        val random = Random()

        // light -> max = bound, min = 0 -> (bound - 0 + 1) + 0
        //                                  (bound)
        // dark -> max = 256, min = 256 - bound -> (256 - 256 + bound + 1) + 256 - bound
        //                                          (bound + 1) + 256 - bound

        val red: Int =
            random.nextInt(if (dark) redBound + 1 else redBound) + if (dark) 256 - redBound else 0
        val green: Int =
            random.nextInt(if (dark) greenBound + 1 else greenBound) + if (dark) 256 - greenBound else 0
        val blue: Int =
            random.nextInt(if (dark) blueBound + 1 else blueBound) + if (dark) 256 - blueBound else 0


        return Color.rgb(red, green, blue)
    }

    val primaryRandomColor: Int
        get() {
            val random = Random()
            val ran = random.nextInt(21)
            return when (ran) {
                0 -> Color.BLUE
                1 -> Color.CYAN
                2 -> Color.GREEN
                3 -> Color.MAGENTA
                4 -> Color.RED
                5 -> Color.YELLOW
                6 -> Color.parseColor(CHOCOLATE)
                7 -> Color.parseColor(CORAL)
                8 -> Color.parseColor(CRIMSON)
                9 -> Color.parseColor(FOREST_GREEN)
                10 -> Color.parseColor(GOLD)
                11 -> Color.parseColor(HOT_PINK)
                12 -> Color.parseColor(LIGHT_CORAL)
                13 -> Color.parseColor(LIGHT_SEA_GREEN)
                14 -> Color.parseColor(LIME_GREEN)
                15 -> Color.parseColor(MEDIUM_PURPLE)
                16 -> Color.parseColor(ORANGE_RED)
                17 -> Color.parseColor(SLATE_BLUE)
                18 -> Color.parseColor(STEEL_BLUE)
                19 -> Color.parseColor(TOMATO)
                20 -> Color.parseColor(VIOLATE)
                else -> 0
            }
        }

    fun getRandomColors(bound: Int, size: Int, dark: Boolean = false): List<Int> {
        return getRandomColors(bound, bound, bound, size, dark)
    }

    fun getRandomColors(
        red: Int,
        green: Int,
        blue: Int,
        size: Int,
        dark: Boolean,
        previousColors: List<Int> = emptyList()
    ): List<Int> {
        val colors = mutableListOf<Int>()
        colors.addAll(previousColors)
        var i = 0
        while (colors.size < size) {
            val newColor = getRandomColor(red, green, blue, dark)
            if (colors.contains(newColor)) continue
            colors.add(newColor)
        }
        return colors
    }

    private fun checkIfArrayContainsValue(array: IntArray, value: Int): Boolean {
        for (element in array) if (element == value) return true
        return false
    }

    fun ARGB_To_RGB(argb: Int): Int {
        val alpha = Color.alpha(argb)
        var red = Color.red(argb)
        var green = Color.green(argb)
        var blue = Color.blue(argb)
        val fraction = (256 - alpha.toFloat()) / 256
        red = red + ((256 - red) * fraction).toInt()
        green = green + ((256 - green) * fraction).toInt()
        blue = blue + ((256 - blue) * fraction).toInt()
        return Color.rgb(red, green, blue)
    }

    fun getLighterVersion(color: Int): Int {
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        return Color.parseColor("#36" + hexColor.substring(hexColor.length - 6))
    }

    fun getLighterVersion(color: Int, alpha: String): Int {
        val hexColor = String.format("#%06X", 0xFFFFFF and color)
        return Color.parseColor(alpha + hexColor.substring(hexColor.length - 6))
    }
}