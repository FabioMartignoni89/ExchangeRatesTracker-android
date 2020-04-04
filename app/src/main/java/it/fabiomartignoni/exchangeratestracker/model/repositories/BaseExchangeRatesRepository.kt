package it.fabiomartignoni.exchangeratestracker.model.repositories

import it.fabiomartignoni.exchangeratestracker.model.exchangeratesdatasource.ExchangeRatesDataSource
import it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice.ExchangeRatesPersistenceService
import it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice.room.CurrencyPair

class BaseExchangeRatesRepository(
    private val dataSource: ExchangeRatesDataSource,
    private val persistenceService: ExchangeRatesPersistenceService
    ):
    ExchangeRatesRepository {

    companion object {
        val TAG = "BaseExchangeRatesRepository"
    }

    override suspend fun getExchangeRates(onResult: (List<ExchangeRate>) -> Unit) {
        val trackedPairs = persistenceService.loadPairs()
        val apiFormattedPairs = trackedPairs.map { "${it.base}${it.counter}" }
        dataSource.fetchExchangeRates(apiFormattedPairs) { rates ->

            val exchangeRates = trackedPairs.map { ExchangeRate(it.base, it.counter, null) }

            if (rates != null) {
                for ((index, exchangeRate) in exchangeRates.withIndex()) {
                    exchangeRate.exchangeRate = rates[index]
                }

                println("$TAG - New exchange rates values retrieved")
            }

            onResult(exchangeRates)
        }
    }

    override suspend fun getCurrencies(): List<String> {
        return dataSource.getCurrencies()
    }

    override suspend fun track(base: String, counter: String) {
        println("$TAG - $base/$counter tracked")
        return persistenceService.savePair(CurrencyPair(base, counter))
    }

    override suspend fun untrack(base: String, counter: String) {
        println("$TAG - $base/$counter untracked")
        persistenceService.removePair(CurrencyPair(base, counter))
    }
}