package ru.netology.estore

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import ru.netology.estore.activity.SignInOutDialogFragment
import ru.netology.estore.databinding.FragmentWaitingOrderBinding
import ru.netology.estore.receiver.AlarmReceiver
import ru.netology.estore.viewmodel.OrderViewModel
import java.util.Calendar

class FragmentWaitingOrder : Fragment() {

    private val orderViewModel:OrderViewModel by activityViewModels()

    lateinit var binding:FragmentWaitingOrderBinding

    lateinit var alarmIntent:PendingIntent

    private var alarmManager:AlarmManager? = null

    private var timer: CountDownTimer? = null

    private var flagStart = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaitingOrderBinding.inflate(inflater, container, false)

//        alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
//
//        alarmIntent = Intent(context, AlarmReceiver::class.java).let {intent ->
//            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//        }

   //     binding.timePicker.setIs24HourView(true)

        binding.textInfoWaitingOrder.text = if(orderViewModel.flagPickUp) "Вы сможете забрать Ваш заказ через" else "Мы привезем Ваш заказ через"

        binding.buttonStartTime.setOnClickListener {
//            val calendar = Calendar.getInstance()
//            Log.d("MyLog", "calendar = $calendar")
//            calendar.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
//            calendar.set(Calendar.MINUTE, binding.timePicker.minute)
//            Log.d("MyLog", "calendarNew.Hour = ${calendar.get(Calendar.HOUR_OF_DAY)}")
//            alarmManager?.setExact(
//                AlarmManager.RTC_WAKEUP,
//                calendar.timeInMillis,
//                alarmIntent
//            )
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonStartTime,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.1F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.1F, 1.0F)
            ).start()

            if(flagStart) {
                startCountDownTimer(10000)
                flagStart = false
                binding.buttonStartTime.setText("Приостановить")
            } else {
                timer?.cancel()
                flagStart = true
                binding.buttonStartTime.setText("Заказать")
            }

        }

        binding.buttonCancelTime.setOnClickListener {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonCancelTime,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.1F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.1F, 1.0F)
            ).start()
            areYouSureCancelOrder()
        }

        binding.buttonReady.setOnClickListener {
            ObjectAnimator.ofPropertyValuesHolder(
                binding.buttonReady,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.1F, 1.0F),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.1F, 1.0F)
            ).start()

        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = FragmentWaitingOrder()
    }

    private fun startCountDownTimer(timeMillis:Long) {
        timer?.cancel()
        timer = object:CountDownTimer(timeMillis, 1000) {

            override fun onTick(p0: Long) {
        //        binding.timer.setTextSize(40F)
                binding.timer.text = (p0/1000).toString()+" сек"
            }

            override fun onFinish() {
             binding.buttonStartTime.visibility = View.GONE
                binding.buttonCancelTime.visibility = View.GONE
                binding.buttonReady.visibility = View.VISIBLE
                binding.timer.visibility = View.GONE
                binding.textInfoWaitingOrder.setTextSize(48F)
                binding.textInfoWaitingOrder.text = "ГОТОВО!"

            }
        }.start()
    }

    fun areYouSureCancelOrder() {
        val menuDialog = SignInOutDialogFragment(
            title = "Отмена заказа",
            text = "Вы уверены, что хотите отменить?",
            icon = R.drawable.warning_24,
            textPosButton = "Выйти",
            textNegButton = "Остаться",
            flagSignIn = false,
            flagSignUp = false
        )
        val manager = childFragmentManager
        menuDialog.show(manager, "Cancel order")
    }
}