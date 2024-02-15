package ru.netology.estore.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.estore.adapter.DataHistoryAdapter
import ru.netology.estore.adapter.HistoryListener
import ru.netology.estore.databinding.FragmentHistoryOfOrdersBinding
import ru.netology.estore.dto.DataHistory
import ru.netology.estore.viewmodel.MainViewModel

class FragmentHistory : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHistoryOfOrdersBinding.inflate(inflater, container, false)

        val adapter = DataHistoryAdapter(object : HistoryListener {
            override fun deleteHistoryById(dataHistory: DataHistory) {
                viewModel.deleteHistoryById(dataHistory.id)
            }
        })

        binding.rwOrderOfHistory.layoutManager = LinearLayoutManager(activity)
        binding.rwOrderOfHistory.adapter = adapter

        viewModel.dataHistoryOrders.observe(viewLifecycleOwner) {
            binding.txEmptyHistory.isVisible = viewModel.emptyHistoryOfOrders
            adapter.submitList(it)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentHistory()
    }
}