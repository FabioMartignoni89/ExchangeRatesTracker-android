package it.fabiomartignoni.exchangeratestracker.exchangeratesdatasource

interface JsonLoaderStrategy {
    fun loadJson(name: String): String?
}