package fr.isen.segfault.thedisneyapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Franchises",
            color = colorResource(R.color.text),
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(20.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(franchisesUi) { franchise ->
                FranchiseCard(
                    franchise = franchise,
                    //logoRes = getFranchiseLogoRes(franchise.id),
                    onClick = {
                        onFranchiseClick(franchise.id)
                    }
                )
            }
        }
    }
}

@Composable
fun FranchiseCard(
    franchise: Franchise,
    //logoRes: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
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
            /*Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(92.dp)
                    .background(
                        color = colorResource(R.color.text).copy(alpha = 0.05f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = logoRes),
                    contentDescription = "${franchise.name} logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 12.dp),
                    contentScale = ContentScale.Fit
                )
            }*/

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = franchise.name,
                    modifier = Modifier.weight(1f),
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
                        text = "${franchise.filmCount} films",
                        color = colorResource(R.color.text).copy(alpha = 0.72f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Open franchise",
                        tint = colorResource(R.color.text).copy(alpha = 0.72f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}