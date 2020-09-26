package ubot.common

import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import ktjsonrpcpeer.RpcChannel

internal class UBotManagerProxy constructor(private val rpc: RpcChannel)
    : UBotManager {
    override suspend fun registerApp(id: String): String {
        return rpc.call("register_app", buildJsonArray {
            add(id)
        })
    }

    override suspend fun registerAccount(id: String): String {
        return rpc.call("register_account", buildJsonArray {
            add(id)
        })
    }
}