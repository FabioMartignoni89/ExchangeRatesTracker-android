package it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(CurrencyPair::class), version = 1)
abstract class CurrenciesDatabase : RoomDatabase() {
    abstract fun currencyPairDao(): CurrencyPairDao
}