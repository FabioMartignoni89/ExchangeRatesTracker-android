package it.fabiomartignoni.exchangeratestracker.viewmodel.exchangerates

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import it.fabiomartignoni.exchangeratestracker.model.repositories.factories.BaseExchangeRatesRepositoryFactory

class ExchangeRatesViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val factory = BaseExchangeRatesRepositoryFactory(context)
        return ExchangeRatesViewModel(
            factory.makeRepository()
        ) as T
    }
}