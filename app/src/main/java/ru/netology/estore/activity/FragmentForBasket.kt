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
import ru.netology.estore.activity.FragmentCurrentProduct.Companion.textArgument
import ru.netology.estore.adapter.Listener
import ru.netology.estore.adapter.ProductInBasketAdapter
import ru.netology.estore.databinding.FragmentForBasketBinding
import ru.netology.estore.dto.Data
import ru.netology.estore.dto.Product
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.OrderViewModel
import ru.netology.estore.viewmodel.TopTextViewModel


class FragmentForBasket : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private val authViewModel:AuthViewModel by activityViewModels()
    private val topTextViewModel:TopTextViewModel by activityViewModels()
    private val orderViewModel:OrderViewModel by activityViewModels()
    lateinit var binding: FragmentForBasketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentForBasketBinding.inflate(inflater)

        val adapter = ProductInBasketAdapter(object : Listener {

            override fun like(product: Product) {
                if(authViewModel.authenticated) {
                    viewModel.like(product)
                } else {
                    mustSignIn()
                }
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

            override fun goToProduct(product: Product) {
                findNavController()
                    .navigate(R.id.fragmentCurrentProduct,
                        Bundle().apply {
                            textArgument = product.id.toString()
                        })
                topTextViewModel.text.value = product.name
                viewModel.pointBottomMenu.value = -1
            }
        })

        binding.rwProducts.layoutManager = LinearLayoutManager(activity)
        binding.rwProducts.adapter = adapter

        viewModel.dataFull.observe(viewLifecycleOwner) { state ->
            binding.txEmptyBasket.isVisible = state.emptyBasket
            binding.amountOrder.isVisible = !state.emptyBasket
            binding.buttonOrder.isEnabled = !state.emptyBasket
            val list = state.products.filter { it.inBasket }
            adapter.submitList(list)
            viewModel.amountOrder.value = viewModel.countOrder(list)
        }

        viewModel.amountOrder.observe(viewLifecycleOwner) {
            binding.amountOrder.text = "$it руб"
        }

        binding.buttonOrder.setOnClickListener {
            viewModel.deleteFromBasketWeightZero()
            if(viewModel.dataFull.value?.emptyBasket == true) {
                return@setOnClickListener
            }
            if(authViewModel.authenticated) {
                topTextViewModel.text.value = Data.orderGroup
                if(orderViewModel.showPoint2.value!=0) {
                    orderViewModel.showPoint1.value = 2
                } else {
                    orderViewModel.showPoint1.value = 1
                }
                viewModel.pointBottomMenu.value = 2
                findNavController().navigate(R.id.orderFragment)
            } else {
                mustSignIn()
            }
        }

        return binding.root
    }

    private fun mustSignIn() {
        val menuDialog = SignInOutDialogFragment(
            title = "Нужна регистрация",
            text = "Для этого действия необходимо войти в систему",
            icon = R.drawable.info_24,
            textPosButton = "Sign In",
            textNegButton = "Позже",
            flagSignIn = true,
            flagOrder = false,
            navigateTo = R.id.fragmentForBasket
            )
        val manager = childFragmentManager
        menuDialog.show(manager, "Sign in")
    }

    companion object {
        fun newInstance() = FragmentForBasket()
    }
}
