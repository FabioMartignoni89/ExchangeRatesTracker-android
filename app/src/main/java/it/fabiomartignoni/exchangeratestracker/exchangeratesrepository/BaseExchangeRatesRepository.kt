package it.fabiomartignoni.exchangeratestracker.exchangeratesrepository

import it.fabiomartignoni.exchangeratestracker.exchangeratesdatasource.ExchangeRatesDataSource
import it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice.ExchangeRatesPersistenceService
import it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice.room.CurrencyPair

class BaseExchangeRatesRepository(
    private val dataSource: ExchangeRatesDataSource,
    private val persistenceService: ExchangeRatesPersistenceService
    ): ExchangeRatesRepository {

    override fun getExchangeRates(onResult: (List<ExchangeRate>) -> Unit) {
        val trackedPairs = persistenceService.loadPairs()
        dataSource.fetchExchangeRates(trackedPairs.map { "${it.base}${it.counter}" }) {
            val exchangeRates = trackedPairs.map { ExchangeRate(it.base, it.counter, null) }
            if (it != null) {
                var index = 0
                for (exchangeRate in exchangeRates) {
                    exchangeRate.exchangeRate = it[index]
                    index++
                }
            }

            onResult(exchangeRates)
        }
    }

    override fun getCurrencies(): List<String> {
        return dataSource.getCurrencies()
    }

    override fun track(base: String, counter: String) {
        return persistenceService.savePair(CurrencyPair(base, counter))
    }

    override fun untrack(base: String, counter: String) {
        persistenceService.removePair(CurrencyPair(base, counter))
    }
}