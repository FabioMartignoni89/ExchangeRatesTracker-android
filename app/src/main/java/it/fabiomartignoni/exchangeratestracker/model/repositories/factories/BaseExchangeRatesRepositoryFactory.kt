package it.fabiomartignoni.exchangeratestracker.model.repositories.factories

import android.content.Context
import androidx.room.Room
import it.fabiomartignoni.exchangeratestracker.model.exchangeratesdatasource.AssetsJsonLoaderStrategy
import it.fabiomartignoni.exchangeratestracker.model.exchangeratesdatasource.BaseExchangeRatesDataSource
import it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice.RoomExchangeRatesPersistenceService
import it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice.room.CurrenciesDatabase
import it.fabiomartignoni.exchangeratestracker.model.repositories.BaseExchangeRatesRepository
import it.fabiomartignoni.exchangeratestracker.model.repositories.ExchangeRatesRepository
import it.fabiomartignoni.exchangeratestracker.other.Constants
import it.fabiomartignoni.exchangeratestracker.other.Endpoints

class BaseExchangeRatesRepositoryFactory(private val context: Context): ExchangeRatesRepositoryFactory {
    override fun makeRepository(): ExchangeRatesRepository {
        val jsonLoader = AssetsJsonLoaderStrategy(context)
        val dataSource = BaseExchangeRatesDataSource(jsonLoader,
            Constants.currenciesJsonFileName,
            Endpoints.fetchExchangeRates)

        val db = Room.databaseBuilder(
            context,
            CurrenciesDatabase::class.java, Constants.exchangeRatesDbName
        ).build()
        val currencyPairDao = db.currencyPairDao()
        val persistenceService = RoomExchangeRatesPersistenceService(currencyPairDao)

        val repository = BaseExchangeRatesRepository(dataSource, persistenceService)
        return repository
    }
}