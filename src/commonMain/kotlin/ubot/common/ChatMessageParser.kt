package ubot.common

import ubot.common.utils.appendCodePoint

object ChatMessageParser {
    fun Parse(content: String): Sequence<ChatMessageEntity> {
        return sequence {
            val data = StringBuilder()
            var inBracket = false
            var type = "text"
            var start = 0
            var count = 0
            var i = 0
            val flushBuf = {
                data.append(content, start, start + count)
                start = i + 1
                count = 0
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
                            if (ChatMessageEntity.typePattern.matches(newType)) {
                                i = j
                                flushBuf()
                                if (data.isNotEmpty() || type != "text") {
                                    yield(ChatMessageEntity(type, data.toString()))
                                }
                                data.clear()
                                type = newType
                                inBracket = true
                                i++
                                continue@loop
                            }
                        }
                        count++
                        i++
                    }
                    content[i] == ']' && inBracket -> {
                        flushBuf()
                        if (data.isNotEmpty() || type != "text") {
                            yield(ChatMessageEntity(type, data.toString()))
                        }
                        data.clear()
                        type = "text"
                        inBracket = false
                        i++
                    }
                    content[i] == '\\' && i + 1 < content.length -> {
                        i++
                        when {
                            content[i] in "\\[]" -> {
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
            if (count != 0) {
                data.append(content, start, start + count)
            }
            if (data.isNotEmpty() || type != "text") {
                yield(ChatMessageEntity(type, data.toString()))
            }
        }
    }
}