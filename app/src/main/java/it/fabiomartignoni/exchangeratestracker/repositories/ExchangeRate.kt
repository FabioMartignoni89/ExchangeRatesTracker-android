package it.fabiomartignoni.exchangeratestracker.repositories

data class ExchangeRate(val baseCurrency: String,
                        val counterCurrency: String,
                        val exchangeRate: Double)