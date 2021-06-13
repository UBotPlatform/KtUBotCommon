package ubot.common

import kotlin.test.Test
import kotlin.test.assertEquals

class ChatMessageParserTest {
    @Test
    fun parseASimpleMessage() {
        val r = ChatMessageParser.parse("""hello, \[go\]\u{1f606}\u303d[at:10000]""").toList()
        assertEquals(listOf(
            ChatMessageEntity("hello, [go]\uD83D\uDE06\u303d"),
            ChatMessageEntity("at", "10000")
        ), r)
    }

    @Test
    fun parseMessageWithArgs() {
        val r = ChatMessageParser.parse("""[image:<url>1\,2\=3,md5=xxx][file:<xxx>,a=b]""").toList()
        assertEquals(listOf(
            ChatMessageEntity("image", listOf("<url>1,2=3"), mapOf("md5" to "xxx")),
            ChatMessageEntity("file", listOf("<xxx>"), mapOf("a" to "b"))
        ), r)
    }
}