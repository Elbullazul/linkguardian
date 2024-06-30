package dev.elbullazul.linkguardian

import android.content.Context
import android.widget.Toast

val PREFERENCES_KEY_FILE = "com.elbullazul.linkguardian.PREFERENCES_KEY_FILE"
val PREF_SERVER_URL = "SERVER_URL"
val PREF_API_TOKEN = "API_TOKEN"
val EMPTY_STRING = ""

fun ShowToast(context: Context, text: String) {
    Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
}