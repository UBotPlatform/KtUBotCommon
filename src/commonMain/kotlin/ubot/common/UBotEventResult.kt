package ubot.common
import kotlinx.serialization.Serializable

@Serializable
class UBotEventResult(override val type: Int) : AbsUBotEventResult() {
    companion object {
        val Ignore = UBotEventResult(0)
        val Continue = UBotEventResult(1)
        val Completed = UBotEventResult(2)
    }
}