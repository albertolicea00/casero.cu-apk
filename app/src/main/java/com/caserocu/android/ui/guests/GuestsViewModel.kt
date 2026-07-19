package com.caserocu.android.ui.guests

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caserocu.android.data.CaseroRepository
import com.caserocu.android.data.model.Guest
import com.caserocu.android.data.net.PortalException
import com.caserocu.android.data.net.SessionExpiredException
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed interface GuestsUiState {
    data object Loading : GuestsUiState
    data class Content(val guests: List<Guest>) : GuestsUiState
    data class Error(val message: String) : GuestsUiState
    data object SessionExpired : GuestsUiState
}

class GuestsViewModel(private val repository: CaseroRepository) : ViewModel() {

    var state by mutableStateOf<GuestsUiState>(GuestsUiState.Loading)
        private set

    init {
        load()
    }

    fun load() {
        state = GuestsUiState.Loading
        viewModelScope.launch {
            try {
                val today = LocalDate.now()
                val guests = repository.registeredGuests(
                    from = today.minusMonths(3),
                    to = today.plusMonths(1),
                )
                state = GuestsUiState.Content(guests)
            } catch (e: SessionExpiredException) {
                state = GuestsUiState.SessionExpired
            } catch (e: PortalException) {
                state = GuestsUiState.Error(e.message ?: "Portal error.")
            } catch (e: Exception) {
                state = GuestsUiState.Error(
                    "Could not reach the portal (only reachable from inside Cuba).",
                )
            }
        }
    }

    fun signOut(onSignedOut: () -> Unit) {
        viewModelScope.launch {
            repository.signOut()
            onSignedOut()
        }
    }
}
