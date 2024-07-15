package ru.netology.estore.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.netology.estore.R
import ru.netology.estore.adapter.Listener
import ru.netology.estore.adapter.ProductInBasketAdapter
import ru.netology.estore.databinding.FragmentForBasketBinding
import ru.netology.estore.dto.Product
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.OrderViewModel
import ru.netology.estore.viewmodel.TopTextViewModel

class FragmentForBasket : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val topTextViewModel: TopTextViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()

    private var _binding: FragmentForBasketBinding? = null
    val binding: FragmentForBasketBinding
        get() = _binding!!

    private val adapter: ProductInBasketAdapter by lazy { createAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForBasketBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        setObserver()
        setListeners()
        setSwipe()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun mustSignIn() {
        val menuDialog = SignInOutDialogFragment(
            title = getString(R.string.need_registration),
            text = getString(R.string.need_yo_signin),
            icon = R.drawable.info_24,
            textPosButton = getString(R.string.sign_in),
            textNegButton = getString(R.string.later),
            flagSignIn = true,
            flagOrder = false,
            navigateTo = R.id.fragmentForBasket
        )
        val manager = childFragmentManager
        menuDialog.show(manager, "Sign in")
    }

    private fun createAdapter(): ProductInBasketAdapter = ProductInBasketAdapter(object : Listener {
        override fun like(product: Product) {
            if (authViewModel.authenticated) {
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

        override fun goToProduct(view: View, product: Product) {
        }
    })

    private fun setAdapter() {
        binding.rwProducts.layoutManager = LinearLayoutManager(activity)
        binding.rwProducts.adapter = adapter
    }

    private fun setObserver() {
        viewModel.dataFull.observe(viewLifecycleOwner) { state ->
            binding.txEmptyBasket.isVisible = state.emptyBasket
            binding.amountOrder.isVisible = !state.emptyBasket
            binding.buttonOrder.isEnabled = !state.emptyBasket
            val list = state.products.filter { it.inBasket }
            adapter.submitList(list)
            viewModel.amountOrderN.value = viewModel.countOrder(list)
            if (state.emptyBasket) {
                orderViewModel.cancelOrder(viewModel.dataLanguage)
            }
        }

        viewModel.amountOrderN.observe(viewLifecycleOwner) {
            binding.amountOrder.text = "$it " + getString(R.string.rub)
        }
    }

    private fun setListeners() {
        binding.buttonOrder.setOnClickListener {
            viewModel.deleteFromBasketWeightZero()
            if (viewModel.dataFull.value?.emptyBasket == true) {
                orderViewModel.showPoint1.value = 0
                return@setOnClickListener
            }
            if (authViewModel.authenticated) {
                topTextViewModel.text.value = getString(R.string.order)
                if (orderViewModel.showPoint2.value != 0) {
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
    }

    private fun setSwipe() {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.START
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                val item = adapter.currentList[viewHolder.absoluteAdapterPosition]
                viewModel.deleteFromBasket(item)
            }
        }).attachToRecyclerView(binding.rwProducts)
    }
}
