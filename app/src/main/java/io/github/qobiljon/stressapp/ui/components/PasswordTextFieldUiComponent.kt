package io.github.qobiljon.stressapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordTextFieldUiComponent(
    modifier: Modifier = Modifier
) {
    val password = remember {
        mutableStateOf("")
    }

    TextField(
        modifier = modifier.padding(8.dp),
        label = { Text(text = "비밀번호", color = Color.Black) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        textStyle = TextStyle(fontSize = 18.sp),
        value = password.value,
        onValueChange = { password.value = it })

}

@Composable
@Preview(showBackground = true)
fun PasswordTextFieldUiComponentPreview() {
    PasswordTextFieldUiComponent()
}