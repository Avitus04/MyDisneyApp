package fr.isen.segfault.thedisneyapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
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
    fetchUniverses: ((List<Universe>) -> Unit) -> Unit
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Universes",
            color = colorResource(R.color.text),
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(universesUi) { universe ->
                UniverseCard(
                    universe = universe,
                    logoRes =   getUniverseLogoRes(universe.id),
                    onClick = {
                        // TODO: navigation vers les franchises
                    }
                )
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
            .height(92.dp)
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
                    .width(86.dp)
                    .background(
                        color = colorResource(R.color.text).copy(alpha = 0.06f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = logoRes),
                    contentDescription = "${universe.name} logo",
                    modifier = Modifier
                        .fillMaxHeight(0.68f)
                        .fillMaxWidth(0.68f),
                    contentScale = ContentScale.Fit
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = universe.name,
                    modifier = Modifier.weight(1f),
                    color = colorResource(R.color.text),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2
                )

                Text(
                    text = "${universe.filmCount}",
                    color = colorResource(R.color.text).copy(alpha = 0.72f),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}