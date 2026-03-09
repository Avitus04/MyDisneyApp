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
import fr.isen.segfault.thedisneyapp.R



@Composable
fun EmailField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Email address") },
        singleLine = true,
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .padding(horizontal = 50.dp)
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.button),
            unfocusedPlaceholderColor = colorResource(R.color.text),
            focusedContainerColor = colorResource(R.color.button2),
            focusedTextColor = colorResource(R.color.text),
            focusedLabelColor = colorResource(R.color.teal_200),
            focusedBorderColor = colorResource(R.color.teal_200),
            focusedPlaceholderColor = colorResource(R.color.text)
        )
    )
}

@Composable
fun UsernameField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Username") },
        singleLine = true,
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .padding(horizontal = 50.dp)
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.button),
            unfocusedPlaceholderColor = colorResource(R.color.text),
            focusedContainerColor = colorResource(R.color.button2),
            focusedTextColor = colorResource(R.color.text),
            focusedLabelColor = colorResource(R.color.teal_200),
            focusedBorderColor = colorResource(R.color.teal_200),
            focusedPlaceholderColor = colorResource(R.color.text)
        )
    )
}
@Composable
fun PasswordField(value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        shape = RoundedCornerShape(30.dp),
        modifier = Modifier
            .padding(horizontal = 50.dp)
            .fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = colorResource(R.color.button),
            unfocusedPlaceholderColor = colorResource(R.color.text),
            focusedContainerColor = colorResource(R.color.button2),
            focusedTextColor = colorResource(R.color.text),
            focusedLabelColor = colorResource(R.color.teal_200),
            focusedBorderColor = colorResource(R.color.teal_200),
            focusedPlaceholderColor = colorResource(R.color.text)
        )
    )
}