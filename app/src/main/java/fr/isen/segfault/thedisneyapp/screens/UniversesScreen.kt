package fr.isen.segfault.thedisneyapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.segfault.thedisneyapp.R
import fr.isen.segfault.thedisneyapp.dataClasses.Universe
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import fr.isen.segfault.thedisneyapp.dataClasses.Film
import fr.isen.segfault.thedisneyapp.dataClasses.fetchFilms
import fr.isen.segfault.thedisneyapp.dataClasses.getUniverseLogoRes

data class UniverseUi(
    val id: String = "",
    val name: String = "",
    val filmCount: Int = 0
)

@Composable
fun UniversesScreen(
    modifier: Modifier = Modifier,
    fetchUniverses: ((List<Universe>) -> Unit) -> Unit,
    onUniverseClick: (String) -> Unit
) {
    var universes by remember { mutableStateOf<List<Universe>>(emptyList()) }
    var films by remember { mutableStateOf<List<Film>>(emptyList()) }

    LaunchedEffect(Unit) {
        fetchUniverses { universes = it }
        fetchFilms { films = it }
    }

    val universesUi = universes.map { universe ->
        UniverseUi(
            id = universe.id,
            name = universe.name,
            filmCount = films.count { it.universeId == universe.id }
        )
    }.sortedBy { it.name }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
    ) {
        // top radial glow
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
                text = "UNIVERSES",
                color = colorResource(R.color.accent),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 56.dp)
            )

            Text(
                text = "Explore",
                color = colorResource(R.color.text),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(universesUi) { universe ->
                    UniverseCard(
                        universe = universe,
                        logoRes = getUniverseLogoRes(universe.id),
                        onClick = { onUniverseClick(universe.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun UniverseCard(
    universe: UniverseUi,
    logoRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.card)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(R.color.text).copy(alpha = 0.05f))
                    .border(
                        width = 1.dp,
                        color = colorResource(R.color.text).copy(alpha = 0.08f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = logoRes),
                    contentDescription = "${universe.name} logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Text(
                text = universe.name,
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                color = colorResource(R.color.text),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${universe.filmCount} films",
                    color = colorResource(R.color.text).copy(alpha = 0.72f),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Open universe",
                    tint = colorResource(R.color.text).copy(alpha = 0.72f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}