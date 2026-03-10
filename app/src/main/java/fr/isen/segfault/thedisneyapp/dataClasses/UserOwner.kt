package fr.isen.segfault.thedisneyapp.dataClasses

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import com.google.firebase.database.ValueEventListener

data class UserOwnerUi(
    val uid: String = "",
    val displayName: String = "",
    val email: String = ""
)

fun fetchUsersWhoOwnAndWantToGetRid(
    filmId: String,
    callback: (List<UserOwnerUi>) -> Unit
) {
    val database = Firebase.database
    val ref = database.getReference("users")

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val result = mutableListOf<UserOwnerUi>()

            for (userSnapshot in snapshot.children) {
                val uid = userSnapshot.child("uid").getValue(String::class.java).orEmpty()
                val displayName = userSnapshot.child("username  ").getValue(String::class.java).orEmpty()
                val email = userSnapshot.child("email").getValue(String::class.java).orEmpty()

                val filmStatus = userSnapshot.child("filmStatus").child(filmId)
                val own = filmStatus.child("ownDvdBluray").getValue(Boolean::class.java) ?: false
                val wantToGetRid = filmStatus.child("wantToGetRid").getValue(Boolean::class.java) ?: false

                if (own && wantToGetRid) {
                    result.add(
                        UserOwnerUi(
                            uid = uid,
                            displayName = displayName,
                            email = email
                        )
                    )
                }
            }

            callback(result)
        }

        override fun onCancelled(error: DatabaseError) {
            callback(emptyList())
        }
    })
}
