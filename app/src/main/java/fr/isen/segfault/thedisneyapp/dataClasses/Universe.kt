package fr.isen.segfault.thedisneyapp.dataClasses

import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.database
import com.google.gson.annotations.SerializedName
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import fr.isen.segfault.thedisneyapp.R

data class UniverseCategorie(
    @SerializedName("universe")
    val universes: List<Universe> = emptyList()
)

data class Universe(
    val id: String = "",
    val name: String = "",
    val franchises: Map<String, Franchise> = emptyMap()
)

fun fetchUniverses(callback: (List<Universe>) -> Unit) {
    val database = Firebase.database
    val ref = database.getReference("universes")

    ref.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val universes = mutableListOf<Universe>()

            for (child in snapshot.children) {
                val universe = child.getValue(Universe::class.java)
                universe?.let { universes.add(it) }
            }

            callback(universes.sortedBy { it.name })
        }

        override fun onCancelled(error: DatabaseError) {}
    })
}

fun getUniverseLogoRes(universeId: String): Int {
    return when (universeId) {
        "marvel" -> R.drawable.logo_marvel
        "star_wars" -> R.drawable.logo_star_wars
        "disney" -> R.drawable.logo_disney
        "avatar" -> R.drawable.logo_avatar
        "pixar" -> R.drawable.logo_pixar
        else -> R.drawable.logo_disney
    }
}