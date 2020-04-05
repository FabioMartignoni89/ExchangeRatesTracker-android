package it.fabiomartignoni.exchangeratestracker.viewmodel.exchangeratemap

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import it.fabiomartignoni.exchangeratestracker.model.entities.RefCity
import it.fabiomartignoni.exchangeratestracker.model.entities.ExchangeRate
import it.fabiomartignoni.exchangeratestracker.model.repositories.ExchangeRatesRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ExchangeRateMapViewModel(): ViewModel() {
    private lateinit var repository: ExchangeRatesRepository
    private val fetchDelayMillis: Long = 1000
    private var exchangeRate = MutableLiveData<ExchangeRate>()
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

        cityMarkerDisplayModel = Transformations.map(exchangeRate) {
            CityMarkerDisplayModel(refCity?.name ?: "-",
                refCity?.latitude ?: 0.0,
                refCity?.longitude ?: 0.0,
                "${it.baseCurrency}/${it.counterCurrency} = ${it.exchangeRate ?: "-"}")
        }

        startFetchExchangeRates(repository)
    }

    private fun startFetchExchangeRates(repository: ExchangeRatesRepository) {
        val mainHandler = Handler(Looper.getMainLooper())
        mainHandler.post(object : Runnable {
            override fun run() {
                if (cityMarkerDisplayModel.hasActiveObservers()) {
                    GlobalScope.launch {
                        repository.getExchangeRates { it ->
                            exchangeRate.value = it.first {
                                it.baseCurrency == selectedBaseCurrency &&
                                it.counterCurrency == selectedCounterCurrency
                            }
                        }
                    }
                }
                mainHandler.postDelayed(this, fetchDelayMillis)
            }
        })
    }
}
