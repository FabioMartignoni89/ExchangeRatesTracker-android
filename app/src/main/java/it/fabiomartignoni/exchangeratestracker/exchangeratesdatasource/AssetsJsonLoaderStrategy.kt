package it.fabiomartignoni.exchangeratestracker.exchangeratesdatasource

import android.content.Context
import it.fabiomartignoni.exchangeratestracker.other.loadJson

class AssetsJsonLoaderStrategy(private val context: Context) :
    JsonLoaderStrategy {
    override fun loadJson(name: String): String? {
        return context.assets.loadJson(name)
    }
}