package com.caserocu.android.ui.guests

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.caserocu.android.data.model.Guest
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val DISPLAY_DATE: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yy").withZone(ZoneOffset.UTC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestsScreen(
    viewModel: GuestsViewModel,
    onSessionExpired: () -> Unit,
) {
    val state = viewModel.state

    LaunchedEffect(state) {
        if (state is GuestsUiState.SessionExpired) onSessionExpired()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registered guests") },
                actions = {
                    TextButton(onClick = { viewModel.signOut(onSessionExpired) }) {
                        Text("Sign out")
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,
        ) {
            when (state) {
                is GuestsUiState.Loading -> CircularProgressIndicator()

                is GuestsUiState.Error -> Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Text(state.message, color = MaterialTheme.colorScheme.error)
                    Button(onClick = viewModel::load) { Text("Retry") }
                }

                is GuestsUiState.Content -> if (state.guests.isEmpty()) {
                    Text("No guests in this range.")
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(state.guests, key = { it.stayId }) { guest ->
                            GuestCard(guest)
                        }
                    }
                }

                is GuestsUiState.SessionExpired -> CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun GuestCard(guest: Guest) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(guest.fullName, style = MaterialTheme.typography.titleMedium)
            Text("Passport: ${guest.identificador}")
            guest.nationality?.let { Text("Nationality: $it") }
            guest.checkIn?.let { Text("Check-in: ${DISPLAY_DATE.format(it)}") }
            guest.checkOut?.let { Text("Check-out: ${DISPLAY_DATE.format(it)}") }
        }
    }
}
