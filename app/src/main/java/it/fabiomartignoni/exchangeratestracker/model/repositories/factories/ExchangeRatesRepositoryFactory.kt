package it.fabiomartignoni.exchangeratestracker.model.repositories.factories

import it.fabiomartignoni.exchangeratestracker.model.repositories.ExchangeRatesRepository

interface ExchangeRatesRepositoryFactory {
    fun makeRepository(): ExchangeRatesRepository
}