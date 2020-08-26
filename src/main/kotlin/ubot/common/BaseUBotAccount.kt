package ubot.common

open class BaseUBotAccount : UBotAccount {
    override suspend fun getGroupName(id: String): String {
        return "Nameless Group"
    }

    override suspend fun getUserName(id: String): String {
        return "Nameless User"
    }

    override suspend fun login() {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override suspend fun sendChatMessage(type: Int, source: String, target: String, message: String) {
        TODO("Not yet implemented")
    }

    override suspend fun removeMember(source: String, target: String) {
        TODO("Not yet implemented")
    }

    override suspend fun shutupMember(source: String, target: String, duration: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun shutupAllMember(source: String, switch: Boolean) {
        TODO("Not yet implemented")
    }

    override suspend fun getMemberName(source: String, target: String): String {
        return getUserName(target) //fallback
    }

    override suspend fun getUserAvatar(id: String): String {
        TODO("Not yet implemented")
    }

    override suspend fun getSelfID(): String {
        TODO("Not yet implemented")
    }

    override suspend fun getPlatformID(): String {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupList(): Array<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getMemberList(id: String): Array<String> {
        TODO("Not yet implemented")
    }
}