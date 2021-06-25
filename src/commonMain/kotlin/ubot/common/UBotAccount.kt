package ubot.common

import com.github.arcticlampyrid.ktjsonrpcpeer.RpcServiceDsl
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
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
            rpc.register("get_group_name", String.serializer(), JsonArray.serializer()) { params ->
                this.getGroupName(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register("get_user_name", String.serializer(), JsonArray.serializer()) { params ->
                this.getUserName(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register("send_chat_message", Unit.serializer(), JsonArray.serializer()) { params ->
                this.sendChatMessage(
                    params.getOrNull(0)?.jsonPrimitive?.int ?: 0,
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(3)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register("remove_member", Unit.serializer(), JsonArray.serializer()) { params ->
                this.removeMember(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register("shutup_member", Unit.serializer(), JsonArray.serializer()) { params ->
                this.shutupMember(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(2)?.jsonPrimitive?.int ?: 0
                )
            }
            rpc.register("shutup_all_member", Unit.serializer(), JsonArray.serializer()) { params ->
                this.shutupAllMember(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.boolean ?: false
                )
            }
            rpc.register("get_member_name", String.serializer(), JsonArray.serializer()) { params ->
                this.getMemberName(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: "",
                    params.getOrNull(1)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register("get_user_avatar", String.serializer(), JsonArray.serializer()) { params ->
                this.getUserAvatar(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: ""
                )
            }
            rpc.register("get_self_id", String.serializer(), Unit.serializer()) { _ ->
                this.getSelfID()
            }
            rpc.register("get_platform_id", String.serializer(), Unit.serializer()) { _ ->
                this.getPlatformID()
            }
            rpc.register("get_group_list", ListSerializer(String.serializer()), Unit.serializer()) { _ ->
                this.getGroupList()
            }
            rpc.register("get_member_list", ListSerializer(String.serializer()), JsonArray.serializer()) { params ->
                this.getMemberList(
                    params.getOrNull(0)?.jsonPrimitive?.content ?: ""
                )
            }
        }
    }
}