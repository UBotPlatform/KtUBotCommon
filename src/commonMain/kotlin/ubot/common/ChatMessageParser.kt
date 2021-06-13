package ubot.common

import ubot.common.utils.appendCodePoint

object ChatMessageParser {
    fun parse(content: String): Sequence<ChatMessageEntity> {
        return sequence {
            val data = StringBuilder()
            var inBracket = false
            var type = ""
            var start = 0
            var count = 0
            var i = 0
            var args = ArrayList<String>(1)
            var namedArgs: MutableMap<String, String>? = null
            var isNamedArg = false
            var argName = ""
            fun flushBuf() {
                data.append(content, start, start + count)
                start = i + 1
                count = 0
            }

            fun finishArg() {
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

            suspend fun SequenceScope<ChatMessageEntity>.endText() {
                flushBuf()
                if (data.isNotEmpty()) {
                    yield(ChatMessageEntity(data.toString()))
                }
                data.clear()
            }

            suspend fun SequenceScope<ChatMessageEntity>.beginEntity(newType: String) {
                endText()
                type = newType
            }

            suspend fun SequenceScope<ChatMessageEntity>.endEntity() {
                finishArg()
                yield(ChatMessageEntity(type, args, namedArgs ?: emptyMap()))
                args = ArrayList(1)
                namedArgs = null
            }
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
}