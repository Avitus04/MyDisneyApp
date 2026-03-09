package fr.isen.segfault.thedisneyapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import fr.isen.segfault.thedisneyapp.R
import fr.isen.segfault.thedisneyapp.components.EmailField
import fr.isen.segfault.thedisneyapp.components.PasswordField

@Composable
fun LoginScreen(
    onNavigateToSignup: () -> Unit,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .background(colorResource(R.color.background))
                .fillMaxSize()
        ) {
            Column(
                modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(modifier = Modifier.padding(top = 50.dp)) {
                    Text(
                        text = "Email",
                        fontSize = 20.sp,
                        color = colorResource(R.color.text),
                        modifier = Modifier.padding(start = 65.dp)
                    )
                    EmailField(value = email, onValueChange = { email = it })
                }

                Column(modifier = Modifier.padding(top = 50.dp)) {
                    Text(
                        text = "Password",
                        fontSize = 20.sp,
                        color = colorResource(R.color.text),
                        modifier = Modifier.padding(start = 65.dp)
                    )
                    PasswordField(value = password, onValueChange = { password = it })
                }

                errorMessage?.let {
                    Text(text = it, color = colorResource(R.color.red), modifier = Modifier.padding(top = 8.dp))
                }

                Button(
                    onClick = {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener { onLoginSuccess() }
                            .addOnFailureListener { errorMessage = it.message }
                    },
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Text("Connect")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("No account yet ?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorResource(R.color.text))
                    TextButton(onClick = onNavigateToSignup) {
                        Text("Sign up!", color = colorResource(R.color.signup))
                    }
                }
            }
        }
    }
}