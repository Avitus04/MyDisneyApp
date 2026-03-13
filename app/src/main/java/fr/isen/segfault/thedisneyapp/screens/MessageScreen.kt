package fr.isen.segfault.thedisneyapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import fr.isen.segfault.thedisneyapp.R
import fr.isen.segfault.thedisneyapp.dataClasses.MessageUi
import fr.isen.segfault.thedisneyapp.utils.fetchMessages
import fr.isen.segfault.thedisneyapp.utils.markMessageAsRead

@Composable
fun MessagesScreen(
    onFilmClick: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()
    var messages by remember { mutableStateOf<List<MessageUi>>(emptyList()) }

    LaunchedEffect(Unit) {
        fetchMessages { messages = it }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    Log.d("RemoteConfig", "final message: '$messages[0].filmTitle'")
    /////////////////////////////////////////////////////////////////////////////////////

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colorResource(R.color.accent2).copy(alpha = 0.18f),
                            colorResource(R.color.background).copy(alpha = 0f)
                        ),
                        radius = 700f
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "MESSAGES",
                color = colorResource(R.color.accent),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 56.dp)
            )

            Text(
                text = "${messages.size} messages",
                color = colorResource(R.color.text),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            if (messages.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No messages yet",
                        color = colorResource(R.color.text_sub),
                        fontSize = 15.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { msg ->
                        MessageCard(
                            message = msg,
                            onFilmClick = {
                                markMessageAsRead(auth.currentUser?.uid ?: "", msg.id)
                                onFilmClick(msg.filmId)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MessageCard(
    message: MessageUi,
    onFilmClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (!message.read) colorResource(R.color.accent).copy(alpha = 0.5f)
                else colorResource(R.color.card_border),
                RoundedCornerShape(16.dp)
            )
            .clickable { onFilmClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!message.read)
                colorResource(R.color.accent_dim)
            else colorResource(R.color.card)
        ),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.senderUsername.ifBlank { message.senderEmail },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.text),
                    modifier = Modifier.weight(1f)
                )
                if (!message.read) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(colorResource(R.color.accent), CircleShape)
                    )
                }
            }

            Text(
                text = "About: ${message.filmTitle}",
                fontSize = 12.sp,
                color = colorResource(R.color.accent),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = message.message,
                fontSize = 13.sp,
                color = colorResource(R.color.text_sub),
                modifier = Modifier.padding(top = 8.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}