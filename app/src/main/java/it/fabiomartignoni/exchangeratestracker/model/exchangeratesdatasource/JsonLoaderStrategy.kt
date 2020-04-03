package it.fabiomartignoni.exchangeratestracker.model.exchangeratesdatasource

interface JsonLoaderStrategy {
    fun loadJson(name: String): String?
}