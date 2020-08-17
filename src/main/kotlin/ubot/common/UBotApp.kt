package ubot.common

import ktjsonrpcpeer.RpcChannel

interface UBotApp {
    suspend fun onReceiveChatMessage(bot: String, type: Int, source: String, sender: String, message: String, info: ChatMessageInfo): UBotEventResult
    suspend fun onMemberJoined(bot: String, source: String, sender: String, inviter: String): UBotEventResult
    suspend fun onMemberLeft(bot: String, source: String, sender: String): UBotEventResult

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
        }
    }
}