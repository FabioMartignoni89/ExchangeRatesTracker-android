package it.fabiomartignoni.exchangeratestracker.model.exchangeratesdatasource

import CurrenciesDTO
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BaseExchangeRatesDataSource(private val jsonLoader: JsonLoaderStrategy): ExchangeRatesDataSource {

    private val currenciesJsonFileName = "currencies.json"

    override fun getCurrencies(): List<String> {
        val currenciesJson = jsonLoader.loadJson(currenciesJsonFileName)
        val currenciesDTO = Gson().fromJson(currenciesJson, CurrenciesDTO::class.java)
        return currenciesDTO.worldCurrencies.map { it.currency }
    }

    override fun fetchExchangeRates(currencyPairs: List<String>, onResult: (List<Double>?) -> Unit) {
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
                    val exchangeRates = ArrayList<Double>()
                    for (pair in currencyPairs) {
                        val exchangeRate = response.body()!![pair]
                        exchangeRates.add(exchangeRate!!)
                    }
                    onResult(exchangeRates)
                }
                else {
                    print("server error for ${call.request().url()}. Response: $response")
                }
            }
            override fun onFailure(call: Call<HashMap<String, Double>>, t: Throwable) {
                print("error for ${call.request().url()}: t")
                onResult(null)
            }
        })
    }
}

