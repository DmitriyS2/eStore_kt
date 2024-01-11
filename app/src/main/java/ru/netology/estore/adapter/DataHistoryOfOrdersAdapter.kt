package ru.netology.estore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.estore.R
import ru.netology.estore.databinding.ItemDataHistoryOfOrdersBinding
import ru.netology.estore.dto.DataOrderForHistory

class DataHistoryOfOrdersAdapter():
    ListAdapter<DataOrderForHistory, DataHistoryOfOrdersAdapter.DataHistoryOfOrdersHolder>(HistoryDiffCallback()) {

    var productList = emptyList<DataOrderForHistory>()

    class DataHistoryOfOrdersHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ItemDataHistoryOfOrdersBinding.bind(item)

        fun bind(dataOrderForHistory: DataOrderForHistory) {

            binding.textHistory1.text = "${dataOrderForHistory.id} Заказ на сумму ${dataOrderForHistory.sumOrder} руб"
            binding.textHistory2.text = if(dataOrderForHistory.pickUp) "Самовывоз" else "Доставка"
            binding.textHistory3.text = dataOrderForHistory.dateTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHistoryOfOrdersHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_history_of_orders, parent, false)
        return DataHistoryOfOrdersHolder(view)
    }

    override fun onBindViewHolder(holder: DataHistoryOfOrdersHolder, position: Int) {
        val dataOrderForHistory = getItem(position)
        holder.bind(dataOrderForHistory)
    }
}

class HistoryDiffCallback : DiffUtil.ItemCallback<DataOrderForHistory>() {
    override fun areItemsTheSame(oldItem: DataOrderForHistory, newItem: DataOrderForHistory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataOrderForHistory, newItem: DataOrderForHistory): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: DataOrderForHistory, newItem: DataOrderForHistory): Any =
        Payload(
//            liked = newItem.isFavorite.takeIf { oldItem.isFavorite != it },
//            pushButtonAdd = newItem.inBasket.takeIf { oldItem.inBasket != it },
        )
}