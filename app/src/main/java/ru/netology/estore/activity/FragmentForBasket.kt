package ru.netology.estore.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.netology.estore.R
import ru.netology.estore.adapter.Listener
import ru.netology.estore.adapter.ProductInBasketAdapter
import ru.netology.estore.databinding.FragmentForBasketBinding
import ru.netology.estore.dto.Data
import ru.netology.estore.dto.Product
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.TopTextViewModel


class FragmentForBasket : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private val authViewModel:AuthViewModel by activityViewModels()
    private val topTextViewModel:TopTextViewModel by activityViewModels()
    lateinit var binding: FragmentForBasketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentForBasketBinding.inflate(inflater)

        val adapter = ProductInBasketAdapter(object : Listener {

            override fun like(product: Product) {
                viewModel.like(product)
            }

            override fun addToBasket(product: Product) {
                viewModel.addToBasket(product)
            }

            override fun addToBasketAgain(product: Product) {
                viewModel.addToBasketAgain(product)
            }

            override fun deleteFromBasket(product: Product) {
                viewModel.deleteFromBasket(product)
            }

            override fun weightPlus(product: Product) {
                viewModel.weightPLus(product)
            }

            override fun weightMinus(product: Product) {
                viewModel.weightMinus(product)
            }

            override fun deleteFromBasketWeightZero() {
                viewModel.deleteFromBasketWeightZero()
            }
        })


        binding.rwProducts.layoutManager = LinearLayoutManager(activity)

//        var list = getListBasket()
        viewModel.dataFull.value = viewModel.dataFull.value?.copy(isEmptyBasket = getListBasket())

        binding.rwProducts.adapter = adapter

        viewModel.dataFull.observe(viewLifecycleOwner) { state ->
            binding.txEmptyBasket.isVisible = state.isEmptyBasket
            binding.amountOrder.isVisible = !state.isEmptyBasket
            binding.buttonOrder.isEnabled = !state.isEmptyBasket
            val list = state.products.filter { it.inBasket }
            adapter.submitList(list)
            viewModel.amountOrder.value = viewModel.countOrder(list)
            // binding.amountOrder.text = "${viewModel.countOrder(list)} руб"

//            list = getListBasket()
//            if(list.isEmpty() && state.isEmptyBasket) {
//                binding.txEmptyBasket.isVisible = state.isEmptyBasket
//                adapter.productList = list
//                adapter.submitList(list)
//            } else if (list.isEmpty() && !state.isEmptyBasket) {
//                model.dataFull.value = model.dataFull.value?.copy(isEmptyBasket = list.isEmpty())
//            } else if(list.isNotEmpty()) {
//                binding.txEmptyBasket.isVisible = state.isEmptyBasket
//                adapter.productList = list
//                adapter.submitList(list)
//            }
        }

        viewModel.amountOrder.observe(viewLifecycleOwner) {
            binding.amountOrder.text = "$it руб"
        }

        binding.buttonOrder.setOnClickListener {
            if(authViewModel.authenticated) {
                topTextViewModel.text.value = Data.orderGroup
                viewModel.deleteFromBasketWeightZero()
                findNavController().navigate(R.id.orderFragment)
            } else {
                mustSignIn()
            }
        }

        return binding.root
    }

    fun getListBasket(): Boolean {
        val list = viewModel.dataFull.value?.products?.filter { it.inBasket }.orEmpty()
        return list.isEmpty()
    }

    fun mustSignIn() {
        val menuDialog = SignInOutDialogFragment("Нужна регистрация","Для этого действия необходимо войти в систему", R.drawable.info_24, "Sign In", "Позже")
        val manager = childFragmentManager
        menuDialog.show(manager, "Sign in")
    }

    companion object {
        fun newInstance() = FragmentForBasket()
    }
}
