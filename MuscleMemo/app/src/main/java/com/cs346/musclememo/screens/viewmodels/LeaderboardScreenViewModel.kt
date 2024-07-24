package com.cs346.musclememo.screens.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.cs346.musclememo.api.RetrofitInstance
import com.cs346.musclememo.api.services.Records
import com.cs346.musclememo.api.services.UpdateUserVisibilityPrRequest
import com.cs346.musclememo.api.types.ApiResponse
import com.cs346.musclememo.classes.ExerciseRef
import com.cs346.musclememo.classes.LeaderboardEntry
import com.cs346.musclememo.classes.User
import com.cs346.musclememo.utils.convertPrNameToRefName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderboardScreenViewModel : ViewModel() {
    var user by mutableStateOf(User(0, "User", "", null, null, "Male", "Intermediate"))
        private set
    val globalLeaderboardEntries = mutableStateListOf<LeaderboardEntry>()
    val friendsLeaderboardEntries = mutableStateListOf<LeaderboardEntry>()

    var meOnLeaderboard by mutableStateOf(false)

    val myLeaderboardEntries = mutableMapOf<String, LeaderboardEntry>()

    val _leaderboardEntries: List<LeaderboardEntry>
        get() =  if (leaderboardType == "Friends")
            friendsLeaderboardEntries.toList()
        else
            globalLeaderboardEntries.toList()

    val leaderboardEntries: List<LeaderboardEntry?>
        get() = if (!meOnLeaderboard)
                    _leaderboardEntries.plus(myLeaderboardEntries[currentExerciseRef.name]).sortedByDescending { it?.value }
                else
                    _leaderboardEntries.sortedByDescending { it.value }

    var currentExerciseRef by mutableStateOf(ExerciseRef(id = 1))
    var exerciseRefs by mutableStateOf<List<ExerciseRef>>(listOf())
    private val sortedExerciseRefs: List<ExerciseRef>
        get() = exerciseRefs.filter { it.name.contains(exerciseSearchText, ignoreCase = true) }
                .sortedWith(
                if (isSortedAlphabetically)
                    compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }
                else
                    compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.name }
                )

    val groupedRefs:  Map<String, List<ExerciseRef>>
        get() = if (exerciseSearchText == "") {
            userCompletedExerciseRefs
        } else {
            sortedExerciseRefs
        }.groupBy { it.name.first().uppercase() }

    private val userCompletedExerciseRefs: List<ExerciseRef>
        get() = exerciseRefs.filter {
            myLeaderboardEntries[it.name] != null && myLeaderboardEntries[it.name]?.value != "0"
        }

    var leaderboardType by mutableStateOf("Global")
    var chooseExerciseVisible by mutableStateOf(false)

    var exerciseSearchText by mutableStateOf("")
    var isSortedAlphabetically by mutableStateOf(true)

    var mePrs by mutableStateOf<Map<String, Int>?>(null)
    var mePrsVisible = mutableStateMapOf<String, Boolean>()

    fun updateCurrentExerciseRef(ref: ExerciseRef){
        currentExerciseRef = ref
    }

    fun toggleSort(){
        isSortedAlphabetically = !isSortedAlphabetically
    }
    fun updateExerciseSearchText (text: String){
        exerciseSearchText = text
    }
    fun updateChooseExerciseVisible(state: Boolean){
        chooseExerciseVisible = state
    }
    fun updateLeaderboardType(type: String){
        leaderboardType = type
    }

    fun fetchAllLeaderboards(){
        meOnLeaderboard = false
        fetchLeaderboardResults()
        fetchFriendLeaderboardResults()
    }

    fun fetchPrVisibility(refName: String){
        val ref = exerciseRefs.filter { it.name == refName }
        if (ref.isNotEmpty()){
            RetrofitInstance.prVisibilityService.getUserPr(ref[0].id.toString()).enqueue(object :
                Callback<ApiResponse<Boolean>>{
                override fun onResponse(
                    call: Call<ApiResponse<Boolean>>,
                    response: Response<ApiResponse<Boolean>>
                ) {
                    if (response.isSuccessful){
                        mePrsVisible[refName] = response.body()?.data ?: false
                        print("PrsVisible: ")
                        println(mePrsVisible)
                    }
                }
                override fun onFailure(call: Call<ApiResponse<Boolean>>, t: Throwable) {
                    t.printStackTrace()
                }
            }
            )
        }
    }

    fun toggleVisibility(exerciseRef: ExerciseRef){
        val isVisible = mePrsVisible[exerciseRef.name]
        if (isVisible != null){
            RetrofitInstance.prVisibilityService.updateUserPr(exerciseRef.id, UpdateUserVisibilityPrRequest(!isVisible)).enqueue(
                object : Callback<ApiResponse<String>>{
                    override fun onResponse(
                        call: Call<ApiResponse<String>>,
                        response: Response<ApiResponse<String>>
                    ) {
                        if (response.isSuccessful){
                            mePrsVisible[exerciseRef.name] = !isVisible
                        }
                        else {
                            println("toggle failed")
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse<String>>, t: Throwable) {
                        t.printStackTrace()
                        println("server cannot be reached")
                    }
                }
            )
        }
    }

    private fun fetchLeaderboardResults(){
        RetrofitInstance.userPrsService.getTopN(currentExerciseRef.id.toString(), "50").enqueue(object:
            Callback<ApiResponse<List<Records>>>{
            override fun onResponse(
                call: Call<ApiResponse<List<Records>>>,
                response: Response<ApiResponse<List<Records>>>
            ) {
                if (response.isSuccessful){
                    globalLeaderboardEntries.clear()
                    response.body()?.data?.let{ Records ->
                        for (record in Records){
                            val entry = LeaderboardEntry(username = record.username, value = record.pr)
                            globalLeaderboardEntries.add(entry)
                            if (user.username == record.username)
                                meOnLeaderboard = true
                        }
                        globalLeaderboardEntries.sortBy {it.value}
                        }
                    }
            }

            override fun onFailure(call: Call<ApiResponse<List<Records>>>, t: Throwable) {
                t.printStackTrace()
                println("Fetch leaderboard failed")
            }

        })
    }

    fun fetchFriendLeaderboardResults(){
        RetrofitInstance.userPrsService.getTopNFriends(currentExerciseRef.id.toString(), "50").enqueue(object:
            Callback<ApiResponse<List<Records>>>{
            override fun onResponse(
                call: Call<ApiResponse<List<Records>>>,
                response: Response<ApiResponse<List<Records>>>
            ) {
                if (response.isSuccessful){
                    friendsLeaderboardEntries.clear()
                    response.body()?.data?.let{
                        for (record in it){
                            val entry = LeaderboardEntry(username = record.username, value = record.pr)
                            friendsLeaderboardEntries.add(entry)
                        }
                        friendsLeaderboardEntries.sortBy {it.value}
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<Records>>>, t: Throwable) {
                t.printStackTrace()
                println("Fetch friend leaderboard failed")
            }

        })
    }

    private fun getExerciseRefs(onSuccess: () -> Unit){
        RetrofitInstance.exerciseService.getExerciseRef().enqueue(object: Callback<ApiResponse<List<ExerciseRef>>>{
            override fun onResponse(
                call: Call<ApiResponse<List<ExerciseRef>>>,
                response: Response<ApiResponse<List<ExerciseRef>>>
            ) {
                if (response.isSuccessful) {
                    exerciseRefs = response.body()?.data!!
                    onSuccess()
                }
            }

            override fun onFailure(call: Call<ApiResponse<List<ExerciseRef>>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    private fun getPersonalBests(onSuccess: () -> Unit){
        RetrofitInstance.userPrsService.getAllUserPr().enqueue(object :
            Callback<ApiResponse<Map<String, Int>>>{
            override fun onResponse(
                call: Call<ApiResponse<Map<String, Int>>>,
                response: Response<ApiResponse<Map<String, Int>>>
            ) {
                if (response.isSuccessful){
                    mePrs = response.body()?.data
                    mePrs = mePrs!!.filter { it.key != "id" && it.value != 0 }
                    mePrsVisible.clear()
                    myLeaderboardEntries.clear()
                    mePrs!!.forEach{ pr ->
                        val refName = convertPrNameToRefName(pr.key)
                        myLeaderboardEntries[refName] =
                            LeaderboardEntry(user.username, pr.value.toString())
                        fetchPrVisibility(refName)
                    }
                    onSuccess()
                }
            }

            override fun onFailure(call: Call<ApiResponse<Map<String, Int>>>, t: Throwable) {
                t.printStackTrace()
            }

        }
        )
    }

    private fun getMe(onSuccess: () -> Unit){
        RetrofitInstance.userService.getMyUser().enqueue(object: Callback<ApiResponse<User>>{
            override fun onResponse(
                call: Call<ApiResponse<User>>,
                response: Response<ApiResponse<User>>
            ) {
                if (response.isSuccessful){
                    val fetchedUser = response.body()?.data
                    fetchedUser?.let{
                        user = it

                    }
                    onSuccess()
                }
            }

            override fun onFailure(call: Call<ApiResponse<User>>, t: Throwable) {
                t.printStackTrace()
            }

        })
    }


    init {
        getMe(onSuccess = {
                getExerciseRefs(onSuccess = {
                    getPersonalBests(onSuccess = {
                    currentExerciseRef = exerciseRefs[0]
                    fetchAllLeaderboards()
                })
            })
        })
    }
}