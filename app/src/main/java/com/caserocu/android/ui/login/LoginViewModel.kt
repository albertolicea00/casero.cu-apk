package com.caserocu.android.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caserocu.android.data.CaseroRepository
import com.caserocu.android.data.net.InvalidCredentialsException
import com.caserocu.android.data.net.PortalException
import kotlinx.coroutines.launch

data class LoginUiState(
    val user: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val error: String? = null,
) {
    val canSubmit: Boolean get() = user.isNotBlank() && password.length >= 8 && !loading
}

class LoginViewModel(private val repository: CaseroRepository) : ViewModel() {

    var state by mutableStateOf(LoginUiState())
        private set

    fun onUserChange(value: String) {
        state = state.copy(user = value, error = null)
    }

    fun onPasswordChange(value: String) {
        state = state.copy(password = value, error = null)
    }

    fun submit(onSuccess: () -> Unit) {
        if (!state.canSubmit) return
        state = state.copy(loading = true, error = null)
        viewModelScope.launch {
            try {
                repository.login(state.user.trim(), state.password)
                state = state.copy(loading = false)
                onSuccess()
            } catch (e: InvalidCredentialsException) {
                state = state.copy(loading = false, error = "Invalid username or password.")
            } catch (e: PortalException) {
                state = state.copy(loading = false, error = e.message)
            } catch (e: Exception) {
                state = state.copy(
                    loading = false,
                    error = "Could not reach the portal (only reachable from inside Cuba).",
                )
            }
        }
    }
}
