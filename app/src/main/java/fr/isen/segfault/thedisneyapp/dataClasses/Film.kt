package fr.isen.segfault.thedisneyapp.dataClasses

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import com.google.firebase.database.ValueEventListener

data class Film(
    val id: String = "",
    val title: String = "",
    val universeId: String = "",
    val franchiseId: String = "",
    val releaseYear: Int? = null,
    val genre: String? = null
)

data class UniverseFilm(
    val id: String = "",
    val name: String = "",
    val filmCount: Int = 0
)

fun fetchFilms(callback: (List<Film>) -> Unit) {
    val database = Firebase.database
    val ref = database.getReference("films")

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val films = mutableListOf<Film>()

            for (child in snapshot.children) {
                val film = child.getValue(Film::class.java)
                film?.let { films.add(it.copy(id = child.key ?: it.id)) } // <-- fix ici
            }

            callback(films)
        }

        override fun onCancelled(error: DatabaseError) {}
    })
}

fun fetchFilmsByFranchise(
    universeId: String,
    franchiseId: String,
    callback: (List<Film>) -> Unit
) {
    val database = Firebase.database
    val ref = database.getReference("films")

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val films = mutableListOf<Film>()

            for (child in snapshot.children) {
                val film = child.getValue(Film::class.java)
                if (film != null &&
                    film.universeId == universeId &&
                    film.franchiseId == franchiseId
                ) {
                    films.add(film)
                }
            }

            callback(films.sortedBy { it.releaseYear ?: 0 })
        }

        override fun onCancelled(error: DatabaseError) {}
    })
}

fun fetchFilmById(
    filmId: String,
    callback: (Film?) -> Unit
) {
    val database = Firebase.database
    val ref = database.getReference("films").child(filmId)

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val film = snapshot.getValue(Film::class.java)
            callback(film)
        }

        override fun onCancelled(error: DatabaseError) {
            callback(null)
        }
    })
}