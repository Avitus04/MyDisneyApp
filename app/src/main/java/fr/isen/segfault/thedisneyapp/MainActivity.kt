package fr.isen.segfault.thedisneyapp

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
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.segfault.thedisneyapp.dataClasses.fetchUniverses
import fr.isen.segfault.thedisneyapp.screens.LoginScreen
import fr.isen.segfault.thedisneyapp.screens.SignupScreen
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
                        title = "Profil",
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
                            UniversesScreen(fetchUniverses = ::fetchUniverses)
                        }
                        composable("Profil") {
                            val context = LocalContext.current
                            LoginScreen(
                                onNavigateToSignup = { navController.navigate("signup") },
                                onLoginSuccess = { /* à gérer */ }
                            )
                        }
                        composable("signup") {
                            SignupScreen(
                                onNavigateToLogin = { navController.popBackStack() },
                                onSignupSuccess = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}