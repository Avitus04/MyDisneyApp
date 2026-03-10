package fr.isen.segfault.thedisneyapp.dataClasses

data class TmdbMovieSearchResponse(
    val page: Int = 0,
    val results: List<TmdbMovie> = emptyList()
)

data class TmdbMovie(
    val id: Int = 0,
    val title: String = "",
    val original_title: String = "",
    val poster_path: String? = null,
    val release_date: String? = null
)

fun getPosterUrl(posterPath: String?): String? {
    return posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }
}