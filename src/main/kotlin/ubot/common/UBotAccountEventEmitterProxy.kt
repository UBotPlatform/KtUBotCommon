package ubot.common

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.encodeToJsonElement
import twitter.qiqiworld1.ktjsonrpcpeer.RpcChannel

internal class UBotAccountEventEmitterProxy constructor(private val rpc: RpcChannel)
    : UBotAccountEventEmitter {
    override suspend fun onReceiveChatMessage(type: Int, source: String, sender: String, message: String, info: ChatMessageInfo) {
        return rpc.call("on_receive_chat_message", buildJsonArray {
            add(type)
            add(source)
            add(sender)
            add(message)
            add(Json.encodeToJsonElement(info))
        })
    }

    override suspend fun onMemberJoined(source: String, sender: String, inviter: String) {
        return rpc.call("on_member_joined", buildJsonArray {
            add(source)
            add(sender)
            add(inviter)
        })
    }

    override suspend fun onMemberLeft(source: String, sender: String) {
        return rpc.call("on_member_left", buildJsonArray {
            add(source)
            add(sender)
        })
    }

    override suspend fun processGroupInvitation(sender: String, target: String, reason: String): UBotEventResultWithReason {
        return rpc.call("process_group_invitation", buildJsonArray {
            add(sender)
            add(target)
            add(reason)
        })
    }

    override suspend fun processFriendRequest(sender: String, reason: String): UBotEventResultWithReason {
        return rpc.call("process_friend_request", buildJsonArray {
            add(sender)
            add(reason)
        })
    }

    override suspend fun processMembershipRequest(source: String, sender: String, inviter: String, reason: String): UBotEventResultWithReason {
        return rpc.call("process_membership_request", buildJsonArray {
            add(source)
            add(sender)
            add(inviter)
            add(reason)
        })
    }
}