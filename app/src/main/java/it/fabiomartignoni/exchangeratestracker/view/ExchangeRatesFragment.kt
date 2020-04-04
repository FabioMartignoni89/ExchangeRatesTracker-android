package it.fabiomartignoni.exchangeratestracker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import it.fabiomartignoni.exchangeratestracker.R
import it.fabiomartignoni.exchangeratestracker.viewmodel.ExchangeRatesViewModel
import it.fabiomartignoni.exchangeratestracker.viewmodel.ExchangeRatesViewModelFactory
import kotlinx.android.synthetic.main.fragment_exchange_rates.*

class ExchangeRatesFragment : Fragment() {

    companion object {
        val TAG = "exchange_rates_fragment"
    }

    var viewModel: ExchangeRatesViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_exchange_rates, container, false)

        if (activity != null) {
            viewModel = ViewModelProvider(this, ExchangeRatesViewModelFactory(activity!!.baseContext))
                .get(ExchangeRatesViewModel::class.java)
            setupBindings()
        }

        return view
    }

    private fun setupBindings() {
        viewModel?.exchangeRates?.observe(viewLifecycleOwner, Observer { exchangeRate ->
           // exchangeRatesRecyclerview
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addExchangeRatesButton.setOnClickListener {
            openNewExchangeRateDialog()
        }
    }

    //region Utils

    private fun openNewExchangeRateDialog() {
        val addExchangeRateFragment = NewExchangeRateFragment()
        addExchangeRateFragment.isCancelable = true
        addExchangeRateFragment.show(parentFragmentManager, NewExchangeRateFragment.TAG)
    }

    //endregion
}
