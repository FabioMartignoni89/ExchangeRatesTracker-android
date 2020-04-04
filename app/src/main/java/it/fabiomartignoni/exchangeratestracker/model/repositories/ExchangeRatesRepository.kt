package it.fabiomartignoni.exchangeratestracker.model.repositories

interface ExchangeRatesRepository {
    suspend fun getExchangeRates(onResult: (List<ExchangeRate>) -> Unit)
    suspend fun getCurrencies(): List<String>
    suspend fun track(base: String, counter: String)
    suspend fun untrack(base: String, counter: String)
}
