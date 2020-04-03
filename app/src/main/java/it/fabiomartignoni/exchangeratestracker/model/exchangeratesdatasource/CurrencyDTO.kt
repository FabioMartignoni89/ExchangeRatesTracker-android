import com.google.gson.annotations.SerializedName

data class CurrenciesDTO (
	@SerializedName("world_currencies") val worldCurrencies : List<WorldCurrencies>
)

data class WorldCurrencies (
	@SerializedName("currency") val currency : String,
	@SerializedName("city") val city : City
)

data class City (
	@SerializedName("name") val name : String,
	@SerializedName("lat") val lat : Double,
	@SerializedName("lon") val lon : Double
)

data class ExchangeRatesDTO (
	val exchangeRates : HashMap<String, Double>
)