package it.fabiomartignoni.exchangeratestracker.model.exchangeratesdatasource

import it.fabiomartignoni.exchangeratestracker.model.entities.RefCity

interface ExchangeRatesDataSource {
    fun getCurrencies(): List<String>
    fun getRefCity(currency: String): RefCity
    fun fetchExchangeRates(currencyPairs: List<String>, onResult: (List<Double>?) -> Unit)
}
