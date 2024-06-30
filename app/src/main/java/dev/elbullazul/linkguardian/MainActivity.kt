package dev.elbullazul.linkguardian

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.ui.theme.LinkGuardianTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LinkGuardianTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginFragment(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginFragment(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val serverUrl = remember { mutableStateOf(TextFieldValue("")) }
    val apiToken = remember { mutableStateOf(TextFieldValue("")) }

    val sharedPref = context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE) ?: return
    val savedUrl = sharedPref.getString(PREF_SERVER_URL, EMPTY_STRING)
    val savedToken = sharedPref.getString(PREF_API_TOKEN, EMPTY_STRING)

    if (!savedUrl.isNullOrEmpty() && savedToken.isNullOrEmpty()) {
        ShowToast(context, savedUrl)

        // TODO: login
    }
//    else {
        // TODO: move to separate class
        Column(
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                modifier = modifier
            )
            TextField(
                value = serverUrl.value,
                onValueChange = { serverUrl.value = it },
                label = { Text(stringResource(id = R.string.server_url)) }
            )
            TextField(
                value = apiToken.value,
                onValueChange = { apiToken.value = it },
                label = { Text(stringResource(id = R.string.api_token)) }
            )
            Button(
                onClick = {
                    // TODO: attempt to login

                    with(sharedPref.edit()) {
                        putString(PREF_SERVER_URL, serverUrl.value.text)
                        putString(PREF_API_TOKEN, apiToken.value.text)
                        apply()
                    }
                }
            ) {
                Text(stringResource(id = R.string.login))
            }
        }
//    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LinkGuardianTheme {
        LoginFragment()
    }
}