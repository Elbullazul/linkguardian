package dev.elbullazul.linkguardian.storage

import android.content.Context

const val PREFERENCES_KEY_FILE = "com.elbullazul.linkguardian.PREFERENCES_KEY_FILE"
const val PREF_DOMAIN = "DOMAIN"
const val PREF_TOKEN = "TOKEN"
const val PREF_SCHEME = "SCHEME"
const val PREF_LINKWARDEN_USERID = "USERID"
const val EMPTY_STRING = ""

const val SCHEME_HTTP = "http"
const val SCHEME_HTTPS = "https"

class PreferencesManager(
    private val context: Context,
    var domain: String = "",
    var token: String = "",
    var scheme: String = SCHEME_HTTP,
    var userId: Int = -1
) {
    fun load(): Boolean {
        val preferences =
            context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE)
        domain = preferences.getString(PREF_DOMAIN, EMPTY_STRING)!!.toString()
        token = preferences.getString(PREF_TOKEN, EMPTY_STRING)!!.toString()
        scheme = preferences.getString(PREF_SCHEME, EMPTY_STRING)!!.toString()
        userId = preferences.getInt(PREF_LINKWARDEN_USERID, -1)

        return domain.isNotEmpty() && token.isNotEmpty()
    }

    fun persist() {
        val preferences =
            context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE)
        with(preferences.edit()) {
            putString(PREF_DOMAIN, domain)
            putString(PREF_TOKEN, token)
            putString(PREF_SCHEME, scheme)
            putInt(PREF_LINKWARDEN_USERID, userId)
            apply()
        }
    }

    fun clear() {
        val preferences =
            context.getSharedPreferences(PREFERENCES_KEY_FILE, Context.MODE_PRIVATE)
        with(preferences.edit()) {

            remove(PREF_DOMAIN)
            remove(PREF_TOKEN)
            remove(PREF_SCHEME)
            remove(PREF_LINKWARDEN_USERID)
            commit()
        }
    }
}