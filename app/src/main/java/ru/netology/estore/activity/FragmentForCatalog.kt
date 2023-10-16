package ru.netology.estore.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import ru.netology.estore.R
import ru.netology.estore.adapter.ProductAdapter
import ru.netology.estore.databinding.FragmentForCatalogBinding
import ru.netology.estore.viewmodel.MainViewModel


class FragmentForCatalog : Fragment() {

    private val model:MainViewModel by activityViewModels()

    lateinit var binding: FragmentForCatalogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForCatalogBinding.inflate(inflater)
        binding.rwProducts.layoutManager = GridLayoutManager(activity, 2)

        model.data.observe(viewLifecycleOwner) {
//            val adapterProduct = ProductAdapter()
//            adapterProduct.addAllProduct(it)
            //adapter.submitList(it)
            binding.rwProducts.adapter = adapter
        }


        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = FragmentForCatalog()
    }

}