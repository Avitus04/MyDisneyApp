package fr.isen.segfault.thedisneyapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.isen.segfault.thedisneyapp.screens.LoginScreen
import fr.isen.segfault.thedisneyapp.screens.SignupScreen
import fr.isen.segfault.thedisneyapp.ui.theme.MyDisneyAppTheme

class RegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).apply { // used for the systems var like the clock on top of the screen
            isAppearanceLightStatusBars = false // false = white icons, true = dark icons
        }
        setContent {
            MyDisneyAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        val context = LocalContext.current
                        LoginScreen(
                            onNavigateToSignup = { navController.navigate("signup") },
                            onLoginSuccess = {
                                context.startActivity(Intent(context, MainActivity::class.java))
                                (context as Activity).finish() // ferme RegistrationActivity
                            }
                        )
                    }
                    composable("signup") {
                        SignupScreen(
                            onNavigateToLogin = { navController.popBackStack() },
                            onSignupSuccess = { navController.navigate("login") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MyDisneyAppTheme {
        Greeting2("Android")
    }
}