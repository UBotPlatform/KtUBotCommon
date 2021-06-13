package ubot.common

import ubot.common.utils.toCodePoint

class ChatMessageBuilder {
    companion object {
        private const val OP_CHARS_NORMAL = "\\[]"
        private const val OP_CHARS_ARGS = "\\[],="
    }

    private val result = StringBuilder()

    private fun internalAddString(content: String, start: Int, end: Int, opChars: String): ChatMessageBuilder {
        var chunkStart = start
        var i = start
        loop@ while (i < end) {
            when {
                content[i] in opChars -> {
                    result.append(content, chunkStart, i)
                    result.append("\\")
                    result.append(content[i])
                    i++
                    chunkStart = i
                }
                content[i].isHighSurrogate() -> {
                    if (i + 1 >= content.length || !content[i + 1].isLowSurrogate()) {
                        result.append(content, chunkStart, i)
                        result.append("\uFFFD")
                        i++
                        chunkStart = i
                    } else {
                        result.append(content, chunkStart, i)
                        result.append("\\u{")
                        result.append(Char.toCodePoint(content[i], content[i + 1]).toString(16))
                        result.append("}")
                        i += 2
                        chunkStart = i
                    }
                }
                content[i].isLowSurrogate() -> {
                    result.append(content, chunkStart, i)
                    result.append("\uFFFD")
                    i++
                    chunkStart = i
                }
                lowCodePointNeedEscape(content[i]) -> {
                    result.append(content, chunkStart, i)
                    result.append("\\u")
                    result.append(content[i].code.toString(16).padStart(4, '0'))
                    i++
                    chunkStart = i
                }
                else -> {
                    i++
                }
            }
        }
        result.append(content, chunkStart, i)
        return this
    }

    private fun internalAddString(content: String, opChars: String): ChatMessageBuilder {
        return internalAddString(content, 0, content.length, opChars)
    }

    /**
     * 添加文本
     */
    fun add(content: String): ChatMessageBuilder {
        return add(content, 0, content.length)
    }

    /**
     * 添加 [content] 中从 [start] 到 [end] 部分的文本
     */
    fun add(content: String, start: Int, end: Int): ChatMessageBuilder {
        return internalAddString(content, start, end, OP_CHARS_NORMAL)
    }

    /**
     * 添加单参数实体，类型由 [type] 指定，参数为 [content] 中从 [start] 到 [end] 部分的数据
     */
    fun add(type: String, content: String, start: Int, end: Int): ChatMessageBuilder {
        if (type == "" || type == "text") {
            return add(content, start, end)
        }
        result.append("[")
        result.append(type)
        result.append(":")
        internalAddString(content, start, end, OP_CHARS_ARGS)
        result.append("]")
        return this
    }

    /**
     * 添加单参数实体，类型由 [type] 指定，参数为 [content]
     */
    fun add(type: String, content: String): ChatMessageBuilder {
        return add(type, content, 0, content.length)
    }

    /**
     * 添加指定实体
     */
    fun add(entity: ChatMessageEntity): ChatMessageBuilder {
        if (entity.type == "" || entity.type == "text") {
            return add(entity.args.firstOrNull() ?: "")
        }
        result.append("[")
        result.append(entity.type)
        result.append(":")
        val argIterator = entity.args.iterator()
        if (argIterator.hasNext()) {
            internalAddString(argIterator.next(), OP_CHARS_ARGS)
            while (argIterator.hasNext()) {
                result.append(',')
                internalAddString(argIterator.next(), OP_CHARS_ARGS)
            }
            entity.namedArgs.entries.forEach {
                result.append(',')
                internalAddString(it.key, OP_CHARS_ARGS)
                result.append('=')
                internalAddString(it.value, OP_CHARS_ARGS)
            }
        } else {
            val namedArgIterator = entity.namedArgs.entries.iterator()
            if (namedArgIterator.hasNext()) {
                namedArgIterator.next().let {
                    internalAddString(it.key, OP_CHARS_ARGS)
                    result.append('=')
                    internalAddString(it.value, OP_CHARS_ARGS)
                }
                while (namedArgIterator.hasNext()) {
                    namedArgIterator.next().let {
                        result.append(',')
                        internalAddString(it.key, OP_CHARS_ARGS)
                        result.append('=')
                        internalAddString(it.value, OP_CHARS_ARGS)
                    }
                }
            }
        }
        result.append("]")
        return this
    }

    private fun lowCodePointNeedEscape(codePoint: Char): Boolean {
        return when (codePoint) {
            in '\u00A9'..'\u00AE', in '\u2580'..'\u2FFF' -> true
            else -> false
        }
    }

    fun build(): String {
        return result.toString()
    }

    override fun toString(): String {
        return result.toString()
    }
}