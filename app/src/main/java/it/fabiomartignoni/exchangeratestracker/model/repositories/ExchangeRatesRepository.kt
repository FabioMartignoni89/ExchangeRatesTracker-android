package it.fabiomartignoni.exchangeratestracker.model.repositories

import androidx.lifecycle.LiveData
import it.fabiomartignoni.exchangeratestracker.model.entities.RefCity
import it.fabiomartignoni.exchangeratestracker.model.entities.ExchangeRate

interface ExchangeRatesRepository {
    fun getExchangeRates(): LiveData<List<ExchangeRate>>
    suspend fun getCurrencies(): List<String>
    suspend fun track(base: String, counter: String)
    suspend fun untrack(base: String, counter: String)
    suspend fun getRefCity(currency: String): RefCity
}
