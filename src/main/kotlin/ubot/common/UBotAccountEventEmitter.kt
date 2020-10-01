package ubot.common

import twitter.qiqiworld1.ktjsonrpcpeer.RpcChannel

interface UBotAccountEventEmitter {
    suspend fun onReceiveChatMessage(type: Int, source: String, sender: String, message: String, info: ChatMessageInfo)
    suspend fun onMemberJoined(source: String, sender: String, inviter: String)
    suspend fun onMemberLeft(source: String, sender: String)
    suspend fun processGroupInvitation(sender: String, target: String, reason: String): UBotEventResultWithReason
    suspend fun processFriendRequest(sender: String, reason: String): UBotEventResultWithReason
    suspend fun processMembershipRequest(source: String, sender: String, inviter: String, reason: String): UBotEventResultWithReason

    companion object {
        fun of(rpc: RpcChannel): UBotAccountEventEmitter {
            return UBotAccountEventEmitterProxy(rpc)
        }
    }
}