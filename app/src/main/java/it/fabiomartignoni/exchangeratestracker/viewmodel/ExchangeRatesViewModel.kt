package it.fabiomartignoni.exchangeratestracker.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.fabiomartignoni.exchangeratestracker.model.repositories.ExchangeRate
import it.fabiomartignoni.exchangeratestracker.model.repositories.ExchangeRatesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExchangeRatesViewModel(): ViewModel() {
    private lateinit var repository: ExchangeRatesRepository
    private val fetchDelayMillis: Long = 1000

    var exchangeRates = MutableLiveData<List<ExchangeRateDisplayModel>>()
    var availableCurrencies = MutableLiveData<List<String>>()

    constructor(repository: ExchangeRatesRepository) : this() {
        this.repository = repository
        fetchAvailableCurrencies()
        startFetchExchangeRates(repository)
    }

    //region UI actions

    fun trackCurrencyPair(base: String, counter: String) {
        GlobalScope.launch {
            repository.track(base, counter)
        }
        fetchAvailableCurrencies()
    }

    fun untrackCurrencyPair(base: String, counter: String) {
        GlobalScope.launch {
            repository.untrack(base, counter)
        }
        fetchAvailableCurrencies()
    }

    //endregion

    //region private

    private fun fetchAvailableCurrencies() {
        GlobalScope.launch {
            availableCurrencies.postValue(repository.getCurrencies())
        }
    }

    private fun startFetchExchangeRates(repository: ExchangeRatesRepository) {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if (exchangeRates.hasActiveObservers()) {
                    GlobalScope.launch {
                        repository.getExchangeRates { it ->
                            exchangeRates.value = it.map { mapExchangeRate(it) }
                        }
                    }
                }
                mainHandler.postDelayed(this, fetchDelayMillis)
            }
        })
    }

    private fun mapExchangeRate(it: ExchangeRate): ExchangeRateDisplayModel {
        val currencyPairFormatted = "${it.baseCurrency}/${it.counterCurrency}"
        var exchangeRateFormatted = "-"
        if (it.exchangeRate != null) {
            exchangeRateFormatted = "${it.exchangeRate}"
        }
        return ExchangeRateDisplayModel(
            currencyPairFormatted,
            exchangeRateFormatted
        )
    }

    //endregion
}