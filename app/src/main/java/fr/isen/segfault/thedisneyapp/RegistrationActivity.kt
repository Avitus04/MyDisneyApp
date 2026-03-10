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
import com.google.firebase.auth.FirebaseAuth
import fr.isen.segfault.thedisneyapp.screens.LoginScreen
import fr.isen.segfault.thedisneyapp.screens.SignupScreen
import fr.isen.segfault.thedisneyapp.ui.theme.MyDisneyAppTheme
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // check if the user is already connected
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
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
                                (context as Activity).finish()
                            }
                        )
                    }
                    composable("signup") {
                        SignupScreen(
                            onNavigateToLogin = { navController.popBackStack() },
                            onSignupSuccess = {
                                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                                finish()
                            }
                        )
                    }
                }
            }
        }
    }
}