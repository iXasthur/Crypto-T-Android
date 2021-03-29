package com.osmosys.myosmo.utils.extensions

import java.text.DecimalFormat

fun Double.format(fracDigits: Int): String {
    val df = DecimalFormat()
    df.maximumFractionDigits = fracDigits
    return df.format(this)
}