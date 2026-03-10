package fr.isen.segfault.thedisneyapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import fr.isen.segfault.thedisneyapp.dataClasses.fetchUniverses
import fr.isen.segfault.thedisneyapp.screens.BottomAppBar
import fr.isen.segfault.thedisneyapp.screens.ProfileScreen
import fr.isen.segfault.thedisneyapp.screens.TabBarItem
import fr.isen.segfault.thedisneyapp.screens.UniversesScreen
import fr.isen.segfault.thedisneyapp.ui.theme.MyDisneyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyDisneyAppTheme {
                val navController = rememberNavController()

                val tabBarItems = listOf(
                    TabBarItem(
                        title = "Universes",
                        selectedIcon = Icons.Filled.Home,
                        unselectedIcon = Icons.Outlined.Home
                    ),
                    TabBarItem(
                        title = "Profile",
                        selectedIcon = Icons.Filled.Person,
                        unselectedIcon = Icons.Outlined.Person
                    )
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomAppBar(
                            items = tabBarItems,
                            navController = navController
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "Universes",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("Universes") {
                            UniversesScreen(
                                fetchUniverses = ::fetchUniverses,
                                onUniverseClick = { universeId ->
                                    navController.navigate("franchises/$universeId")
                                }
                            )
                        }
                        composable(
                            route = "franchises/{universeId}",
                            arguments = listOf(
                                navArgument("universeId") {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val universeId =
                                backStackEntry.arguments?.getString("universeId").orEmpty()

                            FranchisesScreen(
                                universeId = universeId,
                                fetchFranchises = ::fetchFranchises,
                                onFranchiseClick = { franchiseId ->
                                    navController.navigate("films/$universeId/$franchiseId")
                                }
                            )
                        }
                        composable(
                            route = "films/{universeId}/{franchiseId}",
                            arguments = listOf(
                                navArgument("universeId") { type = NavType.StringType },
                                navArgument("franchiseId") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val universeId = backStackEntry.arguments?.getString("universeId").orEmpty()
                            val franchiseId = backStackEntry.arguments?.getString("franchiseId").orEmpty()

                            FilmsScreen(
                                universeId = universeId,
                                franchiseId = franchiseId,
                                fetchFilmsByFranchise = ::fetchFilmsByFranchise,
                                onFilmClick = { filmId ->
                                    navController.navigate("filmDetail/$filmId")
                                }
                            )
                        }
                        composable(
                            route = "filmDetail/{filmId}",
                            arguments = listOf(
                                navArgument("filmId") {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val filmId = backStackEntry.arguments?.getString("filmId").orEmpty()

                            FilmDetailScreen(
                                filmId = filmId,
                                fetchFilmById = ::fetchFilmById
                            )
                        }


                        composable("Profile") {
                            val context = LocalContext.current
                            val auth = FirebaseAuth.getInstance()

                            // if not connected, go to Login
                            if (auth.currentUser == null) {
                                context.startActivity(Intent(context, RegistrationActivity::class.java))
                                (context as Activity).finish()
                            } else {
                                ProfileScreen(
                                    onLogout = {
                                        auth.signOut()
                                        context.startActivity(
                                            Intent(
                                                context,
                                                RegistrationActivity::class.java
                                            )
                                        )
                                        (context as Activity).finish()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}