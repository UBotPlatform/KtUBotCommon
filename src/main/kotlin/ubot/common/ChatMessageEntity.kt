package ubot.common

data class ChatMessageEntity(val type: String, val data: String) {
    constructor(data: String) : this("text", data)

    companion object {
        val typePattern = Regex("^[a-z0-9_]+$")
    }

    init {
        if (!typePattern.matches(type)) {
            throw IllegalArgumentException("invaild entity type")
        }
    }
}