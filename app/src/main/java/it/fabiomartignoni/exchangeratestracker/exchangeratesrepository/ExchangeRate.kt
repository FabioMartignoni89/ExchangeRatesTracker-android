package it.fabiomartignoni.exchangeratestracker.exchangeratesrepository

data class ExchangeRate(val baseCurrency: String,
                        val counterCurrency: String,
                        var exchangeRate: Double?)