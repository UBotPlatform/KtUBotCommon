package ubot.common

import ubot.common.utils.appendCodePoint

private class ChatMessageParseImpl(private val content: String) {
    val result = ArrayList<ChatMessageEntity>()
    private val data = StringBuilder()
    private var type = ""
    private var start = 0
    private var count = 0
    private var i = 0
    private var args = ArrayList<String>(1)
    private var namedArgs: MutableMap<String, String>? = null
    private var isNamedArg = false
    private var argName = ""

    init {
        parse()
    }

    private fun flushBuf() {
        data.append(content, start, start + count)
        start = i + 1
        count = 0
    }

    private fun finishArg() {
        flushBuf()
        if (isNamedArg) {
            if (namedArgs == null) {
                namedArgs = LinkedHashMap()
            }
            namedArgs!![argName] = data.toString()
        } else {
            args.add(data.toString())
        }
        data.clear()
        argName = ""
        isNamedArg = false
    }

    private fun endText() {
        flushBuf()
        if (data.isNotEmpty()) {
            result.add(ChatMessageEntity(data.toString()))
        }
        data.clear()
    }

    private fun beginEntity(newType: String) {
        endText()
        type = newType
    }

    private fun endEntity() {
        finishArg()
        result.add(ChatMessageEntity(type, args, namedArgs ?: emptyMap()))
        args = ArrayList(1)
        namedArgs = null
    }

    private fun parse() {
        var inBracket = false
        loop@ while (i < content.length) {
            when {
                content[i] == '[' && !inBracket -> {
                    var j = i
                    while (j < content.length && content[j] != ':' && content[j] != ']') {
                        j++
                    }
                    if (j < content.length && content[j] == ':') {
                        val newType = content.substring(i + 1, j)
                        if (ChatMessageEntity.isValidMsgType(newType)) {
                            i = j
                            beginEntity(newType)
                            inBracket = true
                            i++
                            continue@loop
                        }
                    }
                    count++
                    i++
                }
                content[i] == ']' && inBracket -> {
                    endEntity()
                    inBracket = false
                    i++
                }
                content[i] == ',' && inBracket -> {
                    finishArg()
                    i++
                }
                content[i] == '=' && inBracket && !isNamedArg -> {
                    flushBuf()
                    argName = data.toString()
                    isNamedArg = true
                    data.clear()
                    i++
                }
                content[i] == '\\' && i + 1 < content.length -> {
                    i++
                    when {
                        content[i] in "\\[],=" -> {
                            flushBuf()
                            data.append(content[i])
                            i++
                        }
                        content[i] == 'n' -> {
                            flushBuf()
                            data.append('\n')
                            i++
                        }
                        content[i] == 'r' -> {
                            flushBuf()
                            data.append('\r')
                            i++
                        }
                        content[i] == 't' -> {
                            flushBuf()
                            data.append('\t')
                            i++
                        }
                        content[i] == 'u' && i + 1 < content.length -> {
                            if (content[i + 1] == '{') {
                                val pEnd = content.indexOf('}', i + 2)
                                if (pEnd != -1) {
                                    val codepoint = content.substring(i + 2, pEnd).toIntOrNull(16)
                                    if (codepoint != null) {
                                        i = pEnd
                                        flushBuf()
                                        data.appendCodePoint(codepoint)
                                        i++
                                        continue@loop
                                    }
                                }
                            } else {
                                if (i + 4 < content.length) {
                                    val utf16code = content.substring(i + 1, i + 5).toIntOrNull(16)
                                    if (utf16code != null) {
                                        i += 4
                                        flushBuf()
                                        data.append(utf16code.toChar())
                                        i++
                                        continue@loop
                                    }
                                }
                            }
                            count++
                            i++
                        }
                        else -> {
                            count++
                            i++
                        }
                    }
                }
                else -> {
                    count++
                    i++
                }
            }
        }
        endText()
    }
}

object ChatMessageParser {
    fun parse(content: String): List<ChatMessageEntity> =
        ChatMessageParseImpl(content).result
}