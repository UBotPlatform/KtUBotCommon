package ubot.common

import com.github.arcticlampyrid.ktjsonrpcpeer.RpcServiceDsl
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive

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
    suspend fun getGroupList(): List<String>
    suspend fun getMemberList(id: String): List<String>

    companion object {
        fun UBotAccount.applyTo(rpc: RpcServiceDsl) {
            rpc.register<String, JsonArray>("get_group_name") { params ->
                this.getGroupName(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register<String, JsonArray>("get_user_name") { params ->
                this.getUserName(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register<Unit, JsonArray>("send_chat_message") { params ->
                this.sendChatMessage(
                    params.getOrNull(0)?.jsonPrimitive?.int ?: 0,
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(3)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register<Unit, JsonArray>("remove_member") { params ->
                this.removeMember(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register<Unit, JsonArray>("shutup_member") { params ->
                this.shutupMember(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.int ?: 0
                )
            }
            rpc.register<Unit, JsonArray>("shutup_all_member") { params ->
                this.shutupAllMember(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.boolean ?: false
                )
            }
            rpc.register<String, JsonArray>("get_member_name") { params ->
                this.getMemberName(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register<String, JsonArray>("get_user_avatar") { params ->
                this.getUserAvatar(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register<String, Unit>("get_self_id") { _ ->
                this.getSelfID()
            }
            rpc.register<String, Unit>("get_platform_id") { _ ->
                this.getPlatformID()
            }
            rpc.register<List<String>, Unit>("get_group_list") { _ ->
                this.getGroupList()
            }
            rpc.register<List<String>, JsonArray>("get_member_list") { params ->
                this.getMemberList(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: ""
                )
            }
        }
    }
}