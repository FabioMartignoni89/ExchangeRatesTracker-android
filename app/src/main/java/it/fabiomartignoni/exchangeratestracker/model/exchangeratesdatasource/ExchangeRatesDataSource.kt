package it.fabiomartignoni.exchangeratestracker.model.exchangeratesdatasource

interface ExchangeRatesDataSource {
    fun getCurrencies(): List<String>
    fun fetchExchangeRates(currencyPairs: List<String>, onResult: (List<Double>?) -> Unit)
}