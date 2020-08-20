package ubot.common

open class UBotEventResultWithReason(type: Int, val reason: String? = null) : UBotEventResult(type) {
    companion object {
        fun AcceptRequest(reason: String? = null): UBotEventResultWithReason {
            return UBotEventResultWithReason(10, reason)
        }

        fun RejectRequest(reason: String? = null): UBotEventResultWithReason {
            return UBotEventResultWithReason(20, reason)
        }
    }
}