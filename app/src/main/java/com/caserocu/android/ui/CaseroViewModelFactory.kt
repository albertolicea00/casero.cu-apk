package com.caserocu.android.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caserocu.android.data.CaseroRepository
import com.caserocu.android.ui.guests.GuestsViewModel
import com.caserocu.android.ui.login.LoginViewModel

/** Minimal manual DI: hands the shared [CaseroRepository] to each ViewModel. */
class CaseroViewModelFactory(private val repository: CaseroRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
        modelClass.isAssignableFrom(GuestsViewModel::class.java) -> GuestsViewModel(repository) as T
        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
