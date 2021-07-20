package ubot.common
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmStatic

@Serializable
class UBotEventResult(
    @Serializable(TypeSerializer::class)
    val type: Type,
    val reason: String? = null
) {
    @JvmInline
    value class Type(val type: Int) {
        companion object {
            @JvmStatic
            val Ignore = Type(0)

            @JvmStatic
            val Continue = Type(1)

            @JvmStatic
            val Completed = Type(2)

            @JvmStatic
            val Accept = Type(10)

            @JvmStatic
            val Reject = Type(20)
        }

        inline val isBlocking get() = type and (1.inv()) != 0
        inline val isNonBlocking get() = !isBlocking
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(forClass = Type::class)
    private object TypeSerializer : KSerializer<Type> {
        override fun serialize(encoder: Encoder, value: Type) =
            encoder.encodeInt(value.type)

        override fun deserialize(decoder: Decoder) =
            Type(decoder.decodeInt())

        @OptIn(InternalSerializationApi::class)
        override val descriptor =
            buildSerialDescriptor("UBotEventResult.Type", PolymorphicKind.SEALED)
    }

    companion object {
        @JvmStatic
        val Ignore = UBotEventResult(Type.Ignore)

        @JvmStatic
        val Continue = UBotEventResult(Type.Continue)

        @JvmStatic
        val Completed = UBotEventResult(Type.Completed)

        @JvmStatic
        val Accept = UBotEventResult(Type.Accept)

        @JvmStatic
        val Reject = UBotEventResult(Type.Reject)

        @Suppress("NOTHING_TO_INLINE", "FunctionName")
        @JvmStatic
        inline fun Accept(reason: String?) = UBotEventResult(Type.Accept, reason)

        @Suppress("NOTHING_TO_INLINE", "FunctionName")
        @JvmStatic
        inline fun Reject(reason: String?) = UBotEventResult(Type.Reject, reason)
    }
}