package fr.isen.segfault.thedisneyapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import fr.isen.segfault.thedisneyapp.screens.LoginScreen
import fr.isen.segfault.thedisneyapp.screens.SignupScreen
import fr.isen.segfault.thedisneyapp.ui.theme.MyDisneyAppTheme
class RegistrationActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContent {
            MyDisneyAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {},
                            navigationIcon = {
                                IconButton(onClick = {
                                    when (currentRoute) {
                                        "Login" -> {
                                            startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                                            finish()
                                        }
                                        "Signup" -> navController.popBackStack()
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                        tint = colorResource(R.color.accent)
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.Transparent
                            )
                        )
                    },
                    containerColor = colorResource(R.color.background)
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "Login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("Login") {
                            val context = LocalContext.current
                            LoginScreen(
                                onNavigateToSignup = { navController.navigate("Signup") },
                                onLoginSuccess = {
                                    context.startActivity(
                                        Intent(context, MainActivity::class.java).apply {
                                            putExtra("startDestination", "Profile")
                                        }
                                    )
                                    (context as Activity).finish()
                                }
                            )
                        }
                        composable("Signup") {
                            SignupScreen(
                                onNavigateToLogin = { navController.popBackStack() },
                                onSignupSuccess = {
                                    startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                                    finish()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}