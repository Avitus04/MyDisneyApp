package fr.isen.segfault.thedisneyapp.dataClasses
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

data class UserFilmStatus(
    val filmId: String = "",
    val watched: Boolean = false,
    val wantToWatch: Boolean = false,
    val ownDvdBluray: Boolean = false,
    val wantToGetRid: Boolean = false
)

data class OwnedFilmUi(
    val filmId: String = "",
    val title: String = "",
    val releaseYear: Int? = null,
    val wantToGetRid: Boolean = false
)

fun saveFilmStatus(
    filmId: String,
    watched: Boolean,
    wantToWatch: Boolean,
    ownDvdBluray: Boolean,
    wantToGetRid: Boolean,
    onSuccess: () -> Unit = {},
    onFailure: (String) -> Unit = {}
) {
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null) {
        onFailure("No connected user")
        return
    }

    val uid = user.uid

    val status = UserFilmStatus(
        filmId = filmId,
        watched = watched,
        wantToWatch = wantToWatch,
        ownDvdBluray = ownDvdBluray,
        wantToGetRid = wantToGetRid
    )

    Firebase.database
        .getReference("users")
        .child(uid)
        .child("filmStatus")
        .child(filmId)
        .setValue(status)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { error ->
            onFailure(error.message ?: "Unknown error")
        }
}
fun fetchCurrentUserFilmStatus(
    filmId: String,
    callback: (UserFilmStatus?) -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null) {
        callback(null)
        return
    }

    val database = Firebase.database
    val ref = database
        .getReference("users")
        .child(user.uid)
        .child("filmStatus")
        .child(filmId)

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val status = snapshot.getValue(UserFilmStatus::class.java)
            callback(status)
        }

        override fun onCancelled(error: DatabaseError) {
            callback(null)
        }
    })
}

fun fetchOwnedFilms(
    callback: (List<OwnedFilmUi>) -> Unit
) {
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null) {
        callback(emptyList())
        return
    }

    val database = Firebase.database
    val userStatusRef = database
        .getReference("users")
        .child(user.uid)
        .child("filmStatus")

    val filmsRef = database.getReference("films")

    userStatusRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(statusSnapshot: DataSnapshot) {
            val ownedStatuses = mutableListOf<UserFilmStatus>()

            for (child in statusSnapshot.children) {
                val status = child.getValue(UserFilmStatus::class.java)
                if (status != null && status.ownDvdBluray) {
                    ownedStatuses.add(status)
                }
            }

            if (ownedStatuses.isEmpty()) {
                callback(emptyList())
                return
            }

            filmsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(filmsSnapshot: DataSnapshot) {
                    val ownedFilms = mutableListOf<OwnedFilmUi>()

                    for (status in ownedStatuses) {
                        val filmSnapshot = filmsSnapshot.child(status.filmId)
                        val title = filmSnapshot.child("title").getValue(String::class.java).orEmpty()
                        val releaseYear = filmSnapshot.child("releaseYear").getValue(Int::class.java)

                        ownedFilms.add(
                            OwnedFilmUi(
                                filmId = status.filmId,
                                title = title,
                                releaseYear = releaseYear,
                                wantToGetRid = status.wantToGetRid
                            )
                        )
                    }

                    callback(ownedFilms.sortedBy { it.title })
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(emptyList())
                }
            })
        }

        override fun onCancelled(error: DatabaseError) {
            callback(emptyList())
        }
    })
}

fun removeOwnedFilm(
    filmId: String,
    onSuccess: () -> Unit = {},
    onFailure: (String) -> Unit = {}
) {
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null) {
        onFailure("No connected user")
        return
    }

    val ref = Firebase.database
        .getReference("users")
        .child(user.uid)
        .child("filmStatus")
        .child(filmId)
        .child("ownDvdBluray")

    ref.setValue(false)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { error ->
            onFailure(error.message ?: "Unknown error")
        }
}

fun updateWantToGetRid(
    filmId: String,
    value: Boolean,
    onSuccess: () -> Unit = {},
    onFailure: (String) -> Unit = {}
) {
    val user = FirebaseAuth.getInstance().currentUser

    if (user == null) {
        onFailure("No connected user")
        return
    }

    val ref = Firebase.database
        .getReference("users")
        .child(user.uid)
        .child("filmStatus")
        .child(filmId)
        .child("wantToGetRid")

    ref.setValue(value)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { error ->
            onFailure(error.message ?: "Unknown error")
        }
}