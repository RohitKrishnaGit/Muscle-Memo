package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.SocketManager
import com.cs346.musclememo.api.RetrofitInstance
import com.cs346.musclememo.api.services.ReportUserAction
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.api.types.parseErrorBody
import com.cs346.musclememo.classes.Friend
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class Message(
    val id: Double,
    val roomId: Int,
    val message: String,
    val sender: Sender
)

data class Sender(
    val id: Double,
    val username: String,
    val email: String,
    val gender: String,
    val experience: String,
    val firebaseTokens: Any,
    val profilePicture: String?
)

class FriendsScreenViewModel : ViewModel() {
    val sm = SocketManager()
    private val _friends = mutableStateListOf<Friend>()
    private val _incomingRequests = mutableStateListOf<Friend>()
    var currentUser: User? by mutableStateOf(null)

    val friends: List<Friend> = _friends
    val incomingRequests: List<Friend> = _incomingRequests

    init {
        getIncomingFriendRequests()
        getFriendByUserId()
        getCurrentUser()
    }

    private val _receivedMessages = mutableStateListOf<Message>()

    val receivedMessages: List<Message> get() = _receivedMessages

    var showAddFriendDialog by mutableStateOf(false)
        private set

    var showReportFriendDialog by mutableStateOf(false)
        private set

    var dialogErrorMessage by mutableStateOf("")
        private set

    var dialogSuccessMessage by mutableStateOf("")
        private set

    var addFriendCode by mutableStateOf("")

    var reportUserReason by mutableStateOf("")

    var currentMessage by mutableStateOf("")
        private set

    var addFriendVisible by mutableStateOf(false)
        private set

    var selectedFriend by mutableStateOf<User?>(null)
        private set

    var friendProfileVisible by mutableStateOf(false)
        private set

    var friendChatVisible by mutableStateOf(false)
        private set

    fun updateCurrentMessage(message: String) {
        currentMessage = message
    }

    fun updateShowAddFriendDialog(visible: Boolean) {
        showAddFriendDialog = visible
    }

    fun updateShowReportFriendDialog(visible: Boolean) {
        showReportFriendDialog = visible
    }

    fun updateDialogErrorMessage(message: String) {
        dialogErrorMessage = message
    }

    fun updateDialogSuccessMessage(message: String) {
        dialogSuccessMessage = message
    }

    fun updateAddFriendCode(code: String) {
        addFriendCode = code
    }

    fun setAddFriendScreenVisible(visible: Boolean) {
        addFriendVisible = visible
    }

    fun updateFriendProfileVisible(visible: Boolean) {
        friendProfileVisible = visible
    }

    fun updateFriendChatVisible(visible: Boolean) {
        friendChatVisible = visible
    }

    fun updateReceivedMessages(messages: List<Message>) {
        _receivedMessages.clear()
        _receivedMessages.addAll(messages)
    }

    fun getChatHistory(roomId: String) {
        val apiService = RetrofitInstance.friendService
        val call = apiService.getChat(roomId)

        call.enqueue(object : Callback<ApiResponse<List<Message>>> {
            override fun onResponse(call: Call<ApiResponse<List<Message>>>, response: Response<ApiResponse<List<Message>>>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { messages ->
                        updateReceivedMessages(messages)
                    }
                    println(receivedMessages)
                    println(response.body())
                } else {
                    println("Failed to get chat history: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Message>>>, t: Throwable) {
                println("Failed to get chat history: ${t.message}")
            }
        })
    }

    fun connectSocket(roomId: String) {
        sm.connect()
        sm.joinRoom(roomId, AppPreferences.refreshToken.toString())
        sm.onMessageReceived { msg ->
            println(msg)
        }
        sm.onHistoryRequest { getChatHistory(roomId) }
    }

    fun selectFriend(friendId: Int, onComplete: (Boolean) -> Unit) {
        val apiService = RetrofitInstance.friendService
        val call = apiService.getFriendById(friendId)

        call.enqueue(object : Callback<ApiResponse<User>> {
            override fun onResponse(call: Call<ApiResponse<User>>, response: Response<ApiResponse<User>>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { friend ->
                        selectedFriend = friend
                        onComplete(true)
                    } ?: run {
                        onComplete(false)
                    }
                } else {
                    // Handle error response
                    onComplete(false)
                }
            }

            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                // Handle call failure
                onComplete(false)
            }
        })
    }

    fun selectFriendProfile(friendId: Int) {
        selectFriend(friendId) { success ->
            updateFriendProfileVisible(success)
        }
    }

    fun selectFriendChat(friendId: Int) {
        selectFriend(friendId) { success ->
            updateFriendChatVisible(success)
        }
    }

    fun getIncomingFriendRequests() {
        val apiService = RetrofitInstance.friendService
        val call = apiService.getIncomingFriendRequestsByUserId()

        call.enqueue(object : Callback<ApiResponse<List<Friend>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Friend>>>,
                response: Response<ApiResponse<List<Friend>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        _incomingRequests.clear()
                        _incomingRequests.addAll(it)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Friend>>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }

    fun generateRoomId(userId: Int, friendId: Int): String {
        val minId = minOf(userId, friendId)
        val maxId = maxOf(userId, friendId)
        return "$minId$maxId"
    }

    fun getCurrentUser() {
        val apiService = RetrofitInstance.userService
        val call = apiService.getMyUser()

        call.enqueue(object : Callback<ApiResponse<User>> {
            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                if (response.isSuccessful){
                    val fetchedUser = response.body()?.data
                    fetchedUser?.let{
                        currentUser = it
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun getFriendByUserId() {
        val apiService = RetrofitInstance.friendService
        val call = apiService.getFriendByUserId()

        call.enqueue(object : Callback<ApiResponse<List<Friend>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<Friend>>>,
                response: Response<ApiResponse<List<Friend>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        for (friend in it) {
                            println(friend)
                            _friends.add(friend)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Friend>>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }

    fun removeFriend(friendId: Int) {
        data class FriendRequestAction(val friendId: Int)

        val apiService = RetrofitInstance.friendService
        val call = apiService.removeFriendByUserId(FriendRequestAction(friendId))

        call.enqueue(object : Callback<ApiResponse<Void>> {
            override fun onResponse(
                call: Call<ApiResponse<Void>>,
                response: Response<ApiResponse<Void>>
            ) {
                if (response.isSuccessful) {
                    _friends.removeAll { it.id == friendId }
                }
            }

            override fun onFailure(call: Call<ApiResponse<Void>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }

    fun acceptFriendRequest(friendId: Int) {
        data class FriendRequestAction(val friendId: Int)

        val apiService = RetrofitInstance.friendService
        val call = apiService.acceptFriendRequest(FriendRequestAction(friendId))

        call.enqueue(object : Callback<ApiResponse<List<Friend>>> {
            override fun onResponse(call: Call<ApiResponse<List<Friend>>>, response: Response<ApiResponse<List<Friend>>>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { newFriends ->
                        // Remove from incoming requests
                        _incomingRequests.removeAll { it.id == friendId }
                        // Add to friends list
                        _friends.addAll(newFriends)
                        println("Friend request accepted: $friendId")
                    }
                } else {
                    println("Failed to accept friend request: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Friend>>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }

    fun rejectFriendRequest(friendId: Int) {
        data class FriendRequestAction(val friendId: Int)

        val apiService = RetrofitInstance.friendService
        val call = apiService.rejectFriendRequest(FriendRequestAction(friendId))

        call.enqueue(object : Callback<ApiResponse<List<Friend>>> {
            override fun onResponse(call: Call<ApiResponse<List<Friend>>>, response: Response<ApiResponse<List<Friend>>>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { newFriends ->
                        // Remove from incoming requests
                        _incomingRequests.removeAll { it.id == friendId }
                    }
                } else {
                    println("Failed to reject friend request: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Friend>>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }

    fun reportUser(reason: String) {
        if (reason.isEmpty()) {
            dialogErrorMessage = "Please enter a reason"
            return
        }

        val apiService = RetrofitInstance.friendService
        val call = apiService.reportUserById(ReportUserAction(selectedFriend?.id ?: 0, reason))

        call.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                if (response.isSuccessful) {
                    dialogSuccessMessage = "User reported"
                } else {
                    dialogErrorMessage = "Failed to report user: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                dialogErrorMessage = "Something went wrong: ${t.message}"
            }
        })
    }

    fun removeIncomingFriendRequest(requestIndex: Int) {
        if (requestIndex in _incomingRequests.indices) {
            _incomingRequests.removeAt(requestIndex)
        } else {
            println("Invalid index: $requestIndex")
        }
    }

    fun removeFriendIdx(friendIndex: Int) {
        if (friendIndex in _friends.indices) {
            _friends.removeAt(friendIndex)
        } else {
            println("Invalid index: $friendIndex")
        }
    }

    fun sendFriendRequest(friendCode: String) {
        if (friendCode.isEmpty() || !friendCode.isDigitsOnly()) {
            dialogErrorMessage = "Please enter a valid friend code"
            return
        }

        data class FriendRequest(val friendId: Int)

        val apiService = RetrofitInstance.friendService

        val friendRequestCall = apiService.sendFriendRequest(FriendRequest(friendId = friendCode.toInt()))
        friendRequestCall.enqueue(object : Callback<ApiResponse<Any>> {
            override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) {
                if (response.isSuccessful) {
                    println("Friend request sent successfully to User ID: $friendCode")
                    dialogSuccessMessage = "Friend request sent"
                } else {
                    dialogErrorMessage = "Failed to send friend request: ${response.parseErrorBody()?.message}"
                }
            }

            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                dialogErrorMessage = "Something went wrong while sending friend request"
            }
        })
    }

}
