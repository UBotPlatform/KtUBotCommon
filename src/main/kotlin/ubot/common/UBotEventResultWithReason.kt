package ubot.common

import kotlinx.serialization.Serializable

@Serializable
class UBotEventResultWithReason(override val type: Int, val reason: String? = null) : AbsUBotEventResult() {
    companion object {
        val Ignore = UBotEventResultWithReason(0)

        fun AcceptRequest(reason: String? = null): UBotEventResultWithReason {
            return UBotEventResultWithReason(10, reason)
        }

        fun RejectRequest(reason: String? = null): UBotEventResultWithReason {
            return UBotEventResultWithReason(20, reason)
        }
    }
}