package ubot.common

import ktjsonrpcpeer.RpcChannel

interface UBotAccount {
    suspend fun getGroupName(id: String): String
    suspend fun getUserName(id: String): String
    suspend fun login()
    suspend fun logout()
    suspend fun sendChatMessage(type: Int, source: String, target: String, message: String)
    suspend fun removeMember(source: String, target: String)
    suspend fun shutupMember(source: String, target: String, duration: Int)
    suspend fun shutupAllMember(source: String, switch: Boolean)
    suspend fun getMemberName(source: String, target: String): String
    suspend fun getUserAvatar(id: String): String
    suspend fun getSelfID(): String

    companion object {
        fun UBotAccount.applyTo(rpc: RpcChannel) {
            rpc.register("get_group_name") { params ->
                this.getGroupName(
                        RpcChannel.readParam(params, 0, "id") ?: "")
            }
            rpc.register("get_user_name") { params ->
                this.getUserName(
                        RpcChannel.readParam(params, 0, "id") ?: "")
            }
            rpc.register("login") { _ ->
                this.login()
            }
            rpc.register("logout") { _ ->
                this.logout()
            }
            rpc.register("send_chat_message") { params ->
                this.sendChatMessage(
                        RpcChannel.readParam(params, 0, "type") ?: 0,
                        RpcChannel.readParam(params, 1, "source") ?: "",
                        RpcChannel.readParam(params, 2, "target") ?: "",
                        RpcChannel.readParam(params, 3, "message") ?: "")
            }
            rpc.register("remove_member") { params ->
                this.removeMember(
                        RpcChannel.readParam(params, 0, "source") ?: "",
                        RpcChannel.readParam(params, 1, "target") ?: "")
            }
            rpc.register("shutup_member") { params ->
                this.shutupMember(
                        RpcChannel.readParam(params, 0, "source") ?: "",
                        RpcChannel.readParam(params, 1, "target") ?: "",
                        RpcChannel.readParam(params, 2, "duration") ?: 0)
            }
            rpc.register("shutup_all_member") { params ->
                this.shutupAllMember(
                        RpcChannel.readParam(params, 0, "source") ?: "",
                        RpcChannel.readParam(params, 1, "switch") ?: false)
            }
            rpc.register("get_member_name") { params ->
                this.getMemberName(
                        RpcChannel.readParam(params, 0, "source") ?: "",
                        RpcChannel.readParam(params, 1, "target") ?: "")
            }
            rpc.register("get_user_avatar") { params ->
                this.getUserAvatar(
                        RpcChannel.readParam(params, 0, "id") ?: "")
            }
            rpc.register("get_self_id") { _ ->
                this.getSelfID()
            }
        }
    }
}