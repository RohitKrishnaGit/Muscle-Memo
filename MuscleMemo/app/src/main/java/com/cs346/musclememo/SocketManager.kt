package com.cs346.musclememo

import com.cs346.musclememo.api.RetrofitInterface
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.screens.viewmodels.Sender
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
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
        socket?.off("message")
    }

    fun isConnected(): Boolean {
        return socket?.connected() ?: false
    }

    fun onMessageReceived(listener: (msgId: Int, msg: String, sender: Sender, timestamp: Long) -> Unit) {
        socket?.on("message") { args ->
            val messageId = args[0] as Int
            val message = args[1] as String
            val senderJSON = args[2] as String
            val timestamp = args[3] as Long

            var mJson: JsonElement
            try {
                mJson = JsonParser.parseString(senderJSON)
                val gson = Gson()
                val senderObj: Sender = gson.fromJson(mJson, Sender::class.java);
                listener.invoke(messageId, message, senderObj, timestamp)
            } catch (e: Exception) {
                e.printStackTrace()
            }

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