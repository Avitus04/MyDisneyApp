package fr.isen.segfault.thedisneyapp.dataClasses

import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import com.google.firebase.database.ValueEventListener

data class Franchise(
    val id: String = "",
    val name: String = ""
)

fun fetchFranchises(
    universeId: String,
    callback: (List<Franchise>) -> Unit
) {
    val database = Firebase.database
    val ref = database.getReference("universes").child(universeId).child("franchises")

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val franchises = mutableListOf<Franchise>()

            for (child in snapshot.children) {
                val franchise = child.getValue(Franchise::class.java)
                franchise?.let { franchises.add(it) }
            }

            callback(franchises.sortedBy { it.name })
        }

        override fun onCancelled(error: DatabaseError) {}
    })
}
