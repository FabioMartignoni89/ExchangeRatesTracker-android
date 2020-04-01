package it.fabiomartignoni.exchangeratestracker.repositories

interface ExchangeRatesRepository {
    fun getExchangeRates()
    fun getCurrencies(): List<String>
    fun track(base: String, counter: String)
    fun untrack(base: String, counter: String)
}
