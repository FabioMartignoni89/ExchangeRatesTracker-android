package it.fabiomartignoni.exchangeratestracker.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import it.fabiomartignoni.exchangeratestracker.R
import it.fabiomartignoni.exchangeratestracker.viewmodel.ExchangeRatesViewModel
import it.fabiomartignoni.exchangeratestracker.viewmodel.ExchangeRatesViewModelFactory

class NewExchangeRateFragment : DialogFragment() {

    companion object {
        val TAG = "new_exchange_rate_fragment"
    }

    var viewModel: ExchangeRatesViewModel? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        viewModel = ViewModelProvider(this, ExchangeRatesViewModelFactory(activity!!.baseContext))
            .get(ExchangeRatesViewModel::class.java)

        if (activity == null) {
            print("Error presenting add new exchange rate dialog.")
            return super.onCreateDialog(savedInstanceState)
        }

        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.fragment_new_exchange_rate, null)

        val alertBuilder = androidx.appcompat.app.AlertDialog.Builder(activity!!)
        alertBuilder.setView(view)
        alertBuilder.setTitle(R.string.select_currencies)
        alertBuilder.setPositiveButton(R.string.confirm_button) { dialog, which ->
            dismiss()
        }

        val alert = alertBuilder.create()
        alert.show()

        return alert
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBindings()
    }

    private fun setupBindings() {
        viewModel?.availableCurrencies?.observe(viewLifecycleOwner, Observer { currency ->
            // exchangeRatesRecyclerview
        })
    }
}
