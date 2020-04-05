package it.fabiomartignoni.exchangeratestracker.model.entities

data class ExchangeRate(val baseCurrency: String,
                        val counterCurrency: String,
                        var exchangeRate: Double?)