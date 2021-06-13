package ubot.common

import kotlin.test.Test
import kotlin.test.assertEquals

class ChatMessageBuilderTest {
    @Test
    fun buildASimpleMessage() {
        val builder = ChatMessageBuilder()
        builder.add("hello, [go]")
        builder.add("\ud83d\ude06")
        builder.add("\u25b6")
        builder.add("at", "10000")
        val r = builder.build()
        assertEquals("""hello, \[go\]\u{1f606}\u25b6[at:10000]""", r)
    }
}