package it.fabiomartignoni.exchangeratestracker.viewmodel.exchangeratemap

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.fabiomartignoni.exchangeratestracker.model.repositories.factories.BaseExchangeRatesRepositoryFactory

class ExchangeRateMapViewModelFactory(private val context: Context,
                                      private val baseCurrency: String,
                                      private val counterCurrency: String
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val factory = BaseExchangeRatesRepositoryFactory(context)
        return ExchangeRateMapViewModel(
            factory.makeRepository(),
            baseCurrency,
            counterCurrency
        ) as T
    }
}
