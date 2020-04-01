package it.fabiomartignoni.exchangeratestracker.exchangeratesrepository

data class ExchangeRate(val baseCurrency: String,
                        val counterCurrency: String,
                        val exchangeRate: Double)