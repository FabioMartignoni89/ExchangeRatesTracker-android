package it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice

import it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice.room.CurrencyPair

interface ExchangeRatesPersistenceService {
    fun savePair(pair: CurrencyPair)
    fun removePair(pair: CurrencyPair)
    fun loadPairs(): List<CurrencyPair>
}
