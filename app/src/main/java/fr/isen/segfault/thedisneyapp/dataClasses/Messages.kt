package fr.isen.segfault.thedisneyapp.dataClasses

data class MessageData(
    val senderUid: String = "",
    val senderUsername: String = "",
    val senderEmail: String = "",
    val filmTitle: String = "",
    val filmId: String = "",
    val message: String = "",
    val timestamp: Long = 0,
    val read: Boolean = false
)

data class MessageUi(
    val id: String,
    val senderUsername: String,
    val senderEmail: String,
    val filmTitle: String,
    val filmId: String,
    val message: String,
    val timestamp: Long,
    val read: Boolean
)