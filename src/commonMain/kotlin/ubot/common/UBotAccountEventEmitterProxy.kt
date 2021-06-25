package ubot.common

import com.github.arcticlampyrid.ktjsonrpcpeer.RpcChannel
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*

internal class UBotAccountEventEmitterProxy constructor(private val rpc: RpcChannel) : UBotAccountEventEmitter {
    override suspend fun onReceiveChatMessage(
        type: Int,
        source: String,
        sender: String,
        message: String,
        info: ChatMessageInfo
    ) {
        return rpc.call("on_receive_chat_message", Unit.serializer(), JsonArray.serializer(), buildJsonArray {
            add(type)
            add(source)
            add(sender)
            add(message)
            add(Json.encodeToJsonElement(info))
        })
    }

    override suspend fun onMemberJoined(source: String, sender: String, inviter: String) {
        return rpc.call("on_member_joined", Unit.serializer(), JsonArray.serializer(), buildJsonArray {
            add(source)
            add(sender)
            add(inviter)
        })
    }

    override suspend fun onMemberLeft(source: String, sender: String) {
        return rpc.call("on_member_left", Unit.serializer(), JsonArray.serializer(), buildJsonArray {
            add(source)
            add(sender)
        })
    }

    override suspend fun processGroupInvitation(
        sender: String,
        target: String,
        reason: String
    ): UBotEventResultWithReason {
        return rpc.call(
            "process_group_invitation",
            UBotEventResultWithReason.serializer(),
            JsonArray.serializer(),
            buildJsonArray {
                add(sender)
                add(target)
                add(reason)
            })
    }

    override suspend fun processFriendRequest(sender: String, reason: String): UBotEventResultWithReason {
        return rpc.call(
            "process_friend_request",
            UBotEventResultWithReason.serializer(),
            JsonArray.serializer(),
            buildJsonArray {
                add(sender)
                add(reason)
            })
    }

    override suspend fun processMembershipRequest(
        source: String,
        sender: String,
        inviter: String,
        reason: String
    ): UBotEventResultWithReason {
        return rpc.call(
            "process_membership_request",
            UBotEventResultWithReason.serializer(),
            JsonArray.serializer(),
            buildJsonArray {
                add(source)
                add(sender)
                add(inviter)
                add(reason)
            })
    }
}