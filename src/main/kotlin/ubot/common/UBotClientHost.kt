package ubot.common

import ktjsonrpcpeer.RpcChannel
import ktjsonrpcpeer.RpcOkWebsocketOnceAdapter
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import ubot.common.UBotAccount.Companion.applyTo
import ubot.common.UBotApp.Companion.applyTo


object UBotClientHost {
    private suspend fun dialRouter(op: String,
                                   urlStr: String,
                                   registerClient: suspend (managerUrl: HttpUrl, manager: UBotManager) -> HttpUrl)
            : Pair<WebSocket, RpcChannel> {
        val okClient = OkHttpClient()
        val clientUrl: HttpUrl

        //okhttp always requires http/https, though it's actually a WebSocket connection
        val finalUrlStr: String = when {
            urlStr.startsWith("ws:", ignoreCase = true) ->
                "http:${urlStr.substring(3)}"
            urlStr.startsWith("wss:", ignoreCase = true) ->
                "https:${urlStr.substring(4)}"
            else ->
                urlStr
        }
        when (op.toLowerCase()) {
            "ApplyTo".toLowerCase() -> {
                var managerUrl = finalUrlStr.toHttpUrl()
                if (managerUrl.username != "" || managerUrl.password != "") {
                    val user = managerUrl.username
                    val password = managerUrl.password
                    val getTokenUrl = managerUrl.newBuilder()
                            .username("")
                            .password("")
                            .encodedPath("/api/manager/get_token")
                            .build()
                    val managerToken = okClient.newCall(Request.Builder()
                            .url(getTokenUrl)
                            .post(FormBody.Builder()
                                    .add("user", user)
                                    .add("password", password)
                                    .build())
                            .build())
                            .execute().body!!.string()
                    managerUrl = managerUrl.newBuilder()
                            .username("")
                            .password("")
                            .setQueryParameter("token", managerToken)
                            .build()
                }
                val managerRequest: Request = Request.Builder()
                        .url(managerUrl)
                        .build()
                val managerRpcAdapter = RpcOkWebsocketOnceAdapter()
                val managerConn = okClient.newWebSocket(managerRequest, managerRpcAdapter)
                try {
                    val managerRpcChannel = RpcChannel(managerRpcAdapter)
                    clientUrl = registerClient(managerUrl, UBotManager.of(managerRpcChannel))
                } finally {
                    managerConn.close(1000, null)
                }
            }
            "Connect".toLowerCase() -> {
                clientUrl = finalUrlStr.toHttpUrl()
            }
            else -> {
                throw IllegalArgumentException("invalid op")
            }
        }
        val rpcAdapter = RpcOkWebsocketOnceAdapter()
        val request: Request = Request.Builder()
                .url(clientUrl)
                .build()
        val conn = okClient.newWebSocket(request, rpcAdapter)
        val rpcChannel = RpcChannel(rpcAdapter)
        return Pair(conn, rpcChannel)
    }

    private suspend fun host(op: String, urlStr: String,
                             registerClient: suspend (managerUrl: HttpUrl, manager: UBotManager) -> HttpUrl,
                             startup: suspend (rpc: RpcChannel) -> Unit) {
        var channel: Pair<WebSocket, RpcChannel>? = null
        for (retryCount in 1..5) {
            try {
                channel = dialRouter(op, urlStr, registerClient)
                break
            } catch (e: Exception) {
                println("Failed to connect to UBot Router, it will try again in 5 seconds.")
                e.printStackTrace()
            }
        }
        if (channel == null) {
            throw Exception("Failed to connect to UBot Router after 5 attempts.")
        }
        println("Connection established")
        startup(channel.second)
        channel.second.completion.await()
        try {
            channel.first.close(1000, null)
        } catch (e: Exception) {

        }
    }

    suspend fun hostApp(op: String, urlStr: String, id: String, load: suspend (appApi: UBotAppApi) -> UBotApp) {
        host(op, urlStr, { managerUrl, manager ->
            managerUrl.newBuilder()
                    .encodedPath("/api/app")
                    .setQueryParameter("id", id)
                    .setQueryParameter("token", manager.registerApp(id))
                    .build()
        }, { rpc ->
            load(UBotAppApi.of(rpc)).applyTo(rpc)
        })
    }

    suspend fun hostAccount(op: String, urlStr: String, id: String, load: suspend (e: UBotAccountEventEmitter) -> UBotAccount) {
        host(op, urlStr, { managerUrl, manager ->
            managerUrl.newBuilder()
                    .encodedPath("/api/account")
                    .setQueryParameter("id", id)
                    .setQueryParameter("token", manager.registerAccount(id))
                    .build()
        }, { rpc ->
            load(UBotAccountEventEmitter.of(rpc)).applyTo(rpc)
        })
    }
}
