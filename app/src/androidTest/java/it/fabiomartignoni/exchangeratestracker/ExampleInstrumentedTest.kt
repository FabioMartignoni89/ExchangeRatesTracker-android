package it.fabiomartignoni.exchangeratestracker

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import it.fabiomartignoni.exchangeratestracker.exchangeratesdatasource.ExchangeRatesRemoteDataSource
import it.fabiomartignoni.exchangeratestracker.other.loadJson

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("it.fabiomartignoni.exchangeratestracker", appContext.packageName)
    }

    @Test
    fun canLoadCurrenciesFromAssets() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val currenciesJson = appContext.assets.loadJson("currencies.json")
        assertTrue(currenciesJson != null && currenciesJson != "")
    }

    @Test
    fun canRequestExchangeRates() {
        var resp = false
        // https://europe-west1-revolut-230009.cloudfunctions.net/revolut-ios?pairs=USDJPY&pairs=JPYUSD
        val baseUrl = "https://europe-west1-revolut-230009.cloudfunctions.net"
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ExchangeRatesRemoteDataSource::class.java)
        val call = service.getExchangeRates("/revolut-ios?pairs=USDJPY&pairs=JPYUSD")
        call.enqueue(object : Callback<HashMap<String, Double>> {
            override fun onResponse(call: Call<HashMap<String, Double>>, response: Response<HashMap<String, Double>>) {
                if (response.code() == 200 && response.body() != null) {
                    assertNotNull(response.body()!!["JPYUSD"])
                    assertNotNull(response.body()!!["USDJPY"])
                    resp = true
                }
                else {
                    fail()
                }
            }
            override fun onFailure(call: Call<HashMap<String, Double>>, t: Throwable) {
                fail()
            }
        })

        Thread.sleep(1_000)
        assertTrue(resp)
    }
}
