package it.fabiomartignoni.exchangeratestracker.model.repositories

data class ExchangeRate(val baseCurrency: String,
                        val counterCurrency: String,
                        var exchangeRate: Double?)