package ru.netology.estore.activity

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.netology.estore.R
import ru.netology.estore.databinding.FragmentBlankBinding
import ru.netology.estore.databinding.FragmentOrderBinding

class BlankFragment : Fragment() {

    lateinit var binding: FragmentBlankBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBlankBinding.inflate(inflater, container, false)

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0F, 1.2F, 1F)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0F, 1.3F, 1F)
        ObjectAnimator.ofPropertyValuesHolder(binding.textEstore, scaleX, scaleY).apply {
            duration = 2000
        }.start()

        return binding.root
    }

    companion object {

        fun newInstance() =
            BlankFragment()
    }
}