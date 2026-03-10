package fr.isen.segfault.thedisneyapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Face
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.segfault.thedisneyapp.R
import fr.isen.segfault.thedisneyapp.dataClasses.Film

@Composable
fun FilmsScreen(
    universeId: String,
    franchiseId: String,
    fetchFilmsByFranchise: (String, String, (List<Film>) -> Unit) -> Unit,
    onFilmClick: (String) -> Unit
) {
    var films by remember { mutableStateOf<List<Film>>(emptyList()) }

    LaunchedEffect(universeId, franchiseId) {
        fetchFilmsByFranchise(universeId, franchiseId) {
            films = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Films",
            color = colorResource(R.color.text),
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(films) { film ->
                FilmCard(
                    film = film,
                    onClick = {
                        onFilmClick(film.id)
                    }
                )
            }
        }
    }
}

@Composable
fun FilmCard(
    film: Film,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .border(
                width = 1.dp,
                color = colorResource(R.color.text),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.card)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(92.dp)
                    .background(
                        color = colorResource(R.color.text).copy(alpha = 0.05f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Face,
                    contentDescription = "Film",
                    tint = colorResource(R.color.text).copy(alpha = 0.8f),
                    modifier = Modifier.size(34.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = film.title,
                        color = colorResource(R.color.text),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2
                    )

                    film.genre?.takeIf { it.isNotBlank() }?.let { genre ->
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = genre,
                            color = colorResource(R.color.text).copy(alpha = 0.65f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = film.releaseYear?.toString() ?: "-",
                        color = colorResource(R.color.text).copy(alpha = 0.72f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Open film",
                        tint = colorResource(R.color.text).copy(alpha = 0.72f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}