package it.fabiomartignoni.exchangeratestracker.model.exchangeratesdatasource

import CurrenciesDTO
import com.google.gson.Gson
import it.fabiomartignoni.exchangeratestracker.other.Endpoint
import it.fabiomartignoni.exchangeratestracker.other.Endpoints
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder

class BaseExchangeRatesDataSource(private val jsonLoader: JsonLoaderStrategy,
                                  private val fetchExchangeRatesEndpoint: Endpoint): ExchangeRatesDataSource {

    companion object {
        val TAG = "BaseExchangeRatesDataSource"
    }

    private val currenciesJsonFileName = "currencies.json"

    override fun getCurrencies(): List<String> {
        val currenciesJson = jsonLoader.loadJson(currenciesJsonFileName)
        val currenciesDTO = Gson().fromJson(currenciesJson, CurrenciesDTO::class.java)
        return currenciesDTO.worldCurrencies.map { it.currency }
    }

    override fun fetchExchangeRates(currencyPairs: List<String>, onResult: (List<Double>?) -> Unit) {
        // https://europe-west1-revolut-230009.cloudfunctions.net/revolut-ios?pairs=USDJPY&pairs=JPYUSD
        val retrofit = Retrofit.Builder()
            .baseUrl(fetchExchangeRatesEndpoint.baseURL())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ExchangeRatesRemoteDataSource::class.java)

        val call = service.getExchangeRates(prepareURL(currencyPairs))
        call.enqueue(object : Callback<HashMap<String, Double>> {
            override fun onResponse(call: Call<HashMap<String, Double>>, response: Response<HashMap<String, Double>>) {
                if (response.code() == 200 && response.body() != null) {
                    val exchangeRates = ArrayList<Double>()
                    for (pair in currencyPairs) {
                        val exchangeRate = response.body()!![pair]
                        exchangeRates.add(exchangeRate!!)
                    }
                    onResult(exchangeRates)
                }
                else {
                    println("$TAG - server error for ${call.request().url()}. Response: $response")
                }
            }
            override fun onFailure(call: Call<HashMap<String, Double>>, t: Throwable) {
                println("$TAG error for ${call.request().url()}: t")
                onResult(null)
            }
        })
    }

    private fun prepareURL(currencyPairs: List<String>): String {
        val builder = StringBuilder()
        builder.append(fetchExchangeRatesEndpoint.path)

        var isFirstParameter = true
        for (pair in currencyPairs) {
            if (!isFirstParameter)
                builder.append("&")

            builder.append("pairs=")
            builder.append(pair)

            isFirstParameter = false
        }

        return  builder.toString()
    }
}

