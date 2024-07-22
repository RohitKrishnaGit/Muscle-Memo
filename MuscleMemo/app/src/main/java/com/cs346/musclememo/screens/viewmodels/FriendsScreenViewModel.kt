package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.RetrofitInstance
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.Friend
import com.cs346.musclememo.classes.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FriendsScreenViewModel : ViewModel() {
    var showAddFriendDialog by mutableStateOf(false)
        private set

    var dialogErrorMessage by mutableStateOf("")
        private set

    var dialogSuccessMessage by mutableStateOf("")
        private set

    var addFriendEmail by mutableStateOf("")

    var addFriendVisible by mutableStateOf(false)
        private set

    var selectedFriend by mutableStateOf<User?>(null)
        private set

    var friendProfileVisible by mutableStateOf(false)
        private set

    var friendChatVisible by mutableStateOf(false)
        private set

    fun updateShowAddFriendDialog(visible: Boolean) {
        showAddFriendDialog = visible
    }

    fun updateDialogErrorMessage(message: String) {
        dialogErrorMessage = message
    }

    fun updateDialogSuccessMessage(message: String) {
        dialogSuccessMessage = message
    }

    fun updateAddFriendEmail(email: String) {
        addFriendEmail = email
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

    private val _friends = mutableStateListOf<Friend>()
    private val _incomingRequests = mutableStateListOf<Friend>()

    val friends: List<Friend> = _friends
    val incomingRequests: List<Friend> = _incomingRequests

    init {
        getIncomingFriendRequests()
        getFriendByUserId()
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

    fun sendFriendRequest(username: String) {
        if (username.isEmpty()) {
            dialogErrorMessage = "Please enter a username"
            return
        }

        data class FriendRequest(val friendId: Int)

        val apiService = RetrofitInstance.friendService
        val call = apiService.getUserIdByUsername(username)

        call.enqueue(object : Callback<ApiResponse<Int>> {
            override fun onResponse(call: Call<ApiResponse<Int>>, response: Response<ApiResponse<Int>>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let { userId ->
                        println("User ID obtained: $userId")

                        val friendRequestCall = apiService.sendFriendRequest(FriendRequest(friendId = userId))
                        friendRequestCall.enqueue(object : Callback<ApiResponse<Any>> {
                            override fun onResponse(call: Call<ApiResponse<Any>>, response: Response<ApiResponse<Any>>) {
                                if (response.isSuccessful) {
                                    println("Friend request sent successfully to User ID: $userId")
                                    dialogSuccessMessage = "Friend request sent"
                                } else {
                                    dialogErrorMessage = "Failed to send friend request: ${response.message()}"
                                }
                            }

                            override fun onFailure(call: Call<ApiResponse<Any>>, t: Throwable) {
                                dialogErrorMessage = "Something went wrong while sending friend request"
                            }
                        })

                    }
                } else {
                    dialogErrorMessage = "User not found"
                }
            }

            override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {
                dialogErrorMessage = "Something went wrong"
            }
        })
    }

}
