package it.fabiomartignoni.exchangeratestracker.model.exchangeratespersistenceservice.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CurrencyPairDao {
    @Query("SELECT * FROM CurrencyPair")
    fun getAll(): List<CurrencyPair>

    @Query("SELECT * FROM CurrencyPair " +
            "WHERE baseCurrency LIKE :base " +
            "AND counterCurrency LIKE :counter " +
            "LIMIT 1")
    fun find(base: String, counter: String): CurrencyPair?

    @Insert
    fun insert(pair: CurrencyPair)

    @Query("DELETE FROM CurrencyPair " +
            "WHERE baseCurrency LIKE :base " +
            "AND counterCurrency LIKE :counter")
    fun delete(base: String, counter: String)

    @Query("DELETE FROM CurrencyPair")
    fun deleteAll()
}