package ru.netology.estore.activity

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.estore.R
import ru.netology.estore.databinding.FragmentSignUpBinding
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.SignInViewModel
import ru.netology.estore.viewmodel.SignUpViewModel
import ru.netology.estore.viewmodel.TopTextViewModel

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding

    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val topTextViewModel: TopTextViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.buttonSignUp.setOnClickListener {
            if (isFieldNotNull()) {

                signUpViewModel.signUp(binding.login.text.toString(), binding.password.text.toString(), binding.name.text.toString())

                if(authViewModel.authenticated) {
                    topTextViewModel.text.value = ru.netology.estore.dto.Data.basketGroup
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, FragmentForCatalog())
                        .commit()
                } else {
                    val toast = Toast.makeText(requireActivity(), "Такой login уже существует", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP, 0, 0)
                    toast.show()
                    //    Snackbar.make(it, "Неверный login/password", Snackbar.LENGTH_SHORT).show()
                }
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