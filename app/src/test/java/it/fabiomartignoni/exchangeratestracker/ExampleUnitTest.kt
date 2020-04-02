package it.fabiomartignoni.exchangeratestracker

import CurrenciesDTO
import ExchangeRatesDTO
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.junit.Test

import org.junit.Assert.*
import java.io.IOException
import kotlin.reflect.typeOf

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun canParseCurrencies() {
        val currenciesDTO = Gson().fromJson(currenciesJson, CurrenciesDTO::class.java)
        val JPY = currenciesDTO.worldCurrencies[5]
        assertEquals("JPY", JPY.currency)
        assertEquals("Tokyo", JPY.city.name)
        assertEquals(35.652832, JPY.city.lat, 0.000001)
        assertEquals(139.839478, JPY.city.lon, 0.000001)
    }

    @ExperimentalStdlibApi
    @Test
    fun canParseExchangeRates() {
        val DTOType = object : TypeToken<HashMap<String, Double>>() {}.type
        val exchangeRates: HashMap<String, Double> = Gson().fromJson(exchangeRatesJson, DTOType)
        val JPYUSD: Double = exchangeRates["JPYUSD"]!!
        assertEquals(0.0092, JPYUSD, 0.000001)
    }


    val currenciesJson = """{
  "world_currencies": [
    {"currency": "GBP", "city": {"name": "London", "lat": 51.509865, "lon": -0.118092}},
    {"currency": "EUR", "city": {"name": "Bruxelles", "lat": 50.850340, "lon": 4.351710}},
    {"currency": "USD", "city": {"name": "Washington D.C.", "lat": 38.89511, "lon": -77.03637}},
    {"currency": "RUB", "city": {"name": "Moscow", "lat": 55.751244, "lon": 37.618423}},
    {"currency": "CHF", "city": {"name": "Bern", "lat": 46.94809, "lon": 7.44744}},
    {"currency": "JPY", "city": {"name": "Tokyo", "lat": 35.652832, "lon": 139.839478}}
  ]
}"""
    val exchangeRatesJson = """{
  "GBPEUR": 1.1531,
  "GBPRUB": 86.3717,
  "GBPUSD": 1.3138,
  "JPYUSD": 0.0092,
  "RUBCHF": 0.0153,
  "RUBGBP": 0.0119,
  "RUBUSD": 0.0152,
  "USDGBP": 0.7894
}"""

}
