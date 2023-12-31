package ru.netology.estore.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ru.netology.estore.adapter.Listener
import ru.netology.estore.adapter.ProductAdapter
import ru.netology.estore.databinding.FragmentForCatalogBinding
import ru.netology.estore.dto.Data
import ru.netology.estore.dto.Product
import ru.netology.estore.viewmodel.MainViewModel


class FragmentForCatalog : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()

    lateinit var binding: FragmentForCatalogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val adapter = ProductAdapter(object : Listener {
            override fun like(product: Product) {
                viewModel.like(product)
            }

            override fun addToBasket(product: Product) {
                viewModel.addToBasket(product)
            }

            override fun addToBasketAgain(product: Product) {
            }

            override fun deleteFromBasket(product: Product) {
            }

            override fun weightPlus(product: Product) {
            }

            override fun weightMinus(product: Product) {
            }

            override fun deleteFromBasketWeightZero() {
            }
        })

        binding = FragmentForCatalogBinding.inflate(inflater)
        binding.rwProducts.layoutManager = GridLayoutManager(activity, 2)
        binding.rwProducts.adapter = adapter

        viewModel.dataFull.observe(viewLifecycleOwner) { full ->
            val list = when (full.status) {
                Data.allGroup -> viewModel.dataFull.value?.products
                Data.fruitGroup -> viewModel.dataFull.value?.products?.filter { it.group == Data.fruitGroup }
                    .orEmpty()

                Data.vegetableGroup -> viewModel.dataFull.value?.products?.filter { it.group == Data.vegetableGroup }
                    .orEmpty()

                Data.bakeryGroup -> viewModel.dataFull.value?.products?.filter { it.group == Data.bakeryGroup }
                    .orEmpty()

                Data.hitGroup -> viewModel.dataFull.value?.products?.filter { it.isHit }.orEmpty()
                Data.discountGroup -> viewModel.dataFull.value?.products?.filter { it.isDiscount }
                    .orEmpty()

                Data.favoriteGroup -> viewModel.dataFull.value?.products?.filter { it.isFavorite }
                    .orEmpty()

                else -> emptyList()
            }
            adapter.productList = list.orEmpty()
            adapter.submitList(list)

        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentForCatalog()
    }

}