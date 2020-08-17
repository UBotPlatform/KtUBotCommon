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
}