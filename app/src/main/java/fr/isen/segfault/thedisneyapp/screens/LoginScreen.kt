package fr.isen.segfault.thedisneyapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
    ) {
        // top radial glow
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            colorResource(R.color.accent2).copy(alpha = 0.18f),
                            colorResource(R.color.background).copy(alpha = 0f)
                        ),
                        radius = 700f
                    )
                )
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // page title
            Text(
                text = "WELCOME BACK",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.accent),
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 56.dp)
            )

            // app logo / icon
            Box(
                modifier = Modifier
                    .padding(top = 28.dp)
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                colorResource(R.color.accent),
                                colorResource(R.color.accent2)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.surface)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "App logo",
                        tint = colorResource(R.color.accent),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // login card
            Column(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(R.color.card))
                    .border(1.dp, colorResource(R.color.card_border), RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Text(
                    "Email",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.text_sub),
                    modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                )
                EmailField(value = email, onValueChange = { email = it })

                Text(
                    "Password",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(R.color.text_sub),
                    modifier = Modifier.padding(top = 16.dp, start = 4.dp, bottom = 6.dp)
                )
                PasswordField(value = password, onValueChange = { password = it })

                // error message
                errorMessage?.let {
                    Text(
                        text = "⚠ $it",
                        color = colorResource(R.color.red),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                    )
                }

                // connect button
                Button(
                    onClick = {
                        auth.signInWithEmailAndPassword(email, password)
                            .addOnSuccessListener { onLoginSuccess() }
                            .addOnFailureListener { errorMessage = it.message }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.accent2)
                    )
                ) {
                    Text(
                        "Connect",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(R.color.text)
                    )
                }
            }

            // signup row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 20.dp, bottom = 40.dp)
            ) {
                Text(
                    "No account yet?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorResource(R.color.text_sub)
                )
                TextButton(onClick = onNavigateToSignup) {
                    Text(
                        "Sign up!",
                        color = colorResource(R.color.accent),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}