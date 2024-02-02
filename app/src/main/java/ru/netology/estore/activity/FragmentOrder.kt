package ru.netology.estore.activity

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.estore.R
import ru.netology.estore.databinding.FragmentOrderBinding
import ru.netology.estore.dto.Data
import ru.netology.estore.dto.DataHistory
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.OrderViewModel
import ru.netology.estore.viewmodel.TopTextViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ofPattern


@AndroidEntryPoint
class FragmentOrder : Fragment() {

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
                        orderViewModel.cancelOrder()
                        topTextViewModel.text.value = Data.basketGroup
                        vieModel.pointBottomMenu.value = 1
                        findNavController().navigate(R.id.fragmentForBasket)
                    }
                }
            }
        }


        binding.point1.text = "1. Ваш заказ на ${vieModel.amountOrder.value} руб"
        binding.point2delivery.text = "2. ${orderViewModel.typeOfDelivery}"
        binding.point3Address.text = "3. ${orderViewModel.addressPickUp}"
        binding.addressDelivery.text = "3. ${orderViewModel.addressDelivery}"
        binding.textPayment.text = "4. ${orderViewModel.typeOfPayment}"

        val listOrder = vieModel.dataFull.value?.products
            ?.filter {
                it.inBasket
            }.orEmpty()

        for(i in listOrder.indices) {
            binding.textOrder.append("${i+1}. ${listOrder[i].name} ${listOrder[i].weight} ${listOrder[i].unitWeight}\n")
        }

        //заказ на сумму
        orderViewModel.showPoint1.observe(viewLifecycleOwner) {
            when(it) {
                0 -> {
                    binding.point1.visibility = View.GONE
                    binding.groupOrder.visibility = View.GONE
                    binding.buttonCorrectAmount.visibility = View.GONE
                    binding.textOrder.visibility = View.GONE
                }
                1 -> {
                    binding.point1.visibility = View.VISIBLE
                    binding.groupOrder.visibility = View.VISIBLE
                    binding.buttonCorrectAmount.visibility = View.GONE
                    binding.textOrder.visibility = View.VISIBLE
                }
                2 -> {
                    binding.point1.visibility = View.VISIBLE
                    binding.groupOrder.visibility = View.GONE
                    binding.buttonCorrectAmount.visibility = View.VISIBLE
                    binding.textOrder.visibility = View.GONE
                }
            }
        }
        // тип доставки
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
        // самовывоз
        orderViewModel.showPoint3.observe(viewLifecycleOwner) {
            when(it) {
                0 -> {
                    binding.point3Address.visibility = View.GONE
                    binding.radio.visibility = View.GONE
                    binding.buttonCorrectPickUp.visibility = View.GONE
                    binding.cardView3.visibility = View.GONE
                }
                1 -> {
                    binding.cardView3.visibility = View.VISIBLE
                    binding.point3Address.visibility = View.VISIBLE
                    binding.radio.visibility = View.VISIBLE
                    binding.buttonCorrectPickUp.visibility = View.GONE
                    binding.cardView4.visibility = View.GONE
                }
                2 -> {
                    binding.cardView3.visibility = View.VISIBLE
                    binding.point3Address.visibility = View.VISIBLE
                    binding.radio.visibility = View.GONE
                    binding.buttonCorrectPickUp.visibility = View.VISIBLE
                    binding.cardView4.visibility = View.GONE
                }
            }
        }
        // доставка
        orderViewModel.showPoint4.observe(viewLifecycleOwner) {
            when(it) {
                0 -> {
                    binding.addressDelivery.visibility = View.GONE
                    binding.textInputLayoutA.visibility = View.GONE
                    binding.buttonCorrectDelivery.visibility = View.GONE
                    binding.buttonEnterAddressOk.visibility = View.GONE
                    binding.cardView4.visibility = View.GONE
                }
                1 -> {
                    binding.cardView3.visibility = View.GONE
                    binding.cardView4.visibility = View.VISIBLE
                    binding.addressDelivery.visibility = View.VISIBLE
                    binding.textInputLayoutA.visibility = View.VISIBLE
                    binding.buttonCorrectDelivery.visibility = View.GONE
                    binding.buttonEnterAddressOk.visibility = View.VISIBLE
                }
                2 -> {
                    binding.cardView3.visibility = View.GONE
                    binding.cardView4.visibility = View.VISIBLE
                    binding.addressDelivery.visibility = View.VISIBLE
                    binding.textInputLayoutA.visibility = View.GONE
                    binding.buttonCorrectDelivery.visibility = View.VISIBLE
                    binding.buttonEnterAddressOk.visibility = View.GONE
                }
            }
        }

        // тип оплаты
        orderViewModel.showPoint5.observe(viewLifecycleOwner) {
            when(it) {
                0 -> {
                    binding.textPayment.visibility = View.GONE
                    binding.spinnerTypeOfPayment.visibility = View.GONE
                    binding.buttonCorrectPayment.visibility = View.GONE
                }
                1 -> {
                    binding.textPayment.visibility = View.VISIBLE
                    binding.spinnerTypeOfPayment.visibility = View.VISIBLE
                    binding.buttonCorrectPayment.visibility = View.GONE
                }
                2 -> {
                    binding.textPayment.visibility = View.VISIBLE
                    binding.spinnerTypeOfPayment.visibility = View.GONE
                    binding.buttonCorrectPayment.visibility = View.VISIBLE
                    binding.groupEndOfOrder.visibility = View.VISIBLE
                }
            }
        }

        // завершение заказа
        orderViewModel.goToFinalOrder.observe(viewLifecycleOwner) {
            when(it) {
                0 -> binding.cardViewFinalOrder.visibility = View.GONE
                1 -> {
                    vieModel.pointBottomMenu.value = -2
                    val timeOrder = OffsetDateTime.now().plusHours(1L)
                    val timeOrderTime = timeOrder.format((ofPattern("HH:mm")))
                    val timeOrderDate = timeOrder.format(ofPattern("d MMMM uuuu HH:mm")).toString()
                    val dataHistory = DataHistory(
                        id=0L,
                        login = authViewModel.data.value.username ?: "",
                        sumOrder = vieModel.amountOrder.value ?: 0.0,
                        pickUp = orderViewModel.flagPickUp,
                        dateTime = timeOrderDate)
                    vieModel.addHistory(dataHistory)
                    binding.cardView1.visibility = View.GONE
                    binding.cardView2.visibility = View.GONE
                    binding.cardView3.visibility = View.GONE
                    binding.cardView4.visibility = View.GONE
                    binding.cardView5.visibility = View.GONE
                    binding.buttonCancelOrder.visibility = View.GONE
                    binding.buttonToWaitingOrder.visibility = View.GONE
                    binding.cardViewFinalOrder.visibility = View.VISIBLE
                    binding.textInfoWaitingOrder.text = if(orderViewModel.flagPickUp) "Вы сможете забрать Ваш заказ через 1 час в $timeOrderTime"
                    else "Мы привезем Ваш заказ через 1 час в $timeOrderTime"
                }
            }
        }

        //заказ на сумму
        binding.buttonPoint1Yes.setOnClickListener {
            orderViewModel.showPoint1.value = 2
            orderViewModel.showPoint2.value = 1
        }

        binding.buttonPoint1No.setOnClickListener {
            goToBasket()
        }
        binding.buttonCorrectAmount.setOnClickListener {
            goToBasket()
        }

        //доставка/самовывоз
        binding.buttonDelivery.setOnClickListener {
            showDelivery(2,"Доставка")
            orderViewModel.flagPickUp = false
            orderViewModel.showPoint3.value = 0
            orderViewModel.showPoint4.value = 1
            orderViewModel.addressDelivery = "Куда Вам привезти?"
            binding.addressDelivery.text = orderViewModel.addressDelivery
        }

        binding.buttonPickup.setOnClickListener {
            showDelivery(2,"Самовывоз")
            orderViewModel.flagPickUp = true
            orderViewModel.showPoint3.value = 1
            orderViewModel.showPoint4.value = 0
            orderViewModel.addressPickUp = "Выберите магазин, откуда заберете"
            binding.point3Address.text = orderViewModel.addressPickUp
        }

        binding.buttonCorrectTypeDelivery.setOnClickListener {
            showDelivery(1, "Сами заберете или Вам привезти?")
                 binding.radio.clearCheck()
            orderViewModel.showPoint3.value = 0
            orderViewModel.showPoint4.value = 0
        }

        //самовывоз
        binding.radio.setOnCheckedChangeListener { group, checkedId ->
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
                    if(orderViewModel.showPoint5.value!=2) {
                        orderViewModel.showPoint5.value = 1
                    }
                }
        }

        binding.buttonCorrectPickUp.setOnClickListener {
            binding.radio.clearCheck()
            orderViewModel.addressPickUp =  "Выберите магазин, откуда заберете"
            binding.point3Address.text = "3. ${orderViewModel.addressPickUp}"
            orderViewModel.showPoint3.value = 1
        }

        //доставка
        binding.buttonEnterAddressOk.setOnClickListener {
            if(fillField()) {
                orderViewModel.addressDelivery = binding.editAddressDelivery.text.toString()
                binding.addressDelivery.text = "3. ${orderViewModel.addressDelivery}"
                orderViewModel.showPoint4.value = 2
                orderViewModel.showPoint3.value = 0
                if(orderViewModel.showPoint5.value!=2) {
                    orderViewModel.showPoint5.value = 1
                }
            }
        }

        binding.buttonCorrectDelivery.setOnClickListener {
            orderViewModel.addressDelivery = "Куда Вам привезти?"
            orderViewModel.showPoint4.value = 1
        }

        //тип оплаты
        binding.spinnerTypeOfPayment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                itemSelected: View, selectedItemPosition: Int, selectedId: Long
            ) {
                (parent!!.getChildAt(0) as TextView).setTextColor(Color.BLACK)
                (parent!!.getChildAt(0) as TextView).textSize = 15f
                val choose = resources.getStringArray(ru.netology.estore.R.array.payment)
                orderViewModel.typeOfPayment = "Способ оплаты: ${choose[selectedItemPosition].toString()}"
                binding.textPayment.text = "4. ${orderViewModel.typeOfPayment}"
                orderViewModel.showPoint5.value = 2
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.buttonCorrectPayment.setOnClickListener {
            orderViewModel.typeOfPayment = "Выберите способ оплаты"
            binding.textPayment.text = "4. ${orderViewModel.typeOfPayment}"
            orderViewModel.showPoint5.value = 1
        }

        //заказать/отменить/завершить
        binding.buttonCancelOrder.setOnClickListener {
            areYouSureCancelOrder()
        }

        binding.buttonToWaitingOrder.setOnClickListener {
            orderViewModel.goToFinalOrder.value = 1
        }

        binding.buttonThankYou.setOnClickListener {
            orderViewModel.cancelOrder()
            topTextViewModel.text.value = Data.eStoreGroup
            vieModel.cleanBasket()
            vieModel.pointBottomMenu.value = -1
            findNavController().navigate(R.id.blankFragment)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentOrder()
    }

    private fun showDelivery(point:Int, text:String) {
        orderViewModel.showPoint2.value = point
        orderViewModel.typeOfDelivery = text
        binding.point2delivery.text = "2. ${orderViewModel.typeOfDelivery}"
    }

    private fun goToBasket() {
        topTextViewModel.text.value = Data.basketGroup
        vieModel.pointBottomMenu.value = 1
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

    private fun areYouSureCancelOrder() {
        val menuDialog = SignInOutDialogFragment(
            title = "Отмена заказа",
            text = "Вы уверены, что хотите отменить заказ?",
            icon = R.drawable.warning_24,
            textPosButton = "Отменить",
            textNegButton = "Заказать",
            flagSignIn = false,
            flagOrder = true,
            navigateTo = 0
        )
        val manager = childFragmentManager
        menuDialog.show(manager, "Cancel order")
    }
}