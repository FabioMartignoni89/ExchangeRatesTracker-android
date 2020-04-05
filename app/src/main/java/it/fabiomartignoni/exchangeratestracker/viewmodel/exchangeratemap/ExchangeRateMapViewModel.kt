package it.fabiomartignoni.exchangeratestracker.viewmodel.exchangeratemap

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import it.fabiomartignoni.exchangeratestracker.model.entities.ExchangeRate
import it.fabiomartignoni.exchangeratestracker.model.entities.RefCity
import it.fabiomartignoni.exchangeratestracker.model.repositories.ExchangeRatesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExchangeRateMapViewModel(): ViewModel() {
    private lateinit var repository: ExchangeRatesRepository
    private lateinit var exchangeRate: LiveData<ExchangeRate>
    private var refCity: RefCity? = null
    private lateinit var selectedBaseCurrency: String
    private lateinit var selectedCounterCurrency: String

    lateinit var cityMarkerDisplayModel: LiveData<CityMarkerDisplayModel?>

    constructor(repository: ExchangeRatesRepository,
                selectedBaseCurrency: String,
                selectedCounterCurrency: String) : this() {
        this.repository = repository
        this.selectedBaseCurrency = selectedBaseCurrency
        this.selectedCounterCurrency = selectedCounterCurrency

        GlobalScope.launch {
            refCity = repository.getRefCity(selectedBaseCurrency)
        }

        exchangeRate = Transformations.map(repository.getExchangeRates()) { exchangeRates ->
            exchangeRates.first {
                it.baseCurrency == selectedBaseCurrency &&
                        it.counterCurrency == selectedCounterCurrency
            }
        }

        cityMarkerDisplayModel = Transformations.map(exchangeRate) {
            CityMarkerDisplayModel(refCity?.name ?: "-",
                refCity?.latitude ?: 0.0,
                refCity?.longitude ?: 0.0,
                "${it.baseCurrency}/${it.counterCurrency} = ${it.exchangeRate ?: "-"}")
        }
    }
}
