package ubot.common

import com.github.arcticlampyrid.ktjsonrpcpeer.RpcChannel
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

internal class UBotAppApiProxy constructor(private val rpc: RpcChannel) : UBotAppApi {
    override suspend fun getGroupName(bot: String, id: String): String {
        return rpc.call("get_group_name", buildJsonArray {
            add(bot)
            add(id)
        })
    }

    override suspend fun getUserName(bot: String, id: String): String {
        return rpc.call("get_user_name", buildJsonArray {
            add(bot)
            add(id)
        })
    }

    override suspend fun sendChatMessage(bot: String, type: Int, source: String, target: String, message: String) {
        return rpc.call("send_chat_message", buildJsonArray {
            add(bot)
            add(type)
            add(source)
            add(target)
            add(message)
        })
    }

    override suspend fun removeMember(bot: String, source: String, target: String) {
        return rpc.call("remove_member", buildJsonArray {
            add(bot)
            add(source)
            add(target)
        })
    }

    override suspend fun shutupMember(bot: String, source: String, target: String, duration: Int) {
        return rpc.call("shutup_member", buildJsonArray {
            add(bot)
            add(source)
            add(target)
            add(duration)
        })
    }

    override suspend fun shutupAllMember(bot: String, source: String, switch: Boolean) {
        return rpc.call("shutup_all_member", buildJsonArray {
            add(bot)
            add(source)
            add(switch)
        })
    }

    override suspend fun getMemberName(bot: String, source: String, target: String): String {
        return rpc.call("get_member_name", buildJsonArray {
            add(bot)
            add(source)
            add(target)
        })
    }

    override suspend fun getUserAvatar(bot: String, id: String): String {
        return rpc.call("get_user_avatar", buildJsonArray {
            add(bot)
            add(id)
        })
    }

    override suspend fun getSelfID(bot: String): String {
        return rpc.call("get_self_id", buildJsonArray {
            add(bot)
        })
    }

    override suspend fun getPlatformID(bot: String): String {
        return rpc.call("get_platform_id", buildJsonArray {
            add(bot)
        })
    }

    override suspend fun getGroupList(bot: String): Array<String> {
        return rpc.call("get_group_list", buildJsonArray {
            add(bot)
        })
    }

    override suspend fun getMemberList(bot: String, id: String): Array<String> {
        return rpc.call("get_member_list", buildJsonArray {
            add(bot)
            add(id)
        })
    }
}