package ru.netology.estore.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.estore.R
import ru.netology.estore.databinding.ItemDataHistoryOfOrdersBinding
import ru.netology.estore.dto.DataHistory

interface HistoryListener {
    fun deleteHistoryById(dataHistory: DataHistory)
}

class DataHistoryAdapter(private val listener: HistoryListener) :
    ListAdapter<DataHistory, DataHistoryAdapter.DataHistoryHolder>(HistoryDiffCallback()) {

    class DataHistoryHolder(item: View, private val listener: HistoryListener) :
        RecyclerView.ViewHolder(item) {
        val binding = ItemDataHistoryOfOrdersBinding.bind(item)

        fun bind(dataHistory: DataHistory) {
            binding.textHistory1.text = itemView.context.getString(
                R.string.dara_history_order_amount,
                dataHistory.sumOrder.toString()
            )
            binding.textHistory2.isVisible = dataHistory.pickUp
            binding.textHistory2b.isVisible = !dataHistory.pickUp
            binding.textHistory3.text = dataHistory.dateTime

            binding.buttonDeleteOrderOfHistory.setOnClickListener {
                listener.deleteHistoryById(dataHistory)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHistoryHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_data_history_of_orders, parent, false)
        return DataHistoryHolder(view, listener)
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