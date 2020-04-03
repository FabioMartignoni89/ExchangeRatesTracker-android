package it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice.room

import androidx.room.Database
import androidx.room.RoomDatabase
import it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice.room.CurrencyPair
import it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice.room.CurrencyPairDao

@Database(entities = arrayOf(CurrencyPair::class), version = 1)
abstract class CurrenciesDatabase : RoomDatabase() {
    abstract fun currencyPairDao(): CurrencyPairDao
}