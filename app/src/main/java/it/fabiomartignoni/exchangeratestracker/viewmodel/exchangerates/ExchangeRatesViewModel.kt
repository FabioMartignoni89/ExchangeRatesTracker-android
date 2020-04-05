package it.fabiomartignoni.exchangeratestracker.viewmodel.exchangerates

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import it.fabiomartignoni.exchangeratestracker.model.entities.ExchangeRate
import it.fabiomartignoni.exchangeratestracker.model.repositories.ExchangeRatesRepository
import it.fabiomartignoni.exchangeratestracker.view.exchangerates.ExchangeRatesFragmentDirections
import it.fabiomartignoni.exchangeratestracker.view.utils.SingleLiveEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ExchangeRatesViewModel(): ViewModel() {
    private lateinit var repository: ExchangeRatesRepository
    private val fetchDelayMillis: Long = 1000

    private var exchangeRatesModels = MutableLiveData<List<ExchangeRate>>()
    lateinit var exchangeRates: LiveData<List<ExchangeRateDisplayModel>>
    var availableCurrencies = MutableLiveData<List<String>>()
    val onNavigationEvent = SingleLiveEvent<NavDirections>()

    constructor(repository: ExchangeRatesRepository) : this() {
        this.repository = repository
        exchangeRates = Transformations.map(exchangeRatesModels) { exchangeRatesModels ->
            exchangeRatesModels.map { mapExchangeRateToDisplayModel(it) }
        }
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

    fun untrackCurrencyPair(index: Int) {
        GlobalScope.launch {
            if (exchangeRatesModels.value != null &&
                index < exchangeRatesModels.value!!.size) {
                val exchangeRate = exchangeRatesModels.value!!.get(index)
                repository.untrack(exchangeRate.baseCurrency,
                    exchangeRate.counterCurrency)
            }
        }
        fetchAvailableCurrencies()
    }

    fun onExchangeRateSelected(index: Int) {
        val selected = exchangeRatesModels.value?.get(index)
        if (selected != null) {
            val action = ExchangeRatesFragmentDirections
                .actionExchangeRatesFragmentToExchangeRateMapFragment()
            action.baseCurrency = selected.baseCurrency
            action.counterCurrency = selected.counterCurrency
            onNavigationEvent.value = action
        }
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
                            exchangeRatesModels.value = it
                        }
                    }
                }
                mainHandler.postDelayed(this, fetchDelayMillis)
            }
        })
    }

    private fun mapExchangeRateToDisplayModel(it: ExchangeRate): ExchangeRateDisplayModel {
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