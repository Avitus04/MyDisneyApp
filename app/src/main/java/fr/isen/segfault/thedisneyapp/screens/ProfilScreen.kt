package fr.isen.segfault.thedisneyapp.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import fr.isen.segfault.thedisneyapp.components.PasswordField


@Composable
fun ProfilScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var showPasswordFields by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            Modifier
                .background(colorResource(R.color.background))
                .fillMaxSize()
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // profile icon
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = colorResource(R.color.text),
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .size(80.dp)
                )

                // display email (read-only)
                Text(
                    text = user?.email ?: "Unknown",
                    fontSize = 20.sp,
                    color = colorResource(R.color.text),
                    modifier = Modifier.padding(top = 16.dp)
                )

                // display username (read-only)
                Text(
                    text = user?.displayName ?: "Username not set",
                    fontSize = 16.sp,
                    color = colorResource(R.color.text),
                    modifier = Modifier.padding(top = 8.dp)
                )

                // toggle button to show/hide password change section
                TextButton(
                    onClick = {
                        showPasswordFields = !showPasswordFields
                        errorMessage = null
                        successMessage = null
                        newPassword = ""
                        confirmPassword = ""
                    },
                    modifier = Modifier.padding(top = 32.dp)
                ) {
                    Text(
                        text = if (showPasswordFields) "Cancel" else "Change password",
                        color = colorResource(R.color.signup)
                    )
                }

                // password change fields, visible only when toggled
                if (showPasswordFields) {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            "New password",
                            fontSize = 16.sp,
                            color = colorResource(R.color.text),
                            modifier = Modifier.padding(start = 65.dp)
                        )
                        PasswordField(value = newPassword, onValueChange = { newPassword = it })
                    }

                    Column(modifier = Modifier.padding(top = 16.dp)) {
                        Text(
                            "Confirm password",
                            fontSize = 16.sp,
                            color = colorResource(R.color.text),
                            modifier = Modifier.padding(start = 65.dp)
                        )
                        PasswordField(value = confirmPassword, onValueChange = { confirmPassword = it })
                    }

                    // passwords mismatch warning
                    if (confirmPassword.isNotEmpty() && newPassword != confirmPassword) {
                        Text(
                            text = "Passwords do not match",
                            color = colorResource(R.color.red),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    // error message
                    errorMessage?.let {
                        Text(text = it, color = colorResource(R.color.red), modifier = Modifier.padding(top = 8.dp))
                    }

                    // success message
                    successMessage?.let {
                        Text(text = it, color = colorResource(R.color.green), modifier = Modifier.padding(top = 8.dp))
                    }

                    Button(
                        onClick = {
                            when {
                                newPassword.isBlank() ->
                                    errorMessage = "Please fill every field"
                                newPassword != confirmPassword ->
                                    errorMessage = "Passwords do not match"
                                newPassword.length < 8 ->
                                    errorMessage = "Password must be at least 8 characters"
                                else -> {
                                    user?.updatePassword(newPassword)
                                        ?.addOnSuccessListener {
                                            successMessage = "Password updated!"
                                            errorMessage = null
                                            newPassword = ""
                                            confirmPassword = ""
                                            showPasswordFields = false
                                        }
                                        ?.addOnFailureListener {
                                            errorMessage = it.message
                                        }
                                }
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Update password")
                    }
                }

                // logout button
                Button(
                    onClick = {
                        auth.signOut()
                        onLogout()
                    },
                    modifier = Modifier.padding(top = 32.dp, bottom = 32.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.red))
                ) {
                    Text("Log out")
                }
            }
        }
    }
}