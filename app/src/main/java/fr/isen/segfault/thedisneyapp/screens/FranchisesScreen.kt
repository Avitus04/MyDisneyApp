package fr.isen.segfault.thedisneyapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.segfault.thedisneyapp.R
import fr.isen.segfault.thedisneyapp.dataClasses.Film
import fr.isen.segfault.thedisneyapp.dataClasses.Franchise
import fr.isen.segfault.thedisneyapp.dataClasses.fetchFilms

@Composable
fun FranchisesScreen(
    universeId: String,
    fetchFranchises: (String, (List<Franchise>) -> Unit) -> Unit,
    onFranchiseClick: (String) -> Unit
) {
    var franchises by remember { mutableStateOf<List<Franchise>>(emptyList()) }
    var films by remember { mutableStateOf<List<Film>>(emptyList()) }

    LaunchedEffect(universeId) {
        fetchFranchises(universeId) { franchises = it }
        fetchFilms { films = it }
    }

    val franchisesUi = franchises.map { franchise ->
        Franchise(
            id = franchise.id,
            name = franchise.name,
            filmCount = films.count {
                it.universeId == universeId && it.franchiseId == franchise.id
            }
        )
    }.sortedBy { it.name }

    Box(
        modifier = Modifier
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
                text = "FRANCHISES",
                color = colorResource(R.color.accent),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 56.dp)
            )

            Text(
                text = "Collections",
                color = colorResource(R.color.text),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(franchisesUi) { franchise ->
                    FranchiseCard(
                        franchise = franchise,
                        onClick = { onFranchiseClick(franchise.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FranchiseCard(
    franchise: Franchise,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .border(
                width = 1.dp,
                color = colorResource(R.color.card_border),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(R.color.card)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // colored left accent bar
            Box(
                modifier = Modifier
                    .width(3.dp)
                    .height(36.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                colorResource(R.color.accent),
                                colorResource(R.color.accent2)
                            )
                        )
                    )
            )

            Text(
                text = franchise.name,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp),
                color = colorResource(R.color.text),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${franchise.filmCount}",
                    color = colorResource(R.color.accent),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "films",
                    color = colorResource(R.color.text_sub),
                    fontSize = 12.sp
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Open franchise",
                    tint = colorResource(R.color.accent).copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}