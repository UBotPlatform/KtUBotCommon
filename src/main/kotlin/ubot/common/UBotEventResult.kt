package ubot.common

open class UBotEventResult(val type: Int) {
    companion object {
        val Ignore = UBotEventResult(0)
        val Continue = UBotEventResult(1)
        val Completed = UBotEventResult(2)
    }
}