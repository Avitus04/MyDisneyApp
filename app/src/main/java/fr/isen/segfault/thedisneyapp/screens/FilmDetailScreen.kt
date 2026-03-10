package fr.isen.segfault.thedisneyapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.segfault.thedisneyapp.R
import fr.isen.segfault.thedisneyapp.dataClasses.Film
import fr.isen.segfault.thedisneyapp.dataClasses.UserOwnerUi
import fr.isen.segfault.thedisneyapp.dataClasses.fetchUsersWhoOwnAndWantToGetRid

@Composable
fun FilmDetailScreen(
    filmId: String,
    fetchFilmById: (String, (Film?) -> Unit) -> Unit
) {
    var film by remember { mutableStateOf<Film?>(null) }
    var availableUsers by remember { mutableStateOf<List<UserOwnerUi>>(emptyList()) }
    var own by remember { mutableStateOf(false) }
    var dvd by remember { mutableStateOf(false) }
    var getRid by remember { mutableStateOf(false) }


    LaunchedEffect(filmId) {
        fetchFilmById(filmId) {
            film = it
        }
        fetchUsersWhoOwnAndWantToGetRid(filmId) {
            availableUsers = it
        }
    }

    val currentFilm = film

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Detail Film",
            color = colorResource(R.color.text),
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.card)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .height(190.dp)
                            .border(
                                width = 1.dp,
                                color = colorResource(R.color.text).copy(alpha = 0.25f),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .background(
                                color = colorResource(R.color.text).copy(alpha = 0.08f),
                                shape = RoundedCornerShape(18.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Poster",
                            color = colorResource(R.color.text).copy(alpha = 0.65f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = currentFilm?.title ?: "Loading...",
                            color = colorResource(R.color.text),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 3
                        )

                        DetailInfoLine(
                            label = "Year",
                            value = currentFilm?.releaseYear?.toString() ?: "-"
                        )

                        DetailInfoLine(
                            label = "Genre",
                            value = currentFilm?.genre ?: "-"
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        StatusRow(
                            text = if (own) "Own" else "Not owned",
                            color = colorResource(R.color.status_green),
                            isActive = own,
                            onClick = { own = !own }
                        )

                        StatusRow(
                            text = if (dvd) "DVD / Blu-ray" else "No DVD / Blu-ray",
                            color = colorResource(R.color.status_red),
                            isActive = dvd,
                            onClick = { dvd = !dvd }
                        )

                        StatusRow(
                            text = if (getRid) "Want to get rid of" else "Keep",
                            color = colorResource(R.color.status_yellow),
                            isActive = getRid,
                            onClick = { getRid = !getRid }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Column {
                    Text(
                        text = "Universe",
                        color = colorResource(R.color.text).copy(alpha = 0.65f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DetailTagButton(
                        text = currentFilm?.universeId ?: "Universe"
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column {
                    Text(
                        text = "Saga",
                        color = colorResource(R.color.text).copy(alpha = 0.65f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    DetailTagButton(
                        text = currentFilm?.franchiseId ?: "Saga"
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Available from users",
                        color = colorResource(R.color.text).copy(alpha = 0.65f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (availableUsers.isEmpty()) {
                        DetailTagButton(text = "No user currently offers this film")
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            availableUsers.forEach { user ->
                                UserOwnerCard(user = user)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun DetailInfoLine(
    label: String,
    value: String
) {
    Column {
        Text(
            text = label,
            color = colorResource(R.color.text).copy(alpha = 0.65f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            color = colorResource(R.color.text),
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun DetailTagButton(
    text: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.text).copy(alpha = 0.06f)
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            color = colorResource(R.color.text),
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun StatusRow(
    text: String,
    color: Color,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    color = if (isActive) color else color.copy(alpha = 0.30f),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.5.dp,
                    color = color,
                    shape = RoundedCornerShape(8.dp)
                )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            color = colorResource(R.color.text),
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun UserOwnerCard(user: UserOwnerUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.text).copy(alpha = 0.06f)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = if (user.displayName.isNotBlank()) user.displayName else user.email,
                color = colorResource(R.color.text),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Owns it • Wants to get rid of it",
                color = colorResource(R.color.text).copy(alpha = 0.72f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}