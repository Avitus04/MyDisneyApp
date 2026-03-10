package fr.isen.segfault.thedisneyapp.network

import fr.isen.segfault.thedisneyapp.dataClasses.TmdbMovieSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApiService {

    @GET("search/movie")
    fun searchMovie(
        @Query("query") title: String,
        @Query("year") year: Int? = null,
        @Query("language") language: String = "fr-FR"
    ): Call<TmdbMovieSearchResponse>
}