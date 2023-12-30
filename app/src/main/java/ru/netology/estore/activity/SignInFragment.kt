package ru.netology.estore.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
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

    @SuppressLint("ShowToast")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        binding.buttonSignIn.setOnClickListener {
            if (isFieldNotNull()) {

               // Log.d()
                viewModel.signIn(binding.login.text.toString(), binding.password.text.toString())

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        authViewModel.data.collectLatest {
                            delay(50)
         //       if(authViewModel.data.value.id!=0L) {
                            if(it.id!=0L) {
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
//                if(authViewModel.authenticated) {
//                    topTextViewModel.text.value = ru.netology.estore.dto.Data.basketGroup
//                    childFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.nav_host_fragment, FragmentForCatalog())
//                        .commit()
//                } else {
//                    val toast = Toast.makeText(requireActivity(), "Неверный login/password", Toast.LENGTH_SHORT)
//                    toast.setGravity(Gravity.TOP, 0, 0)
//                    toast.show()
//                //    Snackbar.make(it, "Неверный login/password", Snackbar.LENGTH_SHORT).show()
//                }
//
//                findNavController().navigateUp()

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