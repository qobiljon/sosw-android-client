package io.github.qobiljon.stressapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.qobiljon.stressapp.ui.screens.SignInScreen

@Composable
fun SignInButtonUiComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    TextButton(
        modifier = modifier
            .padding(top = 16.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White
        ),
        onClick = { onClick() }) {
        Text(
            text = "로그인",
            style = TextStyle(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignInButtonUiComponentPreview() {
    SignInButtonUiComponent() {

    }
}