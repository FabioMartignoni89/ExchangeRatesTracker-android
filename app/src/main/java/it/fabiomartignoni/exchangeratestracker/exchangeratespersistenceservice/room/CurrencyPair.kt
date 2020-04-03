package it.fabiomartignoni.exchangeratestracker.exchangeratespersistenceservice.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CurrencyPair(
    @ColumnInfo(name = "baseCurrency") val base: String,
    @ColumnInfo(name = "counterCurrency") val counter: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}