package ru.netology.estore.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.estore.R
import ru.netology.estore.adapter.Listener
import ru.netology.estore.adapter.ProductAdapter
import ru.netology.estore.adapter.ProductInBasketAdapter
import ru.netology.estore.databinding.FragmentForBasketBinding
import ru.netology.estore.dto.Product
import ru.netology.estore.viewmodel.MainViewModel


class FragmentForBasket : Fragment() {

    private val model: MainViewModel by activityViewModels()

    lateinit var binding: FragmentForBasketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val adapter = ProductInBasketAdapter(object : Listener {
            override fun like(product: Product) {
                model.like(product)
            }

            override fun addToBasket(product: Product) {
                model.addToBasket(product)
            }

            override fun addToBasketAgain(product: Product) {
                 model.addToBasketAgain(product)
            }

            override fun deleteFromBasket(product: Product) {
                model.deleteFromBasket(product)
            }

            override fun weightPlus(product: Product) {
                model.weightPLus(product)
            }

            override fun weightMinus(product: Product) {
                model.weightMinus(product)
            }

            override fun deleteFromBasketWeightZero() {
                model.deleteFromBasketWeightZero()
            }
        })

        binding = FragmentForBasketBinding.inflate(inflater)
        binding.rwProducts.layoutManager = LinearLayoutManager(activity)

        var list = getListBasket()
        model.dataFull.value = model.dataFull.value?.copy(isEmptyBasket = list.isEmpty())
        //adapter.productList = list
        //binding.rwProducts.layoutManager = GridLayoutManager(activity, 1)
        binding.rwProducts.adapter = adapter

        model.dataFull.observe(viewLifecycleOwner) {state ->
            list = getListBasket()
            if(list.isEmpty() && state.isEmptyBasket) {
                binding.txEmptyBasket.isVisible = state.isEmptyBasket
                adapter.productList = list
                adapter.submitList(list)
            } else if (list.isEmpty() && !state.isEmptyBasket) {
                model.dataFull.value = model.dataFull.value?.copy(isEmptyBasket = list.isEmpty())
            } else if(list.isNotEmpty()) {
                adapter.productList = list
                adapter.submitList(list)
            }
        }
        return binding.root
    }

    fun getListBasket():List<Product>
    {
        val list = model.dataFull.value?.products
            ?.filter { it.inBasket }
            .orEmpty()

        //if (list.isEmpty()) model.dataFull.value = model.dataFull.value?.copy(isEmptyBasket = list.isEmpty())

        return list
    }

    companion object {
        fun newInstance(param1: String, param2: String) = FragmentForBasket()
            }
    }
