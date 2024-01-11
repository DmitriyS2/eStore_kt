package ru.netology.estore.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.estore.adapter.DataHistoryOfOrdersAdapter
import ru.netology.estore.databinding.FragmentHistoryOfOrdersBinding
import ru.netology.estore.viewmodel.MainViewModel

class FragmentHistoryOfOrders : Fragment() {
    lateinit var binding: FragmentHistoryOfOrdersBinding

    private val viewModel:MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryOfOrdersBinding.inflate(inflater, container, false)

        val adapter = DataHistoryOfOrdersAdapter()

        binding.rwOrderOfHistory.layoutManager = LinearLayoutManager(activity)

        binding.rwOrderOfHistory.adapter = adapter

        viewModel.dataHistoryOrders.observe(viewLifecycleOwner) {
           adapter.submitList(it)
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = FragmentHistoryOfOrders()
    }
}