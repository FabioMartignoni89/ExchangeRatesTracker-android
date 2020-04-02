package it.fabiomartignoni.exchangeratestracker.exchangeratesdatasource

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface ExchangeRatesRemoteDataSource {
    @GET
    fun getExchangeRates(@Url url: String?): Call<HashMap<String, Double>>
}