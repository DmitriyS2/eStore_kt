package ru.netology.estore.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import ru.netology.estore.databinding.FragmentSignInBinding
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.SignInViewModel
import ru.netology.estore.viewmodel.TopTextViewModel

@AndroidEntryPoint
class SignInFragment : Fragment() {
    lateinit var binding:FragmentSignInBinding

    private val signInViewModel: SignInViewModel by activityViewModels()
    private val authViewModel:AuthViewModel by activityViewModels()
    private val topTextViewModel:TopTextViewModel by activityViewModels()
    private val viewModel:MainViewModel by activityViewModels()

    @SuppressLint("ShowToast")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        binding.buttonSignIn.setOnClickListener {
            if (isFieldNotNull()) {
                signInViewModel.signInApi(binding.login.text.toString(), binding.password.text.toString())

                signInViewModel.stateAuth.observe(viewLifecycleOwner) {
                    when(it) {
                        // SignIn Ok
                        1 -> {
                            viewModel.getHistory(authViewModel.data.value.username)
                                Log.d("MyLog", "SignIn OK, username=${authViewModel.data.value.username}")
                                viewModel.pointBottomMenu.value = 1
                                topTextViewModel.text.value = ru.netology.estore.dto.Data.basketGroup
                                findNavController().navigate(R.id.fragmentForBasket)
                            signInViewModel.stateAuth.value = 0
                            Log.d("MyLog", "signInFragment signInViewModel.stateAuth.value=${signInViewModel.stateAuth.value}")
                        }

                        // на сервере нет такого user'а - начинаем поиск в ДБ
                        -1 -> signInViewModel.signIn(binding.login.text.toString(), binding.password.text.toString())

                        // в ДБ тоже нет такого user'а - значит неверный логин/пароль
                        -2 ->  {
                            val toast = Toast.makeText(requireActivity(), "Неверный login/password", Toast.LENGTH_SHORT)
                            toast.setGravity(Gravity.TOP, 0, 0)
                            toast.show()
                            signInViewModel.stateAuth.value = 0
                        }
                    }
                }
           }
        }
        return binding.root
    }

    private fun isFieldNotNull(): Boolean {
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
        }
        return flag
    }
}