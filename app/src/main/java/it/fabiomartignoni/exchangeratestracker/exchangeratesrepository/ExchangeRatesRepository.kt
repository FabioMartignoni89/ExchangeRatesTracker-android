package it.fabiomartignoni.exchangeratestracker.exchangeratesrepository

interface ExchangeRatesRepository {
    fun getExchangeRates(onResult: (List<ExchangeRate>) -> Unit)
    fun getCurrencies(): List<String>
    fun track(base: String, counter: String)
    fun untrack(base: String, counter: String)
}
