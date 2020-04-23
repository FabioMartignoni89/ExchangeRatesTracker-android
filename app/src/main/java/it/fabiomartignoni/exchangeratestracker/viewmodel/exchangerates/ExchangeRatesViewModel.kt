package it.fabiomartignoni.exchangeratestracker.viewmodel.exchangerates

import androidx.lifecycle.*
import androidx.navigation.NavDirections
import it.fabiomartignoni.exchangeratestracker.model.entities.ExchangeRate
import it.fabiomartignoni.exchangeratestracker.model.repositories.ExchangeRatesRepository
import it.fabiomartignoni.exchangeratestracker.view.exchangerates.ExchangeRatesFragmentDirections
import it.fabiomartignoni.exchangeratestracker.view.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExchangeRatesViewModel(): ViewModel() {
    private lateinit var repository: ExchangeRatesRepository

    lateinit var exchangeRates: LiveData<List<ExchangeRateDisplayModel>>
    var availableCurrencies = MutableLiveData<List<String>>()
    val onNavigationEvent = SingleLiveEvent<NavDirections>()

    constructor(repository: ExchangeRatesRepository) : this() {
        this.repository = repository

        exchangeRates = Transformations.map(repository.getExchangeRates()) { exchangeRatesModels ->
            exchangeRatesModels.map { mapExchangeRateToDisplayModel(it) }
        }

        fetchAvailableCurrencies()
    }

    //region UI actions

    fun trackCurrencyPair(base: String, counter: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.track(base, counter)
        }
        fetchAvailableCurrencies()
    }

    fun untrackCurrencyPair(index: Int) {
        val exchangeRatesModels = repository.getExchangeRates().value
        if (exchangeRatesModels != null &&
            index < exchangeRatesModels.size) {
            val exchangeRate = exchangeRatesModels.get(index)
            viewModelScope.launch(Dispatchers.IO) {
                repository.untrack(
                    exchangeRate.baseCurrency,
                    exchangeRate.counterCurrency
                )
            }
        }
        fetchAvailableCurrencies()
    }

    fun onExchangeRateSelected(index: Int) {
        val selected = repository.getExchangeRates().value?.get(index)
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
        viewModelScope.launch(Dispatchers.IO) {
            availableCurrencies.postValue(repository.getCurrencies())
        }
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