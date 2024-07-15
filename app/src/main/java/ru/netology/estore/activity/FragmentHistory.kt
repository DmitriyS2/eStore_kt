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

    private var _binding: FragmentHistoryOfOrdersBinding? = null
    val binding: FragmentHistoryOfOrdersBinding
        get() = _binding!!

    private val adapter: DataHistoryAdapter by lazy { createAdapter() }

    private fun createAdapter(): DataHistoryAdapter = DataHistoryAdapter(object : HistoryListener {
        override fun deleteHistoryById(dataHistory: DataHistory) {
            viewModel.deleteHistoryById(dataHistory.id)
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryOfOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObserver()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun setAdapter() {
        binding.rwOrderOfHistory.layoutManager = LinearLayoutManager(activity)
        binding.rwOrderOfHistory.adapter = adapter
    }

    private fun setObserver() {
        viewModel.dataHistoryOrders.observe(viewLifecycleOwner) {
            binding.txEmptyHistory.isVisible = viewModel.emptyHistoryOfOrders
            adapter.submitList(it)
        }
    }
}