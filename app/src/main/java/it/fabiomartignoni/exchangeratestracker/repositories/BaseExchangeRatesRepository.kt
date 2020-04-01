package it.fabiomartignoni.exchangeratestracker.repositories

class BaseExchangeRatesRepository: ExchangeRatesRepository {
    override fun getExchangeRates() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrencies(): List<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun track(base: String, counter: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun untrack(base: String, counter: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}