import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.io.File
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

@Composable
@Preview
fun App() {
    MainScreen()
}

@Composable
fun MainScreen() {
    var textState by rememberSaveable { mutableStateOf("") }
    var textState2 by rememberSaveable { mutableStateOf("") }
    val myFile = File(textState2)
    var txtError by rememberSaveable { mutableStateOf("") }
    val listConnection: MutableList<String> = arrayListOf()
    var isTrue by rememberSaveable { mutableStateOf(false) }
    var isOpen by rememberSaveable { mutableStateOf(false) }

    val onChange = {it: String ->
        textState = it
        if (textState.isNotEmpty()) isTrue = true
    }

    val onChange2 = {it: String ->
        textState2 = it
    }

    val changeOpen = {
        isOpen = false
    }

    fun validFile(): Boolean {
        if (myFile.extension != "txt" || !myFile.isFile) {
            txtError = "Invalid Format"
            isOpen = true
            return false
        } else if (!myFile.exists() || !myFile.canRead()) {
            txtError = "File Exist ?"
            isOpen = true
            return false
        }
        return true
    }

    fun connectionUrl() {
        if (validFile()) {
            try {
                myFile.bufferedReader().forEachLine {
                    val url = textState+it
                    val connection = URL(url).openConnection() as HttpURLConnection
                    try {
                        val rCode = connection.responseCode
                        val data: String
                        if (rCode in 200..399) {
                            val rMessage = connection.responseMessage
                            val rContent = connection.url.host+connection.url.path
                            data = "$rCode $rMessage: $rContent"
                            listConnection.add(data)
                        }
                    } catch (e: Exception) {
                        txtError = "Error: ${e.message}"
                        isOpen = true
                    } finally {
                        connection.disconnect()
                    }
                }
                textState = ""
            } catch (_: MalformedURLException) {
                txtError = "Error, Invalid Url or .txt Invalid"
                isOpen = true
            }
        } else {
            validFile()
        }
    }
    MaterialTheme {
        Column(
            Modifier.fillMaxSize()
                .background(Color(0f, 0f, 0f, 0.29f)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            edtText(textState, onChange, "URL", "Informe a URL ex: https://exemplo.com/")
            edtText(textState2, onChange2, "URI PATH", "Informe o caminho da wordlist txt")
            btnScan({ connectionUrl() }, isTrue)
            Spacer(modifier = Modifier.height(40.dp))
            Box(
                modifier = Modifier.height(200.dp)
                    .width(500.dp)
                    .background(Color(0.400f, 0.100f, 0.999f, 0.600f), shape = RoundedCornerShape(20.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                LazyColumn{
                    items(listConnection) {
                        textV(it)
                    }
                }
                if (isOpen) {
                    toastDialog(txtError, isOpen, changeOpen)
                }
            }
        }
    }
}

@Composable
fun edtText(textV: String, onChange: (String) -> Unit, label: String, placeholder: String) {
    MaterialTheme {
        OutlinedTextField(
            value = textV,
            onValueChange = onChange,
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            maxLines = 1,
            modifier = Modifier.width(400.dp).padding(6.dp)
        )
    }
}

@Composable
fun btnScan(onChange: () -> Unit, isTrue: Boolean) {
    MaterialTheme {
        Button(
            onClick = onChange,
            enabled = isTrue,
            modifier = Modifier.width(100.dp).height(40.dp)
        ) {
            Text("Scan".uppercase(), Modifier.padding(5.dp), fontSize = 10.sp)
        }
    }
}

@Composable
fun textV(text: String) {
    MaterialTheme {
        Text(text.uppercase(), fontSize = 18.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(20.dp))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun toastDialog(
    text: String,
    isOpenDialog: Boolean,
    changeOpen: () -> Unit
) {

    if (isOpenDialog) {
        AlertDialog(
            onDismissRequest = { },
            confirmButton = {
                Button(onClick = changeOpen ) {
                    Text("OK".uppercase())
                }
            },
            title = { Text("Alert Dialog".uppercase()) },
            text = { Text(text.uppercase()) },
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
