package ubot.common

import ktjsonrpcpeer.RpcChannel

interface UBotApp {
    suspend fun onReceiveChatMessage(bot: String, type: Int, source: String, sender: String, message: String, info: ChatMessageInfo): UBotEventResult
    suspend fun onMemberJoined(bot: String, source: String, sender: String, inviter: String): UBotEventResult
    suspend fun onMemberLeft(bot: String, source: String, sender: String): UBotEventResult
    suspend fun processGroupInvitation(bot: String, sender: String, target: String, reason: String): UBotEventResult
    suspend fun processFriendRequest(bot: String, sender: String, reason: String): UBotEventResult
    suspend fun processMembershipRequest(bot: String, source: String, sender: String, inviter: String, reason: String): UBotEventResult

    companion object {
        fun UBotApp.applyTo(rpc: RpcChannel) {
            rpc.register("on_receive_chat_message") { params ->
                this.onReceiveChatMessage(
                        RpcChannel.readParam(params, 0, "bot") ?: "",
                        RpcChannel.readParam(params, 1, "type") ?: 0,
                        RpcChannel.readParam(params, 2, "source") ?: "",
                        RpcChannel.readParam(params, 3, "sender") ?: "",
                        RpcChannel.readParam(params, 4, "message") ?: "",
                        RpcChannel.readParam(params, 5, "info") ?: ChatMessageInfo())
            }
            rpc.register("on_member_joined") { params ->
                this.onMemberJoined(
                        RpcChannel.readParam(params, 0, "bot") ?: "",
                        RpcChannel.readParam(params, 1, "source") ?: "",
                        RpcChannel.readParam(params, 2, "sender") ?: "",
                        RpcChannel.readParam(params, 3, "inviter") ?: "")
            }
            rpc.register("on_member_left") { params ->
                this.onMemberLeft(
                        RpcChannel.readParam(params, 0, "bot") ?: "",
                        RpcChannel.readParam(params, 1, "source") ?: "",
                        RpcChannel.readParam(params, 2, "sender") ?: "")
            }
            rpc.register("process_group_invitation") { params ->
                this.processGroupInvitation(
                        RpcChannel.readParam(params, 0, "bot") ?: "",
                        RpcChannel.readParam(params, 1, "sender") ?: "",
                        RpcChannel.readParam(params, 2, "target") ?: "",
                        RpcChannel.readParam(params, 3, "reason") ?: "")
            }
            rpc.register("process_friend_request") { params ->
                this.processFriendRequest(
                        RpcChannel.readParam(params, 0, "bot") ?: "",
                        RpcChannel.readParam(params, 1, "sender") ?: "",
                        RpcChannel.readParam(params, 2, "reason") ?: "")
            }
            rpc.register("process_membership_request") { params ->
                this.processMembershipRequest(
                        RpcChannel.readParam(params, 0, "bot") ?: "",
                        RpcChannel.readParam(params, 1, "source") ?: "",
                        RpcChannel.readParam(params, 2, "sender") ?: "",
                        RpcChannel.readParam(params, 3, "inviter") ?: "",
                        RpcChannel.readParam(params, 4, "reason") ?: "")
            }
        }
    }
}