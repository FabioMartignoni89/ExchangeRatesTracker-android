package it.fabiomartignoni.exchangeratestracker.view.exchangeratemap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import it.fabiomartignoni.exchangeratestracker.R
import it.fabiomartignoni.exchangeratestracker.viewmodel.exchangeratemap.CityMarkerDisplayModel
import it.fabiomartignoni.exchangeratestracker.viewmodel.exchangeratemap.ExchangeRateMapViewModel
import it.fabiomartignoni.exchangeratestracker.viewmodel.exchangeratemap.ExchangeRateMapViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


class ExchangeRateMapFragment: Fragment() {

    companion object {
        val TAG = "ExchangeRateMapFragment"
    }

    val args: ExchangeRateMapFragmentArgs by navArgs()
    var viewModel: ExchangeRateMapViewModel? = null
    var marker: Marker? = null
    var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exchange_rate_map, container, false)

        viewModel = ViewModelProvider(this,
            ExchangeRateMapViewModelFactory(
                requireActivity().baseContext,
                args.baseCurrency,
                args.counterCurrency
            )
        ).get(ExchangeRateMapViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareActionBar()
        initGoogleMaps()
        setupBindings()
    }

    private fun prepareActionBar() {
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
    }

    private fun initGoogleMaps() {
        val mapFragment = SupportMapFragment.newInstance()
        val fragmentTransaction =
            childFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.mapView, mapFragment)
        fragmentTransaction.commitAllowingStateLoss()
        mapFragment.getMapAsync { map ->
            this.map = map
        }
    }

    private fun setupBindings() {
        viewModel?.cityMarkerDisplayModel?.observe(viewLifecycleOwner, Observer { markerModel ->
            val isMapReady = map != null
            val markerAlreadyDrawn = marker != null
            val hasMarkerData = markerModel != null

            if (isMapReady && hasMarkerData) {
                if (!markerAlreadyDrawn) {
                    drawMarker(map!!, markerModel!!)
                }
                else {
                    updateMarker(marker!!, markerModel!!)
                }
            }
        })
    }

    private fun drawMarker(map: GoogleMap, model: CityMarkerDisplayModel) {
        val cityGeoLocation = LatLng(model.latitude, model.longitude)
        marker = map.addMarker(
            MarkerOptions()
                .position(cityGeoLocation)
                .title(model.cityName)
                .snippet(model.exchangeRate)
        )
        map.moveCamera(CameraUpdateFactory.newLatLng(cityGeoLocation))
    }

    private fun updateMarker(marker: Marker, model: CityMarkerDisplayModel) {
        marker.title = model.cityName
        marker.snippet = model.exchangeRate
        if (marker.isInfoWindowShown) {
            marker.showInfoWindow()
        }
    }
}
