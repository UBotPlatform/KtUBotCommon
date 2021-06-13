package ubot.common

data class ChatMessageEntity(
    val type: String,
    val args: List<String> = emptyList(),
    val namedArgs: Map<String, String> = emptyMap()
) {
    /**
     * 创建文本实体
     */
    constructor(content: String) : this("text", listOf(content))

    /**
     * 创建单参数实体
     */
    constructor(type: String, content: String) : this(type, listOf(content))

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