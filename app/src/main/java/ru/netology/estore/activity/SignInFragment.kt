package ru.netology.estore.activity

import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.estore.R
import ru.netology.estore.databinding.FragmentSignInBinding
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.SignInViewModel
import ru.netology.estore.viewmodel.TopTextViewModel

@AndroidEntryPoint
class SignInFragment : Fragment() {
    lateinit var binding:FragmentSignInBinding

    private val viewModel: SignInViewModel by activityViewModels()
    private val authViewModel:AuthViewModel by activityViewModels()
    private val topTextViewModel:TopTextViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        binding.buttonSignIn.setOnClickListener {
            if (isFieldNotNull()) {

               // Log.d()
                viewModel.signIn(binding.login.text.toString(), binding.password.text.toString())

                if(authViewModel.authenticated) {
                    topTextViewModel.text.value = ru.netology.estore.dto.Data.basketGroup
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.placeHolder, FragmentForBasket())
                        .commit()
                }


//                runBlocking {
//                    delay(1000)
//                    val response:String = if(authViewModel.authenticated) "Вы вошли как:${binding.login.text.toString()}" else "Ошибка входа"
//                    Toast.makeText(activity, response, Toast.LENGTH_SHORT).show()
//                 //   Toast.makeText(activity, "Вы вошли как:${binding.login.text.toString()}", Toast.LENGTH_SHORT).show()
//                    delay(2000)
//                }

         //       findNavController().navigateUp()
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