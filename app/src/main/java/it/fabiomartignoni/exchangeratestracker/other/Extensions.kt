package it.fabiomartignoni.exchangeratestracker.other

import android.content.res.AssetManager
import java.io.IOException

fun AssetManager.loadJson(fileName: String): String? {
    return try {
        this.open(fileName).bufferedReader().use { it.readText() }
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        null
    }
}