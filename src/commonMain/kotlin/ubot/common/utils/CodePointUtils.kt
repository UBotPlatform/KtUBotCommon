package ubot.common.utils


internal fun Char.Companion.toCodePoint(high: Char, low: Char): Int =
    (((high - MIN_HIGH_SURROGATE) shl 10) or (low - MIN_LOW_SURROGATE)) + 0x10000

internal fun StringBuilder.appendCodePoint(codePoint: Int): StringBuilder = apply {
    val isBmpCodePoint = codePoint ushr 16 == 0
    if (isBmpCodePoint) {
        append(codePoint.toChar())
    } else {
        val highSurrogate = ((codePoint ushr 10) + (Char.MIN_HIGH_SURROGATE.toInt() - (0x10000 ushr 10))).toChar()
        val lowSurrogate = ((codePoint and 0x3ff) + Char.MIN_LOW_SURROGATE.toInt()).toChar()
        append(highSurrogate)
        append(lowSurrogate)
    }
}