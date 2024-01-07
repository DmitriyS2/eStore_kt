package ru.netology.estore

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.netology.estore.databinding.FragmentWaitingOrderBinding
import ru.netology.estore.receiver.AlarmReceiver
import java.util.Calendar

class FragmentWaitingOrder : Fragment() {

    lateinit var binding:FragmentWaitingOrderBinding

    lateinit var alarmIntent:PendingIntent

    private var alarmManager:AlarmManager? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaitingOrderBinding.inflate(inflater, container, false)

        alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager

        alarmIntent = Intent(context, AlarmReceiver::class.java).let {intent ->
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }

        binding.timePicker.setIs24HourView(true)

        binding.buttonStartTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            Log.d("MyLog", "calendar = $calendar")
            calendar.set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
            calendar.set(Calendar.MINUTE, binding.timePicker.minute)
            Log.d("MyLog", "calendarNew.Hour = ${calendar.get(Calendar.HOUR_OF_DAY)}")
            alarmManager?.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
        }

        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = FragmentWaitingOrder()
    }
}