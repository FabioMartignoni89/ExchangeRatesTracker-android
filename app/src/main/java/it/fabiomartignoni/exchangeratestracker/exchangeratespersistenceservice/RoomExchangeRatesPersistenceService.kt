package it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice

import it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice.room.CurrencyPair
import it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice.room.CurrencyPairDao

class RoomExchangeRatesPersistenceService(private val dao: CurrencyPairDao):
    ExchangeRatesPersistenceService {

    override fun savePair(pair: CurrencyPair) {
        if (dao.find(pair.base, pair.counter) == null) {
            dao.insert(pair)
        }
    }

    override fun removePair(pair: CurrencyPair) {
        dao.delete(pair.base, pair.counter)
    }

    override fun loadPairs(): List<CurrencyPair> {
        return dao.getAll()
    }
}

