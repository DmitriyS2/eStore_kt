package ru.netology.estore.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.estore.R
import ru.netology.estore.databinding.FragmentOrderBinding
import ru.netology.estore.dto.Data
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.OrderViewModel
import ru.netology.estore.viewmodel.TopTextViewModel

class OrderFragment : Fragment() {

    private val vieModel:MainViewModel by activityViewModels()
    private val authViewModel:AuthViewModel by activityViewModels()
    private val topTextViewModel:TopTextViewModel by activityViewModels()
    private val orderViewModel:OrderViewModel by activityViewModels()

    lateinit var binding: FragmentOrderBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrderBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.data.collectLatest {
                    if(it.id==0L) {
                        topTextViewModel.text.value = Data.basketGroup
                        findNavController().navigate(R.id.fragmentForBasket)
                    }
                }
            }
        }

        binding.point1.text = "1. Ваш заказ на ${vieModel.amountOrder.value} руб"
        binding.point2delivery.text = "2. ${orderViewModel.typeOfDelivery}"

        val listOrder = vieModel.dataFull.value?.products
            ?.filter {
                it.inBasket
            }.orEmpty()

        for(i in listOrder.indices) {
            binding.textOrder.append("${i+1}. ${listOrder[i].name} ${listOrder[i].weight} ${listOrder[i].unitWeight}\n")
        }

        orderViewModel.showPoint1.observe(viewLifecycleOwner) {
            when(it) {
                0 -> {
                    binding.point1.visibility = View.VISIBLE
                    binding.groupOrder.visibility = View.VISIBLE
                    binding.buttonCorrectAmount.visibility = View.GONE
                    binding.textOrder.visibility = View.VISIBLE
                }
                1 -> {
                    binding.point1.visibility = View.VISIBLE
                    binding.groupOrder.visibility = View.GONE
                    binding.buttonCorrectAmount.visibility = View.VISIBLE
                    binding.textOrder.visibility = View.GONE
                }
            }
        }

        orderViewModel.showPoint2.observe(viewLifecycleOwner) {
            when(it) {
                0 -> {
                    binding.point2delivery.visibility = View.GONE
                    binding.groupDelivery.visibility = View.GONE
                    binding.buttonCorrectDelivery.visibility = View.GONE
                }
                1 -> {
                    binding.point2delivery.visibility = View.VISIBLE
                    binding.groupDelivery.visibility = View.VISIBLE
                    binding.buttonCorrectDelivery.visibility = View.GONE
                }
                2 -> {
                    binding.point2delivery.visibility = View.VISIBLE
                    binding.groupDelivery.visibility = View.GONE
                    binding.buttonCorrectDelivery.visibility = View.VISIBLE
                }
            }
        }

        binding.buttonPoint1Yes.setOnClickListener {
            orderViewModel.showPoint1.value = 1
            orderViewModel.showPoint2.value = 1
        }

        binding.buttonPoint1No.setOnClickListener {
            goToBasket()
        }
        binding.buttonCorrectAmount.setOnClickListener {
            goToBasket()
        }

        binding.buttonDelivery.setOnClickListener {
            showDelivery("Доставка")
        }
        binding.buttonPickup.setOnClickListener {
            showDelivery("Самовывоз")
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = OrderFragment()
    }

    private fun showDelivery(text:String) {
        orderViewModel.showPoint2.value = 2
        orderViewModel.typeOfDelivery = text
//        binding.groupDelivery.visibility = View.GONE
//        binding.point2delivery.visibility = View.VISIBLE
        binding.point2delivery.text = "2. ${orderViewModel.typeOfDelivery}"
 //       binding.buttonCorrectDelivery.visibility = View.VISIBLE
    }

    private fun goToBasket() {
        topTextViewModel.text.value = Data.basketGroup
        findNavController().navigate(R.id.fragmentForBasket)
    }
}