package com.caserocu.android.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onSignedIn: () -> Unit,
) {
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Text("Casero", style = MaterialTheme.typography.headlineMedium)
        Text("Sign in to the guest portal", style = MaterialTheme.typography.bodyMedium)

        OutlinedTextField(
            value = state.user,
            onValueChange = viewModel::onUserChange,
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
        )

        state.error?.let { message ->
            Text(message, color = MaterialTheme.colorScheme.error)
        }

        Button(
            onClick = { viewModel.submit(onSignedIn) },
            enabled = state.canSubmit,
            modifier = Modifier.fillMaxWidth(),
        ) {
            if (state.loading) {
                CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
            }
            Text("Sign in")
        }
    }
}
