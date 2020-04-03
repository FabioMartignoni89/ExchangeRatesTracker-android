package it.fabiomartignoni.exchangeratestracker

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import it.fabiomartignoni.exchangeratestracker.exchangeratesdatasource.AssetsJsonLoaderStrategy
import it.fabiomartignoni.exchangeratestracker.exchangeratesdatasource.BaseExchangeRatesDataSource
import it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice.RoomExchangeRatesPersistenceService
import it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice.room.CurrenciesDatabase
import it.fabiomartignoni.exchangeratestracker.exchangeratesrepository.BaseExchangeRatesRepository
import it.fabiomartignoni.exchangeratestracker.other.Constants
import it.fabiomartignoni.exchangeratestracker.viewModels.ExchangeRatesViewModel

class ExchangeRatesViewModelFactory(private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val jsonLoader = AssetsJsonLoaderStrategy(context)
        val dataSource = BaseExchangeRatesDataSource(jsonLoader)

        val db = Room.databaseBuilder(
            context,
            CurrenciesDatabase::class.java, Constants.exchangeRatesDbName
        ).build()
        val currencyPairDao = db.currencyPairDao()
        val persistenceService = RoomExchangeRatesPersistenceService(currencyPairDao)

        val repository = BaseExchangeRatesRepository(dataSource, persistenceService)
        return ExchangeRatesViewModel(
            repository
        ) as T
    }

}