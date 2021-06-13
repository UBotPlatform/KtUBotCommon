package ubot.common

data class ChatMessageEntity(val type: String, val data: String) {
    constructor(data: String) : this("text", data)

    companion object {
        fun isValidMsgType(type: String): Boolean {
            for (x in type) when (x) {
                in 'a'..'z', in '0'..'9', '_', '.' -> Unit
                else -> return false
            }
            return true
        }
    }

    init {
        require(isValidMsgType(type)) {
            "invalid entity type"
        }
    }
}