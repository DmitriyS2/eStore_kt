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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
                signInViewModel.signIn(binding.login.text.toString(), binding.password.text.toString())

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        authViewModel.data.collectLatest {
                            delay(50)
                            if(it.id!=0L) {
                                viewModel.getHistory(authViewModel.data.value.login)
                                Log.d("MyLog", "SignIn OK, login=${it.login}")
                                viewModel.pointBottomMenu.value = 1
                                topTextViewModel.text.value = ru.netology.estore.dto.Data.basketGroup
                                findNavController().navigate(R.id.fragmentForBasket)
                            } else {
                                val toast = Toast.makeText(requireActivity(), "Неверный login/password", Toast.LENGTH_SHORT)
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