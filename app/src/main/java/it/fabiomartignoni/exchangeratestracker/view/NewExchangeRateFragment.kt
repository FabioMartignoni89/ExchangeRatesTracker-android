package it.fabiomartignoni.exchangeratestracker.view

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import it.fabiomartignoni.exchangeratestracker.R
import kotlinx.android.synthetic.main.fragment_new_exchange_rate.view.*


class NewExchangeRateFragment(): DialogFragment() {

    companion object {
        val TAG = "NewExchangeRateFragment"
    }

    interface NewExchangeRateListener {
        fun onCurrencyPairSelected(base: String, counter: String)
    }

    private lateinit var currencies: List<String>
    private lateinit var listener: NewExchangeRateListener

    private var baseSpinner: Spinner? = null
    private var counterSpinner: Spinner? = null

    constructor(currencies: List<String>,
                listener: NewExchangeRateListener) : this() {
        this.currencies = currencies
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = requireActivity().layoutInflater.inflate(R.layout.fragment_new_exchange_rate, null)

        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(view)
        builder.setTitle(R.string.select_currencies)
        builder.setPositiveButton(R.string.confirm_button) { dialog, which ->
            trackSelectedPairs()
            dismiss()
        }

        val alert = builder.create()
        alert.show()

        baseSpinner = view.baseCurrencySpinner
        counterSpinner = view.counterCurrencySpinner
        baseSpinner?.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, currencies)
        counterSpinner?.adapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, currencies)

        return alert
    }

    private fun trackSelectedPairs() {
        val baseIndex = baseSpinner?.selectedItemPosition
        val counterIndex = counterSpinner?.selectedItemPosition

        if (baseIndex != null && counterIndex != null &&
            baseIndex < currencies.size && counterIndex < currencies.size) {
            val base = currencies[baseIndex]
            val counter = currencies[counterIndex]
            listener.onCurrencyPairSelected(base, counter)
        }
        else {
            println("$TAG - Invalid selected currency pairs indexes")
        }
    }
}
