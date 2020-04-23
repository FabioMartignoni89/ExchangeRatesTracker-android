package it.fabiomartignoni.exchangeratestracker.model.repositories

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import it.fabiomartignoni.exchangeratestracker.model.entities.RefCity
import it.fabiomartignoni.exchangeratestracker.model.entities.ExchangeRate
import it.fabiomartignoni.exchangeratestracker.model.exchangeratesdatasource.ExchangeRatesDataSource
import it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice.ExchangeRatesPersistenceService
import it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice.room.CurrencyPair
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BaseExchangeRatesRepository(
    private val dataSource: ExchangeRatesDataSource,
    private val persistenceService: ExchangeRatesPersistenceService
) : ExchangeRatesRepository {

    companion object {
        const val TAG = "BaseExchangeRatesRepository"
    }

    private val exchangeRates = MutableLiveData<List<ExchangeRate>>()
    private val fetchDelayMillis: Long = 1000

    init {
        startFetchExchangeRates()
    }

    override fun getExchangeRates(): LiveData<List<ExchangeRate>> {
        return exchangeRates
    }

    override suspend fun getCurrencies(): List<String> {
        return dataSource.getCurrencies()
    }

    override suspend fun track(base: String, counter: String) {
        if (base != counter && base != "" && counter != "") {
            println("$TAG - $base/$counter tracked")
            persistenceService.savePair(CurrencyPair(base, counter))
        }
    }

    override suspend fun untrack(base: String, counter: String) {
        println("$TAG - $base/$counter untracked")
        persistenceService.removePair(CurrencyPair(base, counter))
    }

    override suspend fun getRefCity(currency: String): RefCity {
        return dataSource.getRefCity(currency)
    }

    //region private

    private fun startFetchExchangeRates() {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if (exchangeRates.hasActiveObservers()) {
                    GlobalScope.launch {
                        fetchExchangeRates()
                    }
                }
                mainHandler.postDelayed(this, fetchDelayMillis)
            }
        })
    }

    private suspend fun fetchExchangeRates() {
        val trackedPairs = persistenceService.loadPairs()
        val apiFormattedPairs = trackedPairs.map { "${it.base}${it.counter}" }
        dataSource.fetchExchangeRates(apiFormattedPairs) { rates ->

            val exchangeRates = trackedPairs.map {
                ExchangeRate(
                    it.base,
                    it.counter,
                    null
                )
            }

            if (rates != null) {
                for ((index, exchangeRate) in exchangeRates.withIndex()) {
                    exchangeRate.exchangeRate = rates[index]
                }

                println("$TAG - New exchange rates values retrieved")
            }

            this.exchangeRates.postValue(exchangeRates)
        }
    }

    //endregion
}