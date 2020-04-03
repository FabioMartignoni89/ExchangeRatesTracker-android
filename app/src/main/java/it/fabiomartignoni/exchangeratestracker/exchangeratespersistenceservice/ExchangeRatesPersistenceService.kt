package it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice

import it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice.room.CurrencyPair

interface ExchangeRatesPersistenceService {
    fun savePair(pair: CurrencyPair)
    fun removePair(pair: CurrencyPair)
    fun loadPairs(): List<CurrencyPair>
}
