package dev.elbullazul.linkguardian.fragments

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import dev.elbullazul.linkguardian.R

@Composable
fun LoginFragment(authenticate: (String, String) -> Unit, modifier: Modifier = Modifier) {
    val serverUrl = remember { mutableStateOf(TextFieldValue("")) }
    val apiToken = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = modifier.padding(horizontal = 15.dp, vertical = 20.dp).fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
            // TODO: check that URL is valid and token isn't empty!
            onClick = {
                authenticate(serverUrl.value.text, apiToken.value.text)
            }
        ) {
            Text(stringResource(id = R.string.login))
        }
    }
}