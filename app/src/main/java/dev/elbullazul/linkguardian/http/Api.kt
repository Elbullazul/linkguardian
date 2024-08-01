package dev.elbullazul.linkguardian.http

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException

@Throws(okio.IOException::class)
fun get(url: String, requiresAuth: Boolean = false): String {
    val client = OkHttpClient()
    var responseBody = ""

    val request = if (requiresAuth) {
        val token = ""      // TODO: load token

        Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $token")
            .build()
    } else {
        Request.Builder()
            .url(url)
            .build()
    }

    client.newCall(request).enqueue(
        object : Callback {
            override fun onFailure(
                call: Call,
                e: IOException,
            ) {
                e.printStackTrace()
            }

            override fun onResponse(
                call: Call,
                response: Response,
            ) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    responseBody = response.body!!.string()
                }
            }
        },
    )

    return responseBody
}

// mostly the same as get, except for the .post() call
@Throws(okio.IOException::class)
fun post(url: String, body: String, requiresAuth: Boolean = false) {
    val client = OkHttpClient()

    val request = if (requiresAuth) {
        val token = ""      // TODO: load token

        Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $token")
            .post(body.toRequestBody())
            .build()
    } else {
        Request.Builder()
            .url(url)
            .post(body.toRequestBody())
            .build()
    }

    client.newCall(request).execute().use { response ->
        if (!response.isSuccessful) throw IOException("Unexpected code $response")

        println(response.body!!.string())
    }
}

fun ping(url: String): Boolean {
    try {
        get(url)
    } catch (e: IOException) {
        println("IO error: no internet?")
        return false;
    } catch (e: IllegalArgumentException) {
        println("Invalid URL")
        return false;
    } catch (e: Exception) {
        print(e.toString())
        println(e.message)
        return false;
    }

    return true;
}