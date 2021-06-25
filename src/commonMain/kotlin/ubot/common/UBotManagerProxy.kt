package ubot.common

import com.github.arcticlampyrid.ktjsonrpcpeer.RpcChannel
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray

internal class UBotManagerProxy constructor(private val rpc: RpcChannel) : UBotManager {
    override suspend fun registerApp(id: String): String {
        return rpc.call("register_app", String.serializer(), JsonArray.serializer(), buildJsonArray {
            add(id)
        })
    }

    override suspend fun registerAccount(id: String): String {
        return rpc.call("register_account", String.serializer(), JsonArray.serializer(), buildJsonArray {
            add(id)
        })
    }
}