package ubot.common

import kotlin.test.Test
import kotlin.test.assertEquals

class ChatMessageParserTest {
    @Test
    fun parseASimpleMessage() {
        val r = ChatMessageParser.Parse("""hello, \[go\]\u{1f606}\u303d[at:10000]""").toList()
        assertEquals(ChatMessageEntity("hello, [go]\uD83D\uDE06\u303d"), r[0])
        assertEquals(ChatMessageEntity("at", "10000"), r[1])
    }
}