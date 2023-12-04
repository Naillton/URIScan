import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var textState by rememberSaveable { mutableStateOf("") }

    val onChange = {it: String ->
        textState = it
    }

    MaterialTheme {
        Column(
            Modifier.fillMaxSize()
                .background(Color(0f, 0f, 0f, 0.29f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            edtText(textState, onChange)
        }
    }
}

@Composable
fun edtText(text: String, onChange: (String) -> Unit) {
    MaterialTheme {
        OutlinedTextField(
            value = text,
            onValueChange = onChange,
            label = { Text("URL") },
            placeholder = { Text("Informe a URL ex: https://exemplo.com/") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            maxLines = 1,
            modifier = Modifier.width(400.dp).padding(10.dp)
        )
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}