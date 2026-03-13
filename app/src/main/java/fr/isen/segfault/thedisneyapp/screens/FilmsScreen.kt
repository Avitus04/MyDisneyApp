package fr.isen.segfault.thedisneyapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import fr.isen.segfault.thedisneyapp.R
import fr.isen.segfault.thedisneyapp.dataClasses.Film
import fr.isen.segfault.thedisneyapp.dataClasses.Franchise
import fr.isen.segfault.thedisneyapp.dataClasses.TmdbMovieSearchResponse
import fr.isen.segfault.thedisneyapp.dataClasses.Universe
import fr.isen.segfault.thedisneyapp.dataClasses.UserFilmStatus
import fr.isen.segfault.thedisneyapp.dataClasses.fetchAllUserFilmStatuses
import fr.isen.segfault.thedisneyapp.dataClasses.fetchFilms
import fr.isen.segfault.thedisneyapp.dataClasses.fetchFranchises
import fr.isen.segfault.thedisneyapp.dataClasses.fetchUniverses
import fr.isen.segfault.thedisneyapp.dataClasses.getPosterUrl
import fr.isen.segfault.thedisneyapp.network.TmdbApiClient

enum class SortOption(val label: String) {
    NAME_ASC("Nom A→Z"),
    NAME_DESC("Nom Z→A"),
    DATE_ASC("Date croissante"),
    DATE_DESC("Date décroissante")
}

@Composable
fun FilmsScreen(
    onFilmClick: (String) -> Unit,
    universeIdFilter: String? = null,
    franchiseIdFilter: String? = null
) {
    var allFilms by remember { mutableStateOf<List<Film>>(emptyList()) }
    var allUniverses by remember { mutableStateOf<List<Universe>>(emptyList()) }
    var allFranchises by remember { mutableStateOf<List<Franchise>>(emptyList()) }

    var selectedUniverseId by remember { mutableStateOf(universeIdFilter) }
    var selectedFranchiseId by remember { mutableStateOf(franchiseIdFilter) }
    var selectedGenre by remember { mutableStateOf<String?>(null) }
    var selectedSort by remember { mutableStateOf(SortOption.NAME_ASC) }
    var filmStatuses by remember { mutableStateOf<Map<String, UserFilmStatus>>(emptyMap()) }
    var showOnlyWatched by remember { mutableStateOf(false) }
    var showOnlyWantToWatch by remember { mutableStateOf(false) }

    var showFilterDropdown by remember { mutableStateOf(false) }
    var showSortDropdown by remember { mutableStateOf(false) }
    var showUniversePicker by remember { mutableStateOf(false) }
    var showFranchisePicker by remember { mutableStateOf(false) }
    var showGenrePicker by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fetchFilms { allFilms = it }
        fetchUniverses { allUniverses = it }
        fetchAllUserFilmStatuses { filmStatuses = it }
    }

    LaunchedEffect(selectedUniverseId) {
        selectedUniverseId?.let { uid ->
            fetchFranchises(uid) { allFranchises = it }
        } ?: run { allFranchises = emptyList() }
    }

    val allGenres = allFilms.mapNotNull { it.genre }.filter { it.isNotBlank() }.distinct().sorted()

    val filteredFilms = allFilms
        .filter { film ->
            (selectedUniverseId == null || film.universeId == selectedUniverseId) &&
                    (selectedFranchiseId == null || film.franchiseId == selectedFranchiseId) &&
                    (selectedGenre == null || film.genre == selectedGenre) &&
                    (searchQuery.isBlank() || film.title.contains(searchQuery, ignoreCase = true)) &&
                    (!showOnlyWatched || filmStatuses[film.id]?.watched == true) &&
                    (!showOnlyWantToWatch || filmStatuses[film.id]?.wantToWatch == true)
        }
        .let { list ->
            when (selectedSort) {
                SortOption.NAME_ASC  -> list.sortedBy { it.title }
                SortOption.NAME_DESC -> list.sortedByDescending { it.title }
                SortOption.DATE_ASC  -> list.sortedBy { it.releaseYear ?: Int.MAX_VALUE }
                SortOption.DATE_DESC -> list.sortedByDescending { it.releaseYear ?: 0 }
            }
        }

    val filterActive = selectedUniverseId != null || selectedFranchiseId != null ||
            selectedGenre != null || showOnlyWatched || showOnlyWantToWatch
    val selectedUniverseName = allUniverses.find { it.id == selectedUniverseId }?.name
    val selectedFranchiseName = allFranchises.find { it.id == selectedFranchiseId }?.name

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
                text = "FILMS",
                color = colorResource(R.color.accent),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 56.dp)
            )

            // title row with search toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${filteredFilms.size} titles",
                    color = colorResource(R.color.text),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = {
                    showSearch = !showSearch
                    if (!showSearch) searchQuery = ""
                }) {
                    Icon(
                        imageVector = if (showSearch) Icons.Filled.Close else Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = if (showSearch) colorResource(R.color.accent)
                        else colorResource(R.color.text_sub),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // animated search field
            AnimatedVisibility(
                visible = showSearch,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search a film...", color = colorResource(R.color.text_sub), fontSize = 14.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = colorResource(R.color.surface),
                        unfocusedPlaceholderColor = colorResource(R.color.text_sub),
                        unfocusedBorderColor = colorResource(R.color.card_border),
                        unfocusedTextColor = colorResource(R.color.text),
                        focusedContainerColor = colorResource(R.color.surface),
                        focusedTextColor = colorResource(R.color.text),
                        focusedPlaceholderColor = colorResource(R.color.text_sub),
                        focusedBorderColor = colorResource(R.color.accent),
                        cursorColor = colorResource(R.color.accent)
                    ),
                    leadingIcon = {
                        Icon(Icons.Filled.Search, contentDescription = null, tint = colorResource(R.color.text_sub), modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Filled.Close, contentDescription = "Clear", tint = colorResource(R.color.text_sub), modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                )
            }

            // filter + sort buttons row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // filter button + dropdown
                Box {
                    val filterActive = selectedUniverseId != null || selectedFranchiseId != null
                    OutlinedButton(
                        onClick = { showFilterDropdown = true },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, if (filterActive) colorResource(R.color.accent) else colorResource(R.color.card_border)),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (filterActive) colorResource(R.color.accent_dim) else colorResource(R.color.card)
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.List,
                            contentDescription = "Filter",
                            tint = if (filterActive) colorResource(R.color.accent) else colorResource(R.color.text_sub),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = when {
                                selectedFranchiseName != null -> selectedFranchiseName
                                selectedUniverseName != null  -> selectedUniverseName
                                else -> "Filter"
                            },
                            color = if (filterActive) colorResource(R.color.accent) else colorResource(R.color.text_sub),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 6.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    DropdownMenu(
                        expanded = showFilterDropdown,
                        onDismissRequest = { showFilterDropdown = false; showUniversePicker = false; showFranchisePicker = false; showGenrePicker = false  },
                        modifier = Modifier.background(colorResource(R.color.card)).border(1.dp, colorResource(R.color.card_border), RoundedCornerShape(12.dp))
                    ) {
                        DropdownMenuItem(
                            text = { Text("All films", color = colorResource(R.color.text_sub), fontSize = 13.sp) },
                            onClick = { selectedUniverseId = null;
                                selectedFranchiseId = null;
                                selectedGenre = null;
                                showFilterDropdown = false;
                                showOnlyWatched = false;
                                showOnlyWantToWatch = false },
                            leadingIcon = { Icon(Icons.Filled.Close, contentDescription = null, tint = colorResource(R.color.text_sub), modifier = Modifier.size(16.dp)) }
                        )

                        HorizontalDivider(color = colorResource(R.color.card_border))

// genre picker
                        DropdownMenuItem(
                            text = {
                                Text(
                                    selectedGenre ?: "Genre",
                                    color = colorResource(R.color.text),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            onClick = {
                                showGenrePicker = !showGenrePicker
                                showUniversePicker = false
                                showFranchisePicker = false
                            },
                            trailingIcon = {
                                Icon(
                                    if (showGenrePicker) Icons.Filled.KeyboardArrowUp
                                    else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = colorResource(R.color.accent),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        )

                        if (showGenrePicker) {
                            allGenres.forEach { genre ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            genre,
                                            color = if (selectedGenre == genre) colorResource(R.color.accent)
                                            else colorResource(R.color.text_sub),
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(start = 12.dp)
                                        )
                                    },
                                    onClick = {
                                        selectedGenre = genre
                                        showGenrePicker = false
                                        showFilterDropdown = false
                                    }
                                )
                            }
                        }

                        HorizontalDivider(color = colorResource(R.color.card_border))

                        DropdownMenuItem(
                            text = { Text(selectedUniverseName ?: "Universe", color = colorResource(R.color.text), fontSize = 13.sp, fontWeight = FontWeight.Medium) },
                            onClick = { showUniversePicker = !showUniversePicker; showFranchisePicker = false },
                            trailingIcon = {
                                Icon(
                                    if (showUniversePicker) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = null, tint = colorResource(R.color.accent), modifier = Modifier.size(16.dp)
                                )
                            }
                        )

                        if (showUniversePicker) {
                            allUniverses.forEach { universe ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            universe.name,
                                            color = if (selectedUniverseId == universe.id) colorResource(R.color.accent) else colorResource(R.color.text_sub),
                                            fontSize = 12.sp,
                                            modifier = Modifier.padding(start = 12.dp)
                                        )
                                    },
                                    onClick = { selectedUniverseId = universe.id; selectedFranchiseId = null; showUniversePicker = false }
                                )
                            }
                        }

                        if (selectedUniverseId != null) {
                            HorizontalDivider(color = colorResource(R.color.card_border))

                            DropdownMenuItem(
                                text = { Text(selectedFranchiseName ?: "Franchise", color = colorResource(R.color.text), fontSize = 13.sp, fontWeight = FontWeight.Medium) },
                                onClick = { showFranchisePicker = !showFranchisePicker; showUniversePicker = false },
                                trailingIcon = {
                                    Icon(
                                        if (showFranchisePicker) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                        contentDescription = null, tint = colorResource(R.color.accent), modifier = Modifier.size(16.dp)
                                    )
                                }
                            )

                            if (showFranchisePicker) {
                                allFranchises.forEach { franchise ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                franchise.name,
                                                color = if (selectedFranchiseId == franchise.id) colorResource(R.color.accent) else colorResource(R.color.text_sub),
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(start = 12.dp)
                                            )
                                        },
                                        onClick = { selectedFranchiseId = franchise.id; showFranchisePicker = false; showFilterDropdown = false }
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = colorResource(R.color.card_border))

                        // watched films filter
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Watched",
                                    color = if (showOnlyWatched) colorResource(R.color.accent)
                                    else colorResource(R.color.text),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            onClick = {
                                showOnlyWatched = !showOnlyWatched
                                if (showOnlyWatched) showOnlyWantToWatch = false // mutuellement exclusifs
                            },
                            trailingIcon = {
                                if (showOnlyWatched) {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = colorResource(R.color.accent),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        )

                        // want to watch filter
                        DropdownMenuItem(
                            text = {
                                Text(
                                    "Want to watch",
                                    color = if (showOnlyWantToWatch) colorResource(R.color.accent)
                                    else colorResource(R.color.text),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            },
                            onClick = {
                                showOnlyWantToWatch = !showOnlyWantToWatch
                                if (showOnlyWantToWatch) showOnlyWatched = false // mutuellement exclusifs
                            },
                            trailingIcon = {
                                if (showOnlyWantToWatch) {
                                    Icon(
                                        Icons.Filled.Check,
                                        contentDescription = null,
                                        tint = colorResource(R.color.accent),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        )
                    }
                }

                // sort button + dropdown
                Box {
                    OutlinedButton(
                        onClick = { showSortDropdown = true },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, colorResource(R.color.card_border)),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = colorResource(R.color.card)),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = "Sort",
                            tint = colorResource(R.color.text_sub),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = selectedSort.label,
                            color = colorResource(R.color.text_sub),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showSortDropdown,
                        onDismissRequest = { showSortDropdown = false },
                        modifier = Modifier.background(colorResource(R.color.card)).border(1.dp, colorResource(R.color.card_border), RoundedCornerShape(12.dp))
                    ) {
                        SortOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        option.label,
                                        color = if (selectedSort == option) colorResource(R.color.accent) else colorResource(R.color.text),
                                        fontSize = 13.sp,
                                        fontWeight = if (selectedSort == option) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                },
                                onClick = { selectedSort = option; showSortDropdown = false },
                                trailingIcon = {
                                    if (selectedSort == option) {
                                        Icon(Icons.Filled.Check, contentDescription = null, tint = colorResource(R.color.accent), modifier = Modifier.size(14.dp))
                                    }
                                }
                            )
                        }
                    }
                }
            }

            // films list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredFilms) { film ->
                    FilmCard(
                        film = film,
                        onClick = { onFilmClick(film.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun FilmCard(
    film: Film,
    onClick: () -> Unit
) {
    // fetch poster for this film
    var posterUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(film.id) {
        val call = TmdbApiClient.retrofit.searchMovie(
            title = film.title,
            year = film.releaseYear
        )
        call.enqueue(object : retrofit2.Callback<TmdbMovieSearchResponse> {
            override fun onResponse(
                call: retrofit2.Call<TmdbMovieSearchResponse>,
                response: retrofit2.Response<TmdbMovieSearchResponse>
            ) {
                val movie = response.body()?.results?.firstOrNull()
                posterUrl = getPosterUrl(movie?.poster_path)
            }

            override fun onFailure(
                call: retrofit2.Call<TmdbMovieSearchResponse>,
                t: Throwable
            ) {
                posterUrl = null
            }
        })
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(88.dp)
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
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // poster zone
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(64.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                colorResource(R.color.accent2).copy(alpha = 0.12f),
                                colorResource(R.color.accent).copy(alpha = 0.06f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (posterUrl != null) {
                    AsyncImage(
                        model = posterUrl,
                        contentDescription = "${film.title} poster",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.Face,
                        contentDescription = "Film",
                        tint = colorResource(R.color.accent).copy(alpha = 0.4f),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = film.title,
                        color = colorResource(R.color.text),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2
                    )

                    film.genre?.takeIf { it.isNotBlank() }?.let { genre ->
                        Text(
                            text = genre,
                            color = colorResource(R.color.text_sub),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            modifier = Modifier.padding(top = 3.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = film.releaseYear?.toString() ?: "-",
                        color = colorResource(R.color.accent),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Open film",
                        tint = colorResource(R.color.accent).copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}