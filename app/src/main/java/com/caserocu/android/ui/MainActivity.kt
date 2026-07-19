package com.caserocu.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.caserocu.android.CaseroApp
import com.caserocu.android.ui.guests.GuestsScreen
import com.caserocu.android.ui.guests.GuestsViewModel
import com.caserocu.android.ui.login.LoginScreen
import com.caserocu.android.ui.login.LoginViewModel
import com.caserocu.android.ui.theme.CaseroTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = (application as CaseroApp).repository
        val factory = CaseroViewModelFactory(repository)
        setContent {
            CaseroTheme {
                CaseroNavHost(factory)
            }
        }
    }
}

private enum class Screen { LOGIN, GUESTS }

@Composable
private fun CaseroNavHost(factory: CaseroViewModelFactory) {
    var screen by rememberSaveable { mutableStateOf(Screen.LOGIN) }

    when (screen) {
        Screen.LOGIN -> {
            val vm: LoginViewModel = viewModel(factory = factory)
            LoginScreen(viewModel = vm, onSignedIn = { screen = Screen.GUESTS })
        }

        Screen.GUESTS -> {
            val vm: GuestsViewModel = viewModel(factory = factory)
            GuestsScreen(viewModel = vm, onSessionExpired = { screen = Screen.LOGIN })
        }
    }
}
