package fr.isen.segfault.thedisneyapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TmdbApiClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private val client = OkHttpClient.Builder()
        .addInterceptor(TmdbAuthInterceptor())
        .build()

    val retrofit: TmdbApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TmdbApiService::class.java)
}