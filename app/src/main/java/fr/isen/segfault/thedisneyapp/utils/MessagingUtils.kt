package fr.isen.segfault.thedisneyapp.utils

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import fr.isen.segfault.thedisneyapp.dataClasses.MessageData
import fr.isen.segfault.thedisneyapp.dataClasses.MessageUi
import kotlin.collections.emptyList
import kotlin.jvm.java

fun initRemoteConfig() {
    val remoteConfig = Firebase.remoteConfig
    val settings = remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 }
    remoteConfig.setConfigSettingsAsync(settings)
    remoteConfig.setDefaultsAsync(
        mapOf("contact_message" to
                "Hello ! {senderUsername} is interested in your DVD for the movie: {filmTitle}. You can contact this user at the following address : {senderEmail}")
    )
}

fun sendContactMessage(
    receiverUid: String,
    filmTitle: String,
    filmId: String,
    onSuccess: () -> Unit,
    onFailure: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    val sender = auth.currentUser ?: run { onFailure("Not logged in"); return }

    val remoteConfig = Firebase.remoteConfig

    // fetch latest config then send
    remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
        val template = remoteConfig.getString("contact_message")

        val message = template
            .replace("{filmTitle}", filmTitle)
            .replace("{senderEmail}", sender.email ?: "")
            .replace("{senderUsername}", sender.displayName ?: "")

        val db = Firebase.database.reference
        val messageId = db.child("messages").child(receiverUid).push().key ?: return@addOnCompleteListener

        val messageData = mapOf(
            "senderUid" to sender.uid,
            "senderUsername" to (sender.displayName ?: ""),
            "senderEmail" to (sender.email ?: ""),
            "filmTitle" to filmTitle,
            "filmId" to filmId,
            "message" to message,
            "timestamp" to System.currentTimeMillis(),
            "read" to false
        )

        db.child("messages").child(receiverUid).child(messageId)
            .setValue(messageData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it.message ?: "Error") }
    }
}

fun fetchMessages(callback: (List<MessageUi>) -> Unit) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: run { callback(emptyList()); return }

    Firebase.database.reference
        .child("messages").child(uid)
        .addListenerForSingleValueEvent(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(snapshot: com.google.firebase.database.DataSnapshot) {
                val messages = snapshot.children.mapNotNull { child ->
                    val m = child.getValue(MessageData::class.java) ?: return@mapNotNull null
                    MessageUi(
                        id = child.key ?: "",
                        senderUsername = m.senderUsername,
                        senderEmail = m.senderEmail,
                        filmTitle = m.filmTitle,
                        filmId = m.filmId,
                        message = m.message,
                        timestamp = m.timestamp,
                        read = m.read
                    )
                }.sortedByDescending { it.timestamp }
                callback(messages)
            }
            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                callback(emptyList())
            }
        })
}

fun markMessageAsRead(receiverUid: String, messageId: String) {
    Firebase.database.reference
        .child("messages").child(receiverUid).child(messageId)
        .child("read").setValue(true)
}