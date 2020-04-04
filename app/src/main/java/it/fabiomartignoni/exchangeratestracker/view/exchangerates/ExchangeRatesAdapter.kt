package it.fabiomartignoni.exchangeratestracker.view.exchangerates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import it.fabiomartignoni.exchangeratestracker.R
import it.fabiomartignoni.exchangeratestracker.viewmodel.exchangerates.ExchangeRateDisplayModel
import kotlinx.android.synthetic.main.exchange_rate_row.view.*

class ExchangeRatesAdapter(private val listener: ExchangeRatesAdapterListener): RecyclerView.Adapter<ExchangeRatesAdapter.ExchangeRateHolder>()  {

    interface ExchangeRatesAdapterListener {
        fun onRowSelected(index: Int)
    }

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
        return ExchangeRateHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: ExchangeRateHolder, position: Int) {
        holder.bind(exchangeRates[position])
        holder.itemView.setOnClickListener() {
            listener.onRowSelected(position)
        }
    }

    class ExchangeRateHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        private var exchangeRate: ExchangeRateDisplayModel? = null

        fun bind(exchangeRate: ExchangeRateDisplayModel) {
            this.exchangeRate = exchangeRate
            val pairsText = "${exchangeRate.currencyPair}   =   "
            view.currencyPairTextView.text = pairsText
            view.exchangeRateTextView.text = exchangeRate.exchangeRate
        }
    }
}