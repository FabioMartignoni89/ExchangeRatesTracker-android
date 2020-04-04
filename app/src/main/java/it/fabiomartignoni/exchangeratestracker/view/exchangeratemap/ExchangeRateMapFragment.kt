package it.fabiomartignoni.exchangeratestracker.view.exchangeratemap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
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

        val mapFragment = SupportMapFragment.newInstance()
        val fragmentTransaction =
            childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.mapView, mapFragment)
        fragmentTransaction.commitAllowingStateLoss()
        mapFragment.getMapAsync { map ->
            val sydney = LatLng(-33.852, 151.211)
            map.addMarker(
                MarkerOptions().position(sydney)
                    .title("Marker in Sydney")
            )
            map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }

        setupBindings()
    }

    private fun setupBindings() {

    }
}
