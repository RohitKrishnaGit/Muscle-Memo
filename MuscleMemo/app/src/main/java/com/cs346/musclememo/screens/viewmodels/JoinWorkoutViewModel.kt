package com.cs346.musclememo.screens.viewmodels

import com.cs346.musclememo.api.RetrofitInstance
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.SocketManager
import com.cs346.musclememo.api.services.CreatePublicWorkout
import com.cs346.musclememo.api.services.ExerciseDataSet
import com.cs346.musclememo.api.services.ExerciseRequest
import com.cs346.musclememo.api.services.CreateWorkoutRequest
import com.cs346.musclememo.api.services.CreateWorkoutResponse
import com.cs346.musclememo.api.services.GetWorkoutResponse
import com.cs346.musclememo.api.services.CreateTemplateClass
import com.cs346.musclememo.api.services.FilterPublicWorkout
import com.cs346.musclememo.api.services.PublicWorkout
import com.cs346.musclememo.api.services.UpdateUserPrRequest
import com.cs346.musclememo.api.services.WorkoutRequest
import com.cs346.musclememo.api.services.WorkoutRequestBody
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.api.types.parseErrorBody
import com.cs346.musclememo.classes.ExerciseIteration
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.ExerciseSet
import com.cs346.musclememo.classes.Friend
import com.cs346.musclememo.classes.Template
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.classes.Workout
import com.cs346.musclememo.utils.AppPreferences
import com.cs346.musclememo.utils.calculateScore
import com.cs346.musclememo.utils.epochToMonthYear
import com.cs346.musclememo.utils.translateDistance
import com.cs346.musclememo.utils.translateWeight
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.math.exp


class JoinWorkoutViewModel: ViewModel() {
    val sm = SocketManager()
    val workouts = mutableStateListOf<PublicWorkout>()
    val workoutRequests = mutableStateListOf<WorkoutRequest>()
    var currentUser: User? by mutableStateOf(null)
    val receivedMessages = mutableStateListOf<Message>()

    init {
        getCurrentUser()
    }

    var createWorkoutVisible by mutableStateOf(false)
        private set

    var publicWorkoutTab by mutableStateOf("Search")

    var createWorkoutName by mutableStateOf("")
        private set

    var createWorkoutExperience by mutableStateOf("")
        private set

    var experienceExpanded by mutableStateOf(false)
        private set

    var createWorkoutDescription by mutableStateOf("")
        private set

    var currentMessage by mutableStateOf("")
        private set

    var showCreateError by mutableStateOf(false)
        private set

    var friendsOnlyFilter by mutableStateOf(false)
        private set

    var genderFilter by mutableStateOf<String?>(null)
        private set

    var experienceFilter by mutableStateOf<String?>(null)
        private set

    var requestsVisible by mutableStateOf(false)
        private set

    var selectedWorkout by mutableStateOf<PublicWorkout?>(null)
        private set

    var workoutChatVisible by mutableStateOf(false)
        private set

    fun updateCurrentMessage(message: String) {
        currentMessage = message
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

    fun connectSocket(roomId: String) {
        sm.connect()
        sm.joinRoom(roomId, AppPreferences.refreshToken.toString())
        sm.onMessageReceived { msgId: Int, msg: String, sender: Sender ->
            receivedMessages.add(Message(msgId, roomId.toInt(), msg, sender))
        }
        sm.onHistoryRequest { getChatHistory(roomId) }
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

    fun updateReceivedMessages(messages: List<Message>) {
        receivedMessages.clear()
        receivedMessages.addAll(messages)
    }

    fun disconnectSocket() {
        sm.disconnect()
    }

    fun updateRequestsVisible(visible: Boolean) {
        requestsVisible = visible
    }

    fun updateExperienceFilter(experience: String?) {
        experienceFilter = experience
    }

    fun updateGenderFilter(gender: String?) {
        genderFilter = gender
    }

    fun updateFriendsOnlyFilter(state: Boolean) {
        friendsOnlyFilter = state
    }

    fun updateCreateWorkoutVisible(visible: Boolean) {
        createWorkoutVisible = visible
    }

    fun updateWorkoutTab(tab: String) {
        publicWorkoutTab = tab
    }

    fun updateCreateWorkoutName(name: String) {
        createWorkoutName = name
    }

    fun updateCreateWorkoutExperience(experience: String) {
        createWorkoutExperience = experience
    }

    fun updateExperienceExpanded(expanded: Boolean) {
        experienceExpanded = expanded
    }

    fun updateCreateWorkoutDescription(description: String) {
        createWorkoutDescription = description
    }

    fun validateForm(): Boolean {
        val isValid = createWorkoutName.isNotBlank() && createWorkoutDescription.isNotBlank()
        showCreateError = !isValid
        return isValid
    }

    fun clearWorkouts() {
        workouts.clear()
    }

    fun clearRequests() {
        workoutRequests.clear()
    }

    fun updateWorkoutChatVisible(visible: Boolean) {
        workoutChatVisible = visible
    }

    fun removeIncomingWorkoutRequest(requestIndex: Int) {
        if (requestIndex in workoutRequests.indices) {
            workoutRequests.removeAt(requestIndex)
        } else {
            println("Invalid index: $requestIndex")
        }
    }

    fun selectWorkout(workout: PublicWorkout, onComplete: (Boolean) -> Unit) {
        selectedWorkout = workout
        onComplete(true)
    }

    fun selectWorkoutChat(workout: PublicWorkout) {
        selectWorkout(workout) { success ->
            updateWorkoutChatVisible(success)
        }
    }

    fun rejectRequest(requestId: Int) {
        val apiService = RetrofitInstance.publicWorkoutService
        val call = apiService.rejectWorkoutRequest(requestId.toString())

        call.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                if (response.isSuccessful) {
                    println("success")
                } else {
                    println("Failed to get requests: ${response.message()}")
                    updateRequestsVisible(false)
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                println("Failed to get requests: ${t.message}")
                updateRequestsVisible(false)
            }
        })
    }

    fun acceptRequest(requestId: Int) {
        val apiService = RetrofitInstance.publicWorkoutService
        val call = apiService.acceptWorkoutRequest(requestId.toString())

        call.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                if (response.isSuccessful) {
                    println("success")
                } else {
                    println("Failed to get requests: ${response.message()}")
                    updateRequestsVisible(false)
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                println("Failed to get requests: ${t.message}")
                updateRequestsVisible(false)
            }
        })
    }

    fun selectRequests(workoutId: Int) {
        val apiService = RetrofitInstance.publicWorkoutService
        val call = apiService.getWorkoutRequests(workoutId.toString())

        call.enqueue(object : Callback<ApiResponse<List<WorkoutRequest>>> {
            override fun onResponse(call: Call<ApiResponse<List<WorkoutRequest>>>, response: Response<ApiResponse<List<WorkoutRequest>>>) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        clearRequests()
                        workoutRequests.addAll(it)
                        updateRequestsVisible(true)
                    }
                } else {
                    println("Failed to get requests: ${response.message()}")
                    updateRequestsVisible(false)
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<WorkoutRequest>>>, t: Throwable) {
                println("Failed to get requests: ${t.message}")
                updateRequestsVisible(false)
            }
        })
    }

    fun sendRequest(workoutId: Int) {
        val apiService = RetrofitInstance.publicWorkoutService
        val apiBody = WorkoutRequestBody(workoutId)
        val call = apiService.sendWorkoutRequest(apiBody)

        call.enqueue(object : Callback<ApiResponse<String>> {
            override fun onResponse(call: Call<ApiResponse<String>>, response: Response<ApiResponse<String>>) {
                if (response.isSuccessful) {
                    println(response.body())
                } else {
                    println("Failed to send request: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                println("Failed to send request: ${t.message}")
            }
        })
    }

    fun createWorkout() {
        val apiService = RetrofitInstance.publicWorkoutService
        val apiBody = CreatePublicWorkout(
            name = createWorkoutName,
            experience = createWorkoutExperience,
            description = createWorkoutDescription
        )
        val call = apiService.createPublicWorkout(apiBody)

        call.enqueue(object : Callback<ApiResponse<Int>> {
            override fun onResponse(call: Call<ApiResponse<Int>>, response: Response<ApiResponse<Int>>) {
                if (response.isSuccessful) {
                    println(response.body())
                } else {
                    println("Failed to create workout: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<Int>>, t: Throwable) {
                println("Failed to create workout: ${t.message}")
            }
        })
    }

    fun clearCreateForm() {
        createWorkoutName = ""
        createWorkoutExperience = ""
        createWorkoutDescription = ""
    }

    fun getJoinedWorkouts() {
        val apiService = RetrofitInstance.publicWorkoutService
        val call = apiService.getJoinedWorkouts()

        call.enqueue(object : Callback<ApiResponse<List<PublicWorkout>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<PublicWorkout>>>,
                response: Response<ApiResponse<List<PublicWorkout>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        workouts.clear()
                        workouts.addAll(it)
                    }
                } else {
                    println("Failed to get workouts: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<PublicWorkout>>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }

    fun getMyWorkouts() {
        val apiService = RetrofitInstance.publicWorkoutService
        val call = apiService.fetchMyPublicWorkouts()

        call.enqueue(object : Callback<ApiResponse<List<PublicWorkout>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<PublicWorkout>>>,
                response: Response<ApiResponse<List<PublicWorkout>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        workouts.clear()
                        workouts.addAll(it)
                    }
                } else {
                    println("Failed to get workouts: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<PublicWorkout>>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }

    fun getWorkouts() {
        println(genderFilter)
        println(experienceFilter)
        println(friendsOnlyFilter)

        val genderValue: String? = genderFilter?.takeIf { it.isNotEmpty() }
        val experienceValue: String? = experienceFilter?.takeIf { it.isNotEmpty() }

        println("Gender: $genderValue, Experience: $experienceValue, Friends Only: $friendsOnlyFilter")



        val apiService = RetrofitInstance.publicWorkoutService
        val apiBody = FilterPublicWorkout(
            gender = genderValue,
            experience = experienceValue,
            friendsOnly = friendsOnlyFilter,
            latitude = "10",
            longitude = "10"
        )
        val call = apiService.filterPublicWorkout(apiBody)
//        val call = apiService.filterPublicWorkout(
//            gender = genderValue,
//            experience = experienceValue,
//            friendsOnly = friendsOnlyFilter,
//            latitude = "10",
//            longitude = "10"
//        )

        call.enqueue(object : Callback<ApiResponse<List<PublicWorkout>>> {
            override fun onResponse(
                call: Call<ApiResponse<List<PublicWorkout>>>,
                response: Response<ApiResponse<List<PublicWorkout>>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.data?.let {
                        workouts.clear()
                        workouts.addAll(it)
                    }
                } else {
                    println("Failed to get workouts: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<PublicWorkout>>>, t: Throwable) {
                println("Failure: ${t.message}")
            }
        })
    }
}