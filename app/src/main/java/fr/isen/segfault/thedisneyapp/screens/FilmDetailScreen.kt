package fr.isen.segfault.thedisneyapp.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import fr.isen.segfault.thedisneyapp.R
import fr.isen.segfault.thedisneyapp.dataClasses.Film
import fr.isen.segfault.thedisneyapp.dataClasses.TmdbMovieSearchResponse
import fr.isen.segfault.thedisneyapp.dataClasses.UserOwnerUi
import fr.isen.segfault.thedisneyapp.dataClasses.fetchCurrentUserFilmStatus
import fr.isen.segfault.thedisneyapp.dataClasses.fetchUsersWhoOwnAndWantToGetRid
import fr.isen.segfault.thedisneyapp.dataClasses.getPosterUrl
import fr.isen.segfault.thedisneyapp.dataClasses.saveFilmStatus
import fr.isen.segfault.thedisneyapp.network.TmdbApiClient

@Composable
fun FilmDetailScreen(
    filmId: String,
    fetchFilmById: (String, (Film?) -> Unit) -> Unit
) {
    var film by remember { mutableStateOf<Film?>(null) }
    var availableUsers by remember { mutableStateOf<List<UserOwnerUi>>(emptyList()) }
    var watched by remember { mutableStateOf(false) }
    var wantToWatch by remember { mutableStateOf(false) }
    var ownDvdBluray by remember { mutableStateOf(false) }
    var wantToGetRid by remember { mutableStateOf(false) }
    var posterUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(filmId) {
        fetchFilmById(filmId) { loadedFilm ->
            film = loadedFilm
            loadedFilm?.let { current ->
                val call = TmdbApiClient.retrofit.searchMovie(
                    title = current.title,
                    year = current.releaseYear
                )
                call.enqueue(object : retrofit2.Callback<TmdbMovieSearchResponse> {
                    override fun onResponse(
                        call: retrofit2.Call<TmdbMovieSearchResponse>,
                        response: retrofit2.Response<TmdbMovieSearchResponse>
                    ) {
                        val movie = response.body()?.results?.firstOrNull()
                        posterUrl = getPosterUrl(movie?.poster_path)
                    }
                    override fun onFailure(call: retrofit2.Call<TmdbMovieSearchResponse>, t: Throwable) {
                        posterUrl = null
                    }
                })
            }
        }
        fetchUsersWhoOwnAndWantToGetRid(filmId) { availableUsers = it }
        fetchCurrentUserFilmStatus(filmId) { status ->
            if (status != null) {
                watched = status.watched
                wantToWatch = status.wantToWatch
                ownDvdBluray = status.ownDvdBluray
                wantToGetRid = status.wantToGetRid
            }
        }
    }

    val currentFilm = film

    fun persistStatus() {
        saveFilmStatus(
            filmId = filmId,
            watched = watched,
            wantToWatch = wantToWatch,
            ownDvdBluray = ownDvdBluray,
            wantToGetRid = wantToGetRid
        )
    }

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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = colorResource(R.color.card)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .width(130.dp)
                            .height(190.dp)
                            .border(1.dp, colorResource(R.color.text).copy(alpha = 0.25f))
                            .background(colorResource(R.color.text).copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (posterUrl != null) {
                            AsyncImage(
                                model = posterUrl,
                                contentDescription = currentFilm?.title,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = "Poster",
                                color = colorResource(R.color.text).copy(alpha = 0.65f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp, top = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = currentFilm?.title ?: "Loading...",
                            color = colorResource(R.color.text),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 3
                        )
                        DetailInfoLine(label = "Year", value = currentFilm?.releaseYear?.toString() ?: "-")
                        DetailInfoLine(label = "Genre", value = currentFilm?.genre ?: "-")
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusButtonWithText(
                            text = "Watched",
                            color = colorResource(R.color.status_green),
                            isActive = watched,
                            onClick = { watched = !watched; persistStatus() },
                            modifier = Modifier.weight(1f)
                        )
                        StatusButtonWithText(
                            text = "Want to watch",
                            color = colorResource(R.color.status_blue),
                            isActive = wantToWatch,
                            onClick = { wantToWatch = !wantToWatch; persistStatus() },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusButtonWithText(
                            text = "Own DVD",
                            color = colorResource(R.color.status_red),
                            isActive = ownDvdBluray,
                            onClick = {
                                ownDvdBluray = !ownDvdBluray
                                if (!ownDvdBluray) wantToGetRid = false
                                persistStatus()
                            },
                            modifier = Modifier.weight(1f)
                        )
                        if (ownDvdBluray) {
                            StatusButtonWithText(
                                text = "Get rid",
                                color = colorResource(R.color.status_yellow),
                                isActive = wantToGetRid,
                                onClick = { wantToGetRid = !wantToGetRid; persistStatus() },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }

                Column(modifier = Modifier.padding(top = 28.dp)) {
                    Text(
                        text = "Universe",
                        color = colorResource(R.color.text).copy(alpha = 0.65f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    DetailTagButton(
                        text = currentFilm?.universeId ?: "Universe",
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Column(modifier = Modifier.padding(top = 14.dp)) {
                    Text(
                        text = "Saga",
                        color = colorResource(R.color.text).copy(alpha = 0.65f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    DetailTagButton(
                        text = currentFilm?.franchiseId ?: "Saga",
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }

                Column(modifier = Modifier.padding(top = 18.dp, bottom = 4.dp)) {
                    Text(
                        text = "Available from users",
                        color = colorResource(R.color.text).copy(alpha = 0.65f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    if (availableUsers.isEmpty()) {
                        DetailTagButton(
                            text = "No user currently offers this film",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else {
                        Column(
                            modifier = Modifier.padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            availableUsers.forEach { user ->
                                UserOwnerCard(user = user)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailInfoLine(label: String, value: String) {
    Column {
        Text(
            text = label,
            color = colorResource(R.color.text).copy(alpha = 0.65f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = colorResource(R.color.text),
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun DetailTagButton(text: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
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
fun StatusButtonWithText(
    text: String,
    color: Color,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    color = if (isActive) color else color.copy(alpha = 0.30f),
                    shape = RoundedCornerShape(6.dp)
                )
                .border(
                    width = 1.5.dp,
                    color = color,
                    shape = RoundedCornerShape(6.dp)
                )
        )
        {
            if(isActive) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "Checked",
                    tint = colorResource(R.color.card_border).copy(alpha = 0.6f),
                )
            }
        }

        Text(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            color = colorResource(R.color.text),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2
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
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Text(
                text = user.displayName.ifBlank { user.email },
                color = colorResource(R.color.text),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Owns it & Wants to get rid of it",
                color = colorResource(R.color.text).copy(alpha = 0.72f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}