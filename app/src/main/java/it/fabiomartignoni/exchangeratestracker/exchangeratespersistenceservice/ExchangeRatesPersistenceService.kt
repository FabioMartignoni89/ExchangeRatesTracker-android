package it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice

interface ExchangeRatesPersistenceService {
    fun saveTrackedCurrencyPairs(pairs: List<CurrencyPairDTO>)
    fun loadTrackedCurrencyPairs(): List<CurrencyPairDTO>
}
