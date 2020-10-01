package ubot.common

import kotlinx.serialization.json.JsonElement
import twitter.qiqiworld1.ktjsonrpcpeer.RpcChannel

interface UBotAccount {
    suspend fun getGroupName(id: String): String
    suspend fun getUserName(id: String): String
    suspend fun sendChatMessage(type: Int, source: String, target: String, message: String)
    suspend fun removeMember(source: String, target: String)
    suspend fun shutupMember(source: String, target: String, duration: Int)
    suspend fun shutupAllMember(source: String, switch: Boolean)
    suspend fun getMemberName(source: String, target: String): String
    suspend fun getUserAvatar(id: String): String
    suspend fun getSelfID(): String
    suspend fun getPlatformID(): String
    suspend fun getGroupList(): Array<String>
    suspend fun getMemberList(id: String): Array<String>

    companion object {
        fun UBotAccount.applyTo(rpc: RpcChannel) {
            rpc.register<String, JsonElement>("get_group_name") { params ->
                this.getGroupName(
                        RpcChannel.readParam(params, 0, "id") ?: "")
            }
            rpc.register<String, JsonElement>("get_user_name") { params ->
                this.getUserName(
                        RpcChannel.readParam(params, 0, "id") ?: "")
            }
            rpc.register<Unit, JsonElement>("send_chat_message") { params ->
                this.sendChatMessage(
                        RpcChannel.readParam(params, 0, "type") ?: 0,
                        RpcChannel.readParam(params, 1, "source") ?: "",
                        RpcChannel.readParam(params, 2, "target") ?: "",
                        RpcChannel.readParam(params, 3, "message") ?: "")
            }
            rpc.register<Unit, JsonElement>("remove_member") { params ->
                this.removeMember(
                        RpcChannel.readParam(params, 0, "source") ?: "",
                        RpcChannel.readParam(params, 1, "target") ?: "")
            }
            rpc.register<Unit, JsonElement>("shutup_member") { params ->
                this.shutupMember(
                        RpcChannel.readParam(params, 0, "source") ?: "",
                        RpcChannel.readParam(params, 1, "target") ?: "",
                        RpcChannel.readParam(params, 2, "duration") ?: 0)
            }
            rpc.register<Unit, JsonElement>("shutup_all_member") { params ->
                this.shutupAllMember(
                        RpcChannel.readParam(params, 0, "source") ?: "",
                        RpcChannel.readParam(params, 1, "switch") ?: false)
            }
            rpc.register<String, JsonElement>("get_member_name") { params ->
                this.getMemberName(
                        RpcChannel.readParam(params, 0, "source") ?: "",
                        RpcChannel.readParam(params, 1, "target") ?: "")
            }
            rpc.register<String, JsonElement>("get_user_avatar") { params ->
                this.getUserAvatar(
                        RpcChannel.readParam(params, 0, "id") ?: "")
            }
            rpc.register<String, JsonElement>("get_self_id") { _ ->
                this.getSelfID()
            }
            rpc.register<String, JsonElement>("get_platform_id") { _ ->
                this.getPlatformID()
            }
            rpc.register<Array<String>, JsonElement>("get_group_list") { _ ->
                this.getGroupList()
            }
            rpc.register<Array<String>, JsonElement>("get_member_list") { params ->
                this.getMemberList(
                        RpcChannel.readParam(params, 0, "id") ?: "")
            }
        }
    }
}