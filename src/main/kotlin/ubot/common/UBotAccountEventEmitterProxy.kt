package ubot.common

import ktjsonrpcpeer.RpcChannel

internal class UBotAccountEventEmitterProxy constructor(private val rpc: RpcChannel)
    : UBotAccountEventEmitter {
    override suspend fun onReceiveChatMessage(type: Int, source: String, sender: String, message: String, info: ChatMessageInfo) {
        return rpc.call("on_receive_chat_message", arrayOf(type, source, sender, message, info))
    }

    override suspend fun onMemberJoined(source: String, sender: String, inviter: String) {
        return rpc.call("on_member_joined", arrayOf(source, sender, inviter))
    }

    override suspend fun onMemberLeft(source: String, sender: String) {
        return rpc.call("on_member_left", arrayOf(source, sender))
    }

    override suspend fun processGroupInvitation(sender: String, target: String, reason: String): UBotEventResultWithReason {
        return rpc.call("process_group_invitation", arrayOf(sender, target, reason))
    }

    override suspend fun processFriendRequest(sender: String, reason: String): UBotEventResultWithReason {
        return rpc.call("process_friend_request", arrayOf(sender, reason))
    }

    override suspend fun processMembershipRequest(source: String, sender: String, inviter: String, reason: String): UBotEventResultWithReason {
        return rpc.call("process_membership_request", arrayOf(source, sender, inviter, reason))
    }
}