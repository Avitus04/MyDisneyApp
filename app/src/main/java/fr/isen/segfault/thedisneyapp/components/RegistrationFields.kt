package fr.isen.segfault.thedisneyapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.segfault.thedisneyapp.R


@Composable
fun EmailField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Email address", fontSize = 14.sp) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.surface),
            unfocusedPlaceholderColor = colorResource(R.color.text_sub),
            unfocusedBorderColor = colorResource(R.color.card_border),
            unfocusedTextColor = colorResource(R.color.text),
            focusedContainerColor = colorResource(R.color.surface),
            focusedTextColor = colorResource(R.color.text),
            focusedPlaceholderColor = colorResource(R.color.text_sub),
            focusedBorderColor = colorResource(R.color.accent),
        )
    )
}

@Composable
fun UsernameField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Username", fontSize = 14.sp) },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.surface),
            unfocusedPlaceholderColor = colorResource(R.color.text_sub),
            unfocusedBorderColor = colorResource(R.color.card_border),
            unfocusedTextColor = colorResource(R.color.text),
            focusedContainerColor = colorResource(R.color.surface),
            focusedTextColor = colorResource(R.color.text),
            focusedPlaceholderColor = colorResource(R.color.text_sub),
            focusedBorderColor = colorResource(R.color.accent),
        )
    )
}

@Composable
fun PasswordField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Password", fontSize = 14.sp) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.surface),
            unfocusedPlaceholderColor = colorResource(R.color.text_sub),
            unfocusedBorderColor = colorResource(R.color.card_border),
            unfocusedTextColor = colorResource(R.color.text),
            focusedContainerColor = colorResource(R.color.surface),
            focusedTextColor = colorResource(R.color.text),
            focusedPlaceholderColor = colorResource(R.color.text_sub),
            focusedBorderColor = colorResource(R.color.accent),
        )
    )
}