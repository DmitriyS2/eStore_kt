package ru.netology.estore.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.estore.R
import ru.netology.estore.databinding.ItemDataHistoryOfOrdersBinding
import ru.netology.estore.dto.DataHistory

class DataHistoryAdapter:
    ListAdapter<DataHistory, DataHistoryAdapter.DataHistoryHolder>(HistoryDiffCallback()) {

    var historyList = emptyList<DataHistory>()
    class DataHistoryHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ItemDataHistoryOfOrdersBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(dataHistory: DataHistory) {
            binding.textHistory1.text = "Заказ на сумму ${dataHistory.sumOrder} руб"
            binding.textHistory2.text = if(dataHistory.pickUp) "Самовывоз" else "Доставка"
            binding.textHistory3.text = dataHistory.dateTime
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHistoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_history_of_orders, parent, false)
        return DataHistoryHolder(view)
    }

    override fun onBindViewHolder(holder: DataHistoryHolder, position: Int) {
        val dataOrderForHistory = getItem(position)
        holder.bind(dataOrderForHistory)
    }
}

class HistoryDiffCallback : DiffUtil.ItemCallback<DataHistory>() {
    override fun areItemsTheSame(oldItem: DataHistory, newItem: DataHistory): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataHistory, newItem: DataHistory): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: DataHistory, newItem: DataHistory): Any =
        Payload()
}