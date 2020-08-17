package ubot.common

import ktjsonrpcpeer.RpcChannel

interface UBotAccountEventEmitter {
    suspend fun onReceiveChatMessage(type: Int, source: String, sender: String, message: String, info: ChatMessageInfo)
    suspend fun onMemberJoined(source: String, sender: String, inviter: String)
    suspend fun onMemberLeft(source: String, sender: String)

    companion object {
        fun of(rpc: RpcChannel): UBotAccountEventEmitter {
            return UBotAccountEventEmitterProxy(rpc)
        }
    }
}