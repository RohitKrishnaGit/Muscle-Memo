import com.cs346.musclememo.screens.services.ExerciseRefService
import com.cs346.musclememo.screens.services.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:3000"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    val exerciseService: ExerciseRefService by lazy {
        retrofit.create(ExerciseRefService::class.java)
    }
}