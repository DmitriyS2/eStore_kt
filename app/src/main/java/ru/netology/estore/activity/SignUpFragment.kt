package ru.netology.estore.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.estore.R
import ru.netology.estore.databinding.FragmentSignUpBinding
import ru.netology.estore.viewmodel.SignUpViewModel

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding

    private val signUpViewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.buttonSignUp.setOnClickListener {
            if (isFieldNotNull()) {

                signUpViewModel.signUp(binding.login.text.toString(), binding.password.text.toString(), binding.name.text.toString())

            //    findNavController().navigateUp()
            }
        }

        return binding.root
    }

    fun isFieldNotNull(): Boolean {
        var flag = true

        binding.apply {
            if (login.text.isNullOrEmpty()) {
                login.error = "Поле должно быть заполнено"
                flag = false
            }

            if (password.text.isNullOrEmpty()) {
                password.error = "Поле должно быть заполнено"
                flag = false
            }
            if (name.text.isNullOrEmpty()) {
                name.error = "Поле должно быть заполнено"
                flag = false
            }

            if (confirmPassword.text.isNullOrEmpty()) {
                confirmPassword.error = "Поле должно быть заполнено"
                flag = false
            }
            if(password.text.toString()!=confirmPassword.text.toString()) {
                password.error = "Пароль не совпадает"
                confirmPassword.error = "Пароль не совпадает"
                flag = false
            }
        }
        return flag
    }
}