package ru.netology.estore.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import ru.netology.estore.adapter.Listener
import ru.netology.estore.adapter.ProductAdapter
import ru.netology.estore.databinding.FragmentForCatalogBinding
import ru.netology.estore.dto.Data
import ru.netology.estore.dto.Product
import ru.netology.estore.viewmodel.MainViewModel


class FragmentForCatalog : Fragment() {

    private val model: MainViewModel by activityViewModels()

    lateinit var binding: FragmentForCatalogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val adapter = ProductAdapter(object : Listener {
            override fun like(product: Product) {
                model.like(product)
            }

            override fun addToBasket(product: Product) {
                model.addToBasket(product)
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

        model.dataFull.observe(viewLifecycleOwner) { full ->
            val list = when (full.status) {
                Data.allGroup -> model.dataFull.value?.products
                Data.fruitGroup -> model.dataFull.value?.products?.filter { it.group == Data.fruitGroup }
                    .orEmpty()

                Data.vegetableGroup -> model.dataFull.value?.products?.filter { it.group == Data.vegetableGroup }
                    .orEmpty()

                Data.bakeryGroup -> model.dataFull.value?.products?.filter { it.group == Data.bakeryGroup }
                    .orEmpty()

                Data.hitGroup -> model.dataFull.value?.products?.filter { it.isHit }.orEmpty()
                Data.discountGroup -> model.dataFull.value?.products?.filter { it.isDiscount }
                    .orEmpty()

                Data.favoriteGroup -> model.dataFull.value?.products?.filter { it.isFavorite }
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