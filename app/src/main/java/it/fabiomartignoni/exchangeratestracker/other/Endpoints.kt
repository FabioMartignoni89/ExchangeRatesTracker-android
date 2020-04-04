package it.fabiomartignoni.exchangeratestracker.other

import java.lang.StringBuilder

class Endpoints {
    companion object {
        val fetchExchangeRates = Endpoint("https", "europe-west1-revolut-230009.cloudfunctions.net", "/revolut-ios?")
    }
}

class Endpoint(val scheme: String,
                    val host: String,
                    val path: String) {

    fun baseURL(): String {
        return "$scheme:$host"
    }

    fun URL(): String {
        return "${baseURL()}$path"
    }
}