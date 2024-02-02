package ru.netology.estore.activity

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.estore.R
import ru.netology.estore.databinding.FragmentSignUpBinding
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.SignUpViewModel
import ru.netology.estore.viewmodel.TopTextViewModel

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignUpBinding

    private val signUpViewModel: SignUpViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val topTextViewModel: TopTextViewModel by activityViewModels()
    private val viewModel:MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)

        binding.buttonSignUp.setOnClickListener {
            if (isFieldNotNull()) {

                signUpViewModel.signUp(
                    binding.login.text.toString(),
                    binding.password.text.toString(),
                    binding.name.text.toString()
                )

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        authViewModel.data.collectLatest {
                            delay(50)
                            if (it.id != 0L) {
                                viewModel.getHistory(authViewModel.data.value.username)
                                Log.d("MyLog", "SignUp OK, login=${it.username}")
                                topTextViewModel.text.value =
                                    ru.netology.estore.dto.Data.basketGroup
                                viewModel.pointBottomMenu.value = 0
                                findNavController().navigate(R.id.fragmentForCatalog)
                            } else {
                                val toast = Toast.makeText(
                                    requireActivity(),
                                    "Такой login уже существует",
                                    Toast.LENGTH_SHORT
                                )
                                toast.setGravity(Gravity.TOP, 0, 0)
                                toast.show()
                            }
                        }
                    }
                }
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