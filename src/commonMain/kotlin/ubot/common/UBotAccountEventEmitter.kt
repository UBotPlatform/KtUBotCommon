package ubot.common

import com.github.arcticlampyrid.ktjsonrpcpeer.RpcChannel

interface UBotAccountEventEmitter {
    suspend fun onReceiveChatMessage(type: Int, source: String, sender: String, message: String, info: ChatMessageInfo)
    suspend fun onMemberJoined(source: String, sender: String, inviter: String)
    suspend fun onMemberLeft(source: String, sender: String)
    suspend fun processGroupInvitation(sender: String, target: String, reason: String): UBotEventResult
    suspend fun processFriendRequest(sender: String, reason: String): UBotEventResult
    suspend fun processMembershipRequest(
        source: String,
        sender: String,
        inviter: String,
        reason: String
    ): UBotEventResult

    companion object {
        fun of(rpc: RpcChannel): UBotAccountEventEmitter {
            return UBotAccountEventEmitterProxy(rpc)
        }
    }
}