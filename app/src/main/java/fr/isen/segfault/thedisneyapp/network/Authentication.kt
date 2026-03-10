package fr.isen.segfault.thedisneyapp.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TMDB_BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1NDZmOTkwNjE2ZjNiZDI1MzgxMzM0ZmMwZGU3YWE5YiIsIm5iZiI6MTc3MzEzNzU1Ny4wMjIsInN1YiI6IjY5YWZlZTk0ZjhjZTU4NjU1Njc5NGVjYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.ORmQjp4jEhe3ebGH1DeWnFY1F1EsX5ASZDqcJX-yqDk"

class TmdbAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $TMDB_BEARER_TOKEN")
            .addHeader("accept", "application/json")
            .build()

        return chain.proceed(request)
    }
}
