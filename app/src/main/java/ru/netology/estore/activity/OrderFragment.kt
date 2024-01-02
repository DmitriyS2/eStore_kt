package ru.netology.estore.activity

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.view.get
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
        binding.point3Address.text = "3. ${orderViewModel.addressPickUp}"
        binding.addressDelivery.text = "3. ${orderViewModel.addressDelivery}"

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
                    binding.buttonCorrectTypeDelivery.visibility = View.GONE
                }
                1 -> {
                    binding.point2delivery.visibility = View.VISIBLE
                    binding.groupDelivery.visibility = View.VISIBLE
                    binding.buttonCorrectTypeDelivery.visibility = View.GONE
                }
                2 -> {
                    binding.point2delivery.visibility = View.VISIBLE
                    binding.groupDelivery.visibility = View.GONE
                    binding.buttonCorrectTypeDelivery.visibility = View.VISIBLE
                }
            }
        }

        orderViewModel.showPoint3.observe(viewLifecycleOwner) {
            when(it) {
                0 -> {
                    binding.point3Address.visibility = View.GONE
                    binding.radio.visibility = View.GONE
                    binding.buttonCorrectPickUp.visibility = View.GONE
                }
                1 -> {
                    binding.point3Address.visibility = View.VISIBLE
                    binding.radio.visibility = View.VISIBLE
                    binding.buttonCorrectPickUp.visibility = View.GONE
                }
                2 -> {
                    binding.point3Address.visibility = View.VISIBLE
                    binding.radio.visibility = View.GONE
                    binding.buttonCorrectPickUp.visibility = View.VISIBLE
                }
            }
        }

        orderViewModel.showPoint4.observe(viewLifecycleOwner) {
            when(it) {
                0 -> {
                    binding.addressDelivery.visibility = View.GONE
                    binding.textInputLayoutA.visibility = View.GONE
                    binding.buttonCorrectDelivery.visibility = View.GONE
                    binding.buttonEnterAddressOk.visibility = View.GONE
                }
                1 -> {
                    binding.addressDelivery.visibility = View.VISIBLE
                    binding.textInputLayoutA.visibility = View.VISIBLE
                    binding.buttonCorrectDelivery.visibility = View.GONE
                    binding.buttonEnterAddressOk.visibility = View.VISIBLE
                }
                2 -> {
                    binding.addressDelivery.visibility = View.VISIBLE
                    binding.textInputLayoutA.visibility = View.GONE
                    binding.buttonCorrectDelivery.visibility = View.VISIBLE
                    binding.buttonEnterAddressOk.visibility = View.GONE
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
            showDelivery(2,"Доставка")
            orderViewModel.showPoint3.value = 0
            orderViewModel.showPoint4.value = 1
            orderViewModel.addressDelivery = "Куда Вам привезти?"
            binding.addressDelivery.text = orderViewModel.addressDelivery
         //   binding.point3Address.visibility = View.GONE
        }
        binding.buttonPickup.setOnClickListener {
            showDelivery(2,"Самовывоз")
            orderViewModel.showPoint3.value = 1
            orderViewModel.showPoint4.value = 0
            orderViewModel.addressPickUp = "Выберите магазин, откуда заберете"
            binding.point3Address.text = orderViewModel.addressPickUp
         //   binding.radio.clearCheck()
        }
        binding.buttonCorrectTypeDelivery.setOnClickListener {
            showDelivery(1, "Сами заберете или Вам привезти?")
                 binding.radio.clearCheck()
            orderViewModel.showPoint3.value = 0
            orderViewModel.showPoint4.value = 0
          //  orderViewModel.addressPickUp =  "Выберите магазин, откуда заберете"
        }


        binding.radio.setOnCheckedChangeListener { group, checkedId ->
          //  if(orderViewModel.showPoint3.value!=0) {
                if(checkedId==-1){
                    orderViewModel.showPoint3.value = 0
                    orderViewModel.addressPickUp =  "Выберите магазин, откуда заберете"
                    binding.point3Address.text = "3. ${orderViewModel.addressPickUp}"
                }

                val radioButton = group.findViewById<RadioButton>(checkedId)
                Log.d("MyLog", "checkId=$checkedId")
                radioButton?.let {
                    orderViewModel.addressPickUp = radioButton.text as String
                    orderViewModel.showPoint3.value = 2
                    binding.point3Address.text = "3. ${orderViewModel.addressPickUp}"
                }
            //    binding.point3Address.text = "3. ${orderViewModel.addressPickUp}"
        //    }
        }

        binding.buttonCorrectPickUp.setOnClickListener {
            binding.radio.clearCheck()
            orderViewModel.addressPickUp =  "Выберите магазин, откуда заберете"
            binding.point3Address.text = "3. ${orderViewModel.addressPickUp}"
            orderViewModel.showPoint3.value = 1
        }


        binding.buttonEnterAddressOk.setOnClickListener {
            if(fillField()) {
                orderViewModel.addressDelivery = binding.editAddressDelivery.text.toString()
                binding.addressDelivery.text = "3. ${orderViewModel.addressDelivery}"
                orderViewModel.showPoint4.value = 2
                orderViewModel.showPoint3.value = 0
            }
        }

        binding.buttonCorrectDelivery.setOnClickListener {
            orderViewModel.addressDelivery = "Куда Вам привезти?"
            orderViewModel.showPoint4.value = 1
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = OrderFragment()
    }

    private fun showDelivery(point:Int, text:String) {
        orderViewModel.showPoint2.value = point
        orderViewModel.typeOfDelivery = text
        binding.point2delivery.text = "2. ${orderViewModel.typeOfDelivery}"
    }

    private fun goToBasket() {
        topTextViewModel.text.value = Data.basketGroup
        findNavController().navigate(R.id.fragmentForBasket)
    }

    private fun fillField():Boolean {
        var flag = true
        binding.apply {
            if (editAddressDelivery.text.isNullOrEmpty()) {
                editAddressDelivery.error = "Поле должно быть заполнено"
                flag = false
            }
        }
        return flag
    }
}