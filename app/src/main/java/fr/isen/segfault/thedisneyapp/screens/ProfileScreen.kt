package fr.isen.segfault.thedisneyapp.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.isen.segfault.thedisneyapp.R
import fr.isen.segfault.thedisneyapp.components.PasswordField
import fr.isen.segfault.thedisneyapp.dataClasses.OwnedFilmUi
import fr.isen.segfault.thedisneyapp.dataClasses.fetchOwnedFilms
import fr.isen.segfault.thedisneyapp.dataClasses.removeOwnedFilm
import fr.isen.segfault.thedisneyapp.dataClasses.updateWantToGetRid

@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var ownedFilms by remember { mutableStateOf<List<OwnedFilmUi>>(emptyList()) }

    // fetch username from DB with LaunchedEffect
    var username by remember { mutableStateOf(user?.displayName ?: "") }
    LaunchedEffect(user?.uid) {
        user?.uid?.let { uid ->
            FirebaseDatabase.getInstance().reference
                .child("users").child(uid)
                .get()
                .addOnSuccessListener { snapshot ->
                    username = snapshot.child("username").getValue(String::class.java)
                        ?: user.displayName
                                ?: "No Username"
                }
            fetchOwnedFilms {
                ownedFilms = it
            }
        }
    }

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var showPasswordFields by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.background))
    ) {
        // top radial glow
        Box(
            modifier = Modifier
                .fillMaxSize()
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
                text = "MY PROFILE",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.accent),
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 56.dp)
            )

            // avatar with glowing border
            Box(
                modifier = Modifier
                    .padding(top = 28.dp)
                    .size(108.dp)
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
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(colorResource(R.color.surface)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Profile",
                        tint = colorResource(R.color.accent),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // username
            Text(
                text = username,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.text),
                modifier = Modifier.padding(top = 16.dp)
            )

            // email pill badge
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorResource(R.color.accent_dim))
                    .border(1.dp, colorResource(R.color.accent).copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = user?.email ?: "Unknown",
                    fontSize = 13.sp,
                    color = colorResource(R.color.accent)
                )
            }

            Column(
                modifier = Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(R.color.card))
                    .border(1.dp, colorResource(R.color.card_border), RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                // card header row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = colorResource(R.color.gold),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Security",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.text),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    )
                    TextButton(
                        onClick = {
                            showPasswordFields = !showPasswordFields
                            errorMessage = null
                            successMessage = null
                            newPassword = ""
                            confirmPassword = ""
                        },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = if (showPasswordFields) "Cancel" else "Change password",
                            color = if (showPasswordFields) colorResource(R.color.text_sub)
                            else colorResource(R.color.accent),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // animated password fields
                AnimatedVisibility(
                    visible = showPasswordFields,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    Column {
                        HorizontalDivider(
                            color = colorResource(R.color.card_border),
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        Text(
                            "New password",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(R.color.text_sub),
                            modifier = Modifier.padding(top = 20.dp, start = 4.dp, bottom = 6.dp)
                        )
                        PasswordField(value = newPassword, onValueChange = { newPassword = it })

                        Text(
                            "Confirm password",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = colorResource(R.color.text_sub),
                            modifier = Modifier.padding(top = 16.dp, start = 4.dp, bottom = 6.dp)
                        )
                        PasswordField(value = confirmPassword, onValueChange = { confirmPassword = it })

                        // passwords mismatch warning
                        if (confirmPassword.isNotEmpty() && newPassword != confirmPassword) {
                            Text(
                                text = "Passwords do not match",
                                color = colorResource(R.color.red),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                            )
                        }

                        // error message
                        errorMessage?.let {
                            Text(
                                text = "$it",
                                color = colorResource(R.color.red),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                            )
                        }

                        // success message
                        successMessage?.let {
                            Text(
                                text = "$it",
                                color = colorResource(R.color.green),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(top = 8.dp, start = 4.dp)
                            )
                        }

                        // update password button
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
                                "Update password",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = colorResource(R.color.text)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorResource(R.color.card))
                    .border(1.dp, colorResource(R.color.card_border), RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Text(
                    text = "Owned films",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.text),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (ownedFilms.isEmpty()) {
                    Text(
                        text = "No owned films",
                        fontSize = 13.sp,
                        color = colorResource(R.color.text_sub)
                    )
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ownedFilms.forEach { film ->
                            OwnedFilmCard(
                                film = film,
                                onRemove = {
                                    removeOwnedFilm(
                                        filmId = film.filmId,
                                        onSuccess = {
                                            fetchOwnedFilms { ownedFilms = it }
                                        }
                                    )
                                },
                                onToggleGetRid = {
                                    updateWantToGetRid(
                                        filmId = film.filmId,
                                        value = !film.wantToGetRid,
                                        onSuccess = {
                                            fetchOwnedFilms { ownedFilms = it }
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // logout button
            Button(
                onClick = {
                    auth.signOut()
                    onLogout()
                },
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 40.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(1.dp, colorResource(R.color.red).copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.red).copy(alpha = 0.12f)
                )
            ) {
                Text(
                    "Log out",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = colorResource(R.color.red)
                )
            }
        }
    }
}

@Composable
fun OwnedFilmCard(
    film: OwnedFilmUi,
    onRemove: () -> Unit,
    onToggleGetRid: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colorResource(R.color.surface))
            .border(
                1.dp,
                colorResource(R.color.card_border),
                RoundedCornerShape(14.dp)
            )
            .padding(14.dp)
    ) {
        Text(
            text = buildString {
                append(film.title)
                film.releaseYear?.let { append(" (${it})") }
            },
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.text),
            modifier = Modifier.padding(8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onToggleGetRid,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (film.wantToGetRid)
                        colorResource(R.color.gold).copy(alpha = 0.25f)
                    else
                        colorResource(R.color.accent_dim)
                )
            ) {
                Text(
                    text = if (film.wantToGetRid) "Marked to get rid" else "Mark to get rid",
                    fontSize = 10.sp,
                    color = colorResource(R.color.text)
                )
            }

            Button(
                onClick = onRemove,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.red).copy(alpha = 0.18f)
                )
            ) {
                Text(
                    text = "Remove from owned",
                    fontSize = 10.sp,
                    color = colorResource(R.color.red)
                )
            }
        }
    }
}