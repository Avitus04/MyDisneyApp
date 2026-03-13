package fr.isen.segfault.thedisneyapp.components

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fr.isen.segfault.thedisneyapp.screens.SortOption

class FilmsViewModel : ViewModel() {
    var selectedUniverseId by mutableStateOf<String?>(null)
    var selectedFranchiseId by mutableStateOf<String?>(null)
    var selectedGenre by mutableStateOf<String?>(null)
    var selectedSort by mutableStateOf(SortOption.NAME_ASC)
    var showOnlyWatched by mutableStateOf(false)
    var showOnlyWantToWatch by mutableStateOf(false)

    fun applyNavFilters(universeId: String?, franchiseId: String?) {
        if (universeId != null) selectedUniverseId = universeId
        if (franchiseId != null) selectedFranchiseId = franchiseId
    }

    fun reset() {
        selectedUniverseId = null
        selectedFranchiseId = null
        selectedGenre = null
        showOnlyWatched = false
        showOnlyWantToWatch = false
    }
}