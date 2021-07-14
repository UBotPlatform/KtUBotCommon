package ubot.common
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmStatic

@Serializable
class UBotEventResult(val type: Int, val reason: String? = null) {
    companion object {
        @JvmStatic
        val Ignore = UBotEventResult(0)

        @JvmStatic
        val Continue = UBotEventResult(1)

        @JvmStatic
        val Completed = UBotEventResult(2)

        @JvmStatic
        val Accept = UBotEventResult(10)

        @JvmStatic
        val Reject = UBotEventResult(20)

        @Suppress("NOTHING_TO_INLINE", "FunctionName")
        @JvmStatic
        inline fun Accept(reason: String?) = UBotEventResult(10, reason)

        @Suppress("NOTHING_TO_INLINE", "FunctionName")
        @JvmStatic
        inline fun Reject(reason: String?) = UBotEventResult(20, reason)
    }
}