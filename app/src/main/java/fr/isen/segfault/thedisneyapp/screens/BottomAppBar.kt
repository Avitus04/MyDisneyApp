package fr.isen.segfault.thedisneyapp.screens

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import fr.isen.segfault.thedisneyapp.R

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun TabBarIcon(isSelected: Boolean, selectedIcon: ImageVector, unselectedIcon: ImageVector, title: String) {
    Icon(
        imageVector = if (isSelected) selectedIcon else unselectedIcon,
        contentDescription = title
    )
}

@Composable
fun BottomAppBar(items: List<TabBarItem>, navController: NavController) {
    var selectedTabIndex by rememberSaveable { mutableStateOf(0) }

    NavigationBar(containerColor = colorResource(R.color.card)) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedTabIndex == index,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(item.title)
                },
                icon = {
                    TabBarIcon(
                        selectedTabIndex == index,
                        item.selectedIcon,
                        item.unselectedIcon,
                        item.title
                    )
                },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorResource(R.color.card),
                    selectedTextColor = colorResource(R.color.text),
                    unselectedIconColor = colorResource(R.color.signup),
                    unselectedTextColor = colorResource(R.color.text)
                )
            )
        }
    }
}