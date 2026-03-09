package fr.isen.segfault.thedisneyapp

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
import androidx.compose.ui.tooling.preview.Preview
import fr.isen.segfault.thedisneyapp.screens.LoginScreen
import fr.isen.segfault.thedisneyapp.ui.theme.MyDisneyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, RegistrationActivity::class.java))
//        enableEdgeToEdge()
//        setContent {
//            MyDisneyAppTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    LoginScreen(
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyDisneyAppTheme {
        Greeting("Android")
    }
}