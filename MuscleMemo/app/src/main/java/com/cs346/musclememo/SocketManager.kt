package com.cs346.musclememo

import com.cs346.musclememo.api.RetrofitInterface
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketManager {
    private var socket: Socket? = null

    init {
        try {
            socket = IO.socket((object : RetrofitInterface {}).baseUrl)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    fun connect() {
        socket?.connect()
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun isConnected(): Boolean {
        return socket?.connected() ?: false
    }

    fun onMessageReceived(listener: (String) -> Unit) {
        socket?.on("message") { args ->
            val message = args[0] as String
            listener.invoke(message)
        }
    }

    fun onHistoryRequest(listener: (String) -> Unit) {
        socket?.on("history") { args ->
            val message = args[0] as String
            listener.invoke(message)
            /* use this to fetch history using "all" endpoint & display
            * see example in ChatTestScreen for onMessageReceived */
        }
    }

    fun joinRoom(roomId: String, refreshToken: String) {
        socket?.emit("join", roomId, refreshToken);
    }

    fun sendMessage(message: String) {
        socket?.emit("message", message)
    }
}