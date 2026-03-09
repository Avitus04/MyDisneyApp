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
import fr.isen.segfault.thedisneyapp.components.UsernameField


@Composable
fun SignupScreen(
    onNavigateToLogin: () -> Unit,
    onSignupSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val auth = FirebaseAuth.getInstance()
    var email by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
                    Text("Email", fontSize = 20.sp, color = colorResource(R.color.text), modifier = Modifier.padding(start = 65.dp))
                    EmailField(value = email, onValueChange = { email = it })
                }

                Column(modifier = Modifier.padding(top = 50.dp)) {
                    Text("Username", fontSize = 20.sp, color = colorResource(R.color.text), modifier = Modifier.padding(start = 65.dp))
                    UsernameField(value = username, onValueChange = { username = it })
                }

                Column(modifier = Modifier.padding(top = 50.dp)) {
                    Text("Password", fontSize = 20.sp, color = colorResource(R.color.text), modifier = Modifier.padding(start = 65.dp))
                    PasswordField(value = password, onValueChange = { password = it })
                }

                Column(modifier = Modifier.padding(top = 50.dp)) {
                    Text("Confirm password", fontSize = 20.sp, color = colorResource(R.color.text), modifier = Modifier.padding(start = 65.dp))
                    PasswordField(value = confirmPassword, onValueChange = { confirmPassword = it })
                }

                // check if the passwords match
                if (confirmPassword.isNotEmpty() && password != confirmPassword) {
                    Text(
                        text = "Les mots de passe ne correspondent pas",
                        color = colorResource(R.color.red),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                errorMessage?.let {
                    Text(text = it, color = colorResource(R.color.red), modifier = Modifier.padding(top = 8.dp))
                }

                Button(
                    onClick = {
                        when {
                            email.isBlank() || username.isBlank() || password.isBlank() ->
                                errorMessage = "Please fill every field"
                            password != confirmPassword ->
                                errorMessage = "Passwords do not match"
                            password.length < 8 ->
                                errorMessage = "Password must be at least 8 charaters"
                            else -> {
                                auth.createUserWithEmailAndPassword(email, password)
                                    .addOnSuccessListener { onSignupSuccess() }
                                    .addOnFailureListener { errorMessage = it.message }
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Text("Sign up")
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Have an account?", style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.text))
                    TextButton(onClick = onNavigateToLogin) {
                        Text("Login!", color = colorResource(R.color.signup))
                    }
                }
            }
        }
    }
}