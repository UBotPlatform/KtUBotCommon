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
    ): UBotEventResultWithReason

    suspend fun processFriendRequest(bot: String, sender: String, reason: String): UBotEventResultWithReason
    suspend fun processMembershipRequest(
        bot: String,
        source: String,
        sender: String,
        inviter: String,
        reason: String
    ): UBotEventResultWithReason

    companion object {
        fun UBotApp.applyTo(rpc: RpcServiceDsl) {
            rpc.register<UBotEventResult, JsonArray>("on_receive_chat_message") { params ->
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
            rpc.register<UBotEventResult, JsonArray>("on_member_joined") { params ->
                this.onMemberJoined(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(3)?.jsonPrimitive?.content ?: "",
                )
            }
            rpc.register<UBotEventResult, JsonArray>("on_member_left") { params ->
                this.onMemberLeft(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                )
            }
            rpc.register<UBotEventResultWithReason, JsonArray>("process_group_invitation") { params ->
                this.processGroupInvitation(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(3)?.jsonPrimitive?.content ?: "",
                )
            }
            rpc.register<UBotEventResultWithReason, JsonArray>("process_friend_request") { params ->
                this.processFriendRequest(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                )
            }
            rpc.register<UBotEventResultWithReason, JsonArray>("process_membership_request") { params ->
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