package it.fabiomartignoni.exchangeratestracker.model.repositories

import it.fabiomartignoni.exchangeratestracker.model.exchangeratesdatasource.ExchangeRatesDataSource
import it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice.ExchangeRatesPersistenceService
import it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice.room.CurrencyPair

class BaseExchangeRatesRepository(
    private val dataSource: ExchangeRatesDataSource,
    private val persistenceService: ExchangeRatesPersistenceService
    ): ExchangeRatesRepository {

    override suspend fun getExchangeRates(onResult: (List<ExchangeRate>) -> Unit) {
        val trackedPairs = persistenceService.loadPairs()
        dataSource.fetchExchangeRates(trackedPairs.map { "${it.base}${it.counter}" }) {
            val exchangeRates = trackedPairs.map { ExchangeRate(it.base, it.counter, null) }
            if (it != null) {
                for ((index, exchangeRate) in exchangeRates.withIndex()) {
                    exchangeRate.exchangeRate = it[index]
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