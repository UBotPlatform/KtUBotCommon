package ubot.common

import com.github.arcticlampyrid.ktjsonrpcpeer.RpcChannel.Companion.jsonIgnoringUnknownKeys
import com.github.arcticlampyrid.ktjsonrpcpeer.RpcServiceDsl
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

interface UBotApp {
    suspend fun onReceiveChatMessage(
        bot: String,
        type: Int,
        source: String,
        sender: String,
        message: String,
        info: ChatMessageInfo
    ): UBotEventResult

    suspend fun onMemberJoined(bot: String, source: String, sender: String, inviter: String): UBotEventResult
    suspend fun onMemberLeft(bot: String, source: String, sender: String): UBotEventResult
    suspend fun processGroupInvitation(
        bot: String,
        sender: String,
        target: String,
        reason: String
    ): UBotEventResult

    suspend fun processFriendRequest(bot: String, sender: String, reason: String): UBotEventResult
    suspend fun processMembershipRequest(
        bot: String,
        source: String,
        sender: String,
        inviter: String,
        reason: String
    ): UBotEventResult

    companion object {
        fun UBotApp.applyTo(rpc: RpcServiceDsl) {
            rpc.register("on_receive_chat_message", UBotEventResult.serializer(), JsonArray.serializer()) { params ->
                this.onReceiveChatMessage(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.int ?: 0,
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(3)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(4)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(5)?.let {
                        jsonIgnoringUnknownKeys.decodeFromJsonElement(ChatMessageInfo.serializer(), it)
                    } ?: ChatMessageInfo()
                )
            }
            rpc.register("on_member_joined", UBotEventResult.serializer(), JsonArray.serializer()) { params ->
                this.onMemberJoined(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(3)?.jsonPrimitive?.content ?: "",
                )
            }
            rpc.register("on_member_left", UBotEventResult.serializer(), JsonArray.serializer()) { params ->
                this.onMemberLeft(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                )
            }
            rpc.register(
                "process_group_invitation",
                UBotEventResult.serializer(),
                JsonArray.serializer()
            ) { params ->
                this.processGroupInvitation(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(3)?.jsonPrimitive?.content ?: "",
                )
            }
            rpc.register(
                "process_friend_request",
                UBotEventResult.serializer(),
                JsonArray.serializer()
            ) { params ->
                this.processFriendRequest(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                )
            }
            rpc.register(
                "process_membership_request",
                UBotEventResult.serializer(),
                JsonArray.serializer()
            ) { params ->
                this.processMembershipRequest(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(3)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(4)?.jsonPrimitive?.content ?: ""
                )
            }
        }
    }
}