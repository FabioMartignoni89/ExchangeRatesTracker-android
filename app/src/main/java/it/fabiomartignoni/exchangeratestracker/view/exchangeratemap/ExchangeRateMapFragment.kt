package it.fabiomartignoni.exchangeratestracker.view.exchangeratemap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import it.fabiomartignoni.exchangeratestracker.R
import it.fabiomartignoni.exchangeratestracker.viewmodel.exchangeratemap.ExchangeRateMapViewModel
import it.fabiomartignoni.exchangeratestracker.viewmodel.exchangeratemap.ExchangeRateMapViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_exchange_rate_map.*

class ExchangeRateMapFragment: Fragment() {

    companion object {
        val TAG = "ExchangeRateMapFragment"
    }

    var viewModel: ExchangeRateMapViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exchange_rate_map, container, false)

        viewModel = ViewModelProvider(this,
            ExchangeRateMapViewModelFactory(
                requireActivity().baseContext
            )
        ).get(ExchangeRateMapViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = requireActivity().toolbar
        toolbar.title = ""
        toolbar.setNavigationOnClickListener() {
            requireActivity().onBackPressed()
        }
        val actionBar = (requireActivity() as? AppCompatActivity)?.supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)
        }

        setupBindings()
    }

    private fun setupBindings() {

    }
}
