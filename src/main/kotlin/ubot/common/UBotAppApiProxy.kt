package ubot.common

import ktjsonrpcpeer.RpcChannel

internal class UBotAppApiProxy constructor(private val rpc: RpcChannel)
    : UBotAppApi {
    override suspend fun getGroupName(bot: String, id: String): String {
        return rpc.call("get_group_name", arrayOf(bot, id))
    }

    override suspend fun getUserName(bot: String, id: String): String {
        return rpc.call("get_user_name", arrayOf(bot, id))
    }

    override suspend fun sendChatMessage(bot: String, type: Int, source: String, target: String, message: String) {
        return rpc.call("send_chat_message", arrayOf(bot, type, source, target, message))
    }

    override suspend fun removeMember(bot: String, source: String, target: String) {
        return rpc.call("remove_member", arrayOf(bot, source, target))
    }

    override suspend fun shutupMember(bot: String, source: String, target: String, duration: Int) {
        return rpc.call("shutup_member", arrayOf(bot, source, target, duration))
    }

    override suspend fun shutupAllMember(bot: String, source: String, switch: Boolean) {
        return rpc.call("shutup_all_member", arrayOf(bot, source, switch))
    }

    override suspend fun getMemberName(bot: String, source: String, target: String): String {
        return rpc.call("get_member_name", arrayOf(bot, source, target))
    }

    override suspend fun getUserAvatar(bot: String, id: String): String {
        return rpc.call("get_user_avatar", arrayOf(bot, id))
    }

    override suspend fun getSelfID(bot: String): String {
        return rpc.call("get_self_id", arrayOf(bot))
    }

    override suspend fun getPlatformID(bot: String): String {
        return rpc.call("get_platform_id", arrayOf(bot))
    }

    override suspend fun getGroupList(bot: String): Array<String> {
        return rpc.call("get_group_list", arrayOf(bot))
    }

    override suspend fun getMemberList(bot: String, id: String): Array<String> {
        return rpc.call("get_member_list", arrayOf(bot, id))
    }
}