package ubot.common

import com.github.arcticlampyrid.ktjsonrpcpeer.RpcChannel
import com.github.arcticlampyrid.ktjsonrpcpeer.RpcKtorWebSocketAdapter
import com.github.arcticlampyrid.ktjsonrpcpeer.RpcServiceDefiner
import com.github.arcticlampyrid.ktjsonrpcpeer.build
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import io.ktor.websocket.*
import mu.KotlinLogging
import ubot.common.UBotAccount.Companion.applyTo
import ubot.common.UBotApp.Companion.applyTo
import kotlin.coroutines.coroutineContext


object UBotClientHost {
    private val logger = KotlinLogging.logger("UBotClientHost")
    private suspend fun dialRouter(
        httpClient: HttpClient,
        op: String,
        urlStr: String,
        registerClient: suspend (managerUrl: Url, manager: UBotManager) -> Url
    ): Pair<WebSocketSession, RpcChannel> {
        val urlParam = URLBuilder().takeFrom(urlStr).build()
        val clientUrl: Url
        when (op.lowercase()) {
            "ApplyTo".lowercase() -> {
                val managerUrl: Url
                val tls = when (urlParam.protocol) {
                    URLProtocol.WSS -> true
                    URLProtocol.HTTPS -> true
                    else -> false
                }
                if (urlParam.user != null || urlParam.password != null) {
                    val user = urlParam.user ?: ""
                    val password = urlParam.password ?: ""
                    val getTokenUrl = URLBuilder(
                        if (tls)
                            URLProtocol.HTTPS
                        else
                            URLProtocol.HTTP,
                        urlParam.host,
                        urlParam.port,
                        null,
                        null,
                        listOf("api", "manager" , "get_token"),
                        urlParam.parameters,
                        urlParam.fragment,
                        urlParam.trailingQuery
                    ).build()
                    val managerToken = httpClient.post(getTokenUrl) {
                        FormDataContent(Parameters.build {
                            append("user", user)
                            append("password", password)
                        }).let(::setBody)
                    }.bodyAsText()
                    logger.trace { "Got manager token: $managerToken" }
                    managerUrl = URLBuilder(
                        if (tls)
                            URLProtocol.WSS
                        else
                            URLProtocol.WS,
                        urlParam.host,
                        urlParam.port,
                        null,
                        null,
                        urlParam.pathSegments,
                        Parameters.build {
                            appendAll(urlParam.parameters)
                            append("token", managerToken)
                        },
                        urlParam.fragment,
                        urlParam.trailingQuery
                    ).build()
                } else {
                    managerUrl = urlParam
                }
                logger.trace { "Applying to $managerUrl" }
                val managerConn = httpClient.webSocketSession {
                    url(managerUrl)
                }
                val managerRpcAdapter = RpcKtorWebSocketAdapter(managerConn)
                var managerRpcChannel: RpcChannel? = null
                try {
                    managerRpcChannel = RpcChannel(managerRpcAdapter, parentCoroutineContext = coroutineContext)
                    clientUrl = registerClient(managerUrl, UBotManager.of(managerRpcChannel))
                } finally {
                    managerRpcChannel?.cancelAndJoin()
                    managerConn.close()
                }
            }
            "Connect".lowercase() -> {
                clientUrl = urlParam
            }
            else -> {
                throw IllegalArgumentException("invalid op")
            }
        }
        logger.trace { "Connecting to $clientUrl" }
        val conn = httpClient.webSocketSession {
            url(clientUrl)
        }
        val rpcAdapter = RpcKtorWebSocketAdapter(conn)
        val rpcChannel = RpcChannel(rpcAdapter, parentCoroutineContext = coroutineContext)
        return Pair(conn, rpcChannel)
    }

    private suspend fun host(
        op: String, urlStr: String,
        registerClient: suspend (managerUrl: Url, manager: UBotManager) -> Url,
        startup: suspend (rpc: RpcChannel) -> Unit
    ) {
        HttpClient {
            install(WebSockets)
        }.use { httpClient ->
            var channel: Pair<WebSocketSession, RpcChannel>? = null
            for (retryCount in 1..5) {
                try {
                    channel = dialRouter(httpClient, op, urlStr, registerClient)
                    break
                } catch (e: Throwable) {
                    logger.error(e) {
                        "Failed to connect to UBot Router, " +
                                "it will try again in 5 seconds. ($retryCount)"
                    }
                }
            }
            check(channel != null) {
                "Failed to connect to UBot Router after 5 attempts."
            }
            logger.info { "Connected to UBot Router" }
            try {
                startup(channel.second)
                channel.second.join()
            } finally {
                channel.first.close()
            }
        }
    }

    suspend fun hostApp(op: String, urlStr: String, id: String, load: suspend (appApi: UBotAppApi) -> UBotApp) {
        host(op, urlStr, { urlParam, manager ->
            URLBuilder(
                urlParam.protocol,
                urlParam.host,
                urlParam.port,
                urlParam.user,
                urlParam.password,
                listOf("api", "app"),
                Parameters.build {
                    append("id", id)
                    append("token", manager.registerApp(id))
                },
                urlParam.fragment,
                urlParam.trailingQuery
            ).build()
        }, { rpc ->
            val impl = load(UBotAppApi.of(rpc))
            rpc.service = RpcServiceDefiner {
                impl.applyTo(this)
            }.build()
        })
    }

    suspend fun hostAccount(
        op: String,
        urlStr: String,
        id: String,
        load: suspend (e: UBotAccountEventEmitter) -> UBotAccount
    ) {
        host(op, urlStr, { urlParam, manager ->
            URLBuilder(
                urlParam.protocol,
                urlParam.host,
                urlParam.port,
                urlParam.user,
                urlParam.password,
                listOf("api", "account"),
                Parameters.build {
                    append("id", id)
                    append("token", manager.registerAccount(id))
                },
                urlParam.fragment,
                urlParam.trailingQuery
            ).build()
        }, { rpc ->
            val impl = load(UBotAccountEventEmitter.of(rpc))
            rpc.service = RpcServiceDefiner {
                impl.applyTo(this)
            }.build()
        })
    }
}
