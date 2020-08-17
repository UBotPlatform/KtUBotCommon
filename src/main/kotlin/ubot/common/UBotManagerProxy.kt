package ubot.common

import ktjsonrpcpeer.RpcChannel

internal class UBotManagerProxy constructor(private val rpc: RpcChannel)
    : UBotManager {
    override suspend fun registerApp(id: String): String {
        return rpc.call("register_app", arrayOf(id))
    }

    override suspend fun registerAccount(id: String): String {
        return rpc.call("register_account", arrayOf(id))
    }
}