package it.fabiomartignoni.exchangeratestracker.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import it.fabiomartignoni.exchangeratestracker.R
import it.fabiomartignoni.exchangeratestracker.viewmodel.ExchangeRatesViewModel
import it.fabiomartignoni.exchangeratestracker.viewmodel.ExchangeRatesViewModelFactory
import kotlinx.android.synthetic.main.fragment_exchange_rates.*

class ExchangeRatesFragment : Fragment(), NewExchangeRateFragment.NewExchangeRateListener {

    companion object {
        val TAG = "ExchangeRatesFragment"
    }

    var viewModel: ExchangeRatesViewModel? = null

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var exchangeRatesAdapter: ExchangeRatesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exchange_rates, container, false)

        viewModel = ViewModelProvider(this, ExchangeRatesViewModelFactory(requireActivity().baseContext))
            .get(ExchangeRatesViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        linearLayoutManager = LinearLayoutManager(context)
        exchangeRatesRecyclerview.layoutManager = linearLayoutManager
        exchangeRatesAdapter = ExchangeRatesAdapter()
        exchangeRatesRecyclerview.adapter = exchangeRatesAdapter

        setupBindings()

        addExchangeRatesButton.setOnClickListener {
            openNewExchangeRateDialog()
        }
    }

    private fun setupBindings() {
        viewModel?.exchangeRates?.observe(viewLifecycleOwner, Observer { exchangeRates ->
            exchangeRatesAdapter.updateExchangeRates(exchangeRates)
        })
    }

    //region

    private fun openNewExchangeRateDialog() = if (viewModel != null) {
        val addExchangeRateFragment = NewExchangeRateFragment(
            viewModel?.availableCurrencies?.value ?: ArrayList(),
            this
        )
        addExchangeRateFragment.isCancelable = true
        addExchangeRateFragment.show(parentFragmentManager, NewExchangeRateFragment.TAG)
    }
    else {
        print("$TAG - viewmodel is required in order to present new exchange rate dialog")
    }

    override fun onCurrencyPairSelected(base: String, counter: String) {
        viewModel?.trackCurrencyPair(base, counter)
    }

    //endregion
}
