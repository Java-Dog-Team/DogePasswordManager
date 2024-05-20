package com.example.dogepasswordmanager.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt

fun String.toColor() = Color(android.graphics.Color.parseColor(this))

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

var ItemColor = Color(235, 195, 18)
var BackGroundColor = "#0A1A2E".toColor()
var BrickRed = "#e26d5c".toColor()
var digitsColor = "#1f84e4".toColor()