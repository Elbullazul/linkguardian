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
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException

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

fun pingUrl(url: String) {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url("https://publicobject.com/helloworld.txt")
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            response.use {
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                for ((name, value) in response.headers) {
                    println("$name: $value")
                }

                println(response.body!!.string())
            }
        }
    })
}

fun OnLoginClick(context: Context, serverUrl: String, apiToken: String) {
    val sharedPref = context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE) ?: return

    with(sharedPref.edit()) {
        putString(dev.elbullazul.linkguardian.PREF_SERVER_URL, serverUrl)
        putString(dev.elbullazul.linkguardian.PREF_API_TOKEN, apiToken)
        apply()

        pingUrl(serverUrl)
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
    }
    else
        ShowToast(context, "savedPrefs is absolutely broken")

    // TODO: move to its own class file
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
                OnLoginClick(
                    context = context,
                    serverUrl = serverUrl.value.text,
                    apiToken = apiToken.value.text
                )
            }
        ) {
            Text(stringResource(id = R.string.login))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LinkGuardianTheme {
        LoginFragment()
    }
}