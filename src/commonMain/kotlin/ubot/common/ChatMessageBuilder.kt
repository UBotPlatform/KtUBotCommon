package ubot.common

import ubot.common.utils.toCodePoint

class ChatMessageBuilder {
    private val result = StringBuilder()
    fun add(content: String): ChatMessageBuilder {
        return add(content, 0, content.length)
    }

    fun add(content: String, start: Int, end: Int): ChatMessageBuilder {
        var chunkStart = start
        var i = start
        loop@ while (i < end) {
            when {
                content[i] in "\\[]" -> {
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
                    result.append(content[i].toInt().toString(16).padStart(4, '0'))
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

    fun add(type: String, data: String, start: Int, end: Int): ChatMessageBuilder {
        if (type == "" || type == "text") {
            return add(data, start, end)
        }
        result.append("[")
        result.append(type)
        result.append(":")
        add(data, start, end)
        result.append("]")
        return this
    }

    fun add(type: String, data: String): ChatMessageBuilder {
        return add(type, data, 0, data.length)
    }

    fun add(entity: ChatMessageEntity): ChatMessageBuilder {
        return add(entity.type, entity.data)
    }


    private fun lowCodePointNeedEscape(codePoint: Char): Boolean {
        val c = codePoint.toInt()
        return c in 0xA9..0xAE || c in 0x200D..0x3299
    }

    fun build(): String {
        return result.toString()
    }

    override fun toString(): String {
        return result.toString()
    }
}