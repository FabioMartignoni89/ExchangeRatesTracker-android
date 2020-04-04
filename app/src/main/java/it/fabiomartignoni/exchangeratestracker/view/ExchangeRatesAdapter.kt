package it.fabiomartignoni.exchangeratestracker.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.fabiomartignoni.exchangeratestracker.R
import it.fabiomartignoni.exchangeratestracker.viewmodel.ExchangeRateDisplayModel
import kotlinx.android.synthetic.main.exchange_rate_row.view.*

class ExchangeRatesAdapter: RecyclerView.Adapter<ExchangeRatesAdapter.ExchangeRateHolder>()  {

    private var exchangeRates: List<ExchangeRateDisplayModel> = ArrayList()

    fun updateExchangeRates(exchangeRates: List<ExchangeRateDisplayModel>) {
        this.exchangeRates = exchangeRates
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return exchangeRates.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeRateHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.exchange_rate_row, parent, false)
        return ExchangeRateHolder(view)
    }

    override fun onBindViewHolder(holder: ExchangeRateHolder, position: Int) {
        holder.bind(exchangeRates[position])
    }

    class ExchangeRateHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {

        private var view: View = v
        private var exchangeRate: ExchangeRateDisplayModel? = null

        init {
            v.setOnClickListener(this)
        }

        override fun onClick(v: View) {

        }

        fun bind(exchangeRate: ExchangeRateDisplayModel) {
            this.exchangeRate = exchangeRate
            val pairsText = "${exchangeRate.currencyPair}   =   "
            view.currencyPairTextView.text = pairsText
            view.exchangeRateTextView.text = exchangeRate.exchangeRate
        }

    }


}

