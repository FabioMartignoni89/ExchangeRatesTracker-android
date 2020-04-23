package it.fabiomartignoni.exchangeratestracker.model.exchangeratesdatasource

import it.fabiomartignoni.exchangeratestracker.model.entities.RefCity

interface ExchangeRatesDataSource {
    suspend fun getCurrencies(): List<String>
    suspend fun getRefCity(currency: String): RefCity
    suspend fun fetchExchangeRates(currencyPairs: List<String>, onResult: (List<Double>?) -> Unit)
}
