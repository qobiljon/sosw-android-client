package io.github.qobiljon.stressapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.qobiljon.stressapp.ui.components.EmailTextFieldUiComponent
import io.github.qobiljon.stressapp.ui.components.PasswordTextFieldUiComponent
import io.github.qobiljon.stressapp.ui.components.SignInButtonUiComponent

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EmailTextFieldUiComponent()
        PasswordTextFieldUiComponent()
        SignInButtonUiComponent(onClick = {})
    }
}

@Composable
@Preview(showBackground = true)
fun SignInScreenPreview() {
    SignInScreen()
}