package ru.netology.estore.activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.estore.R
import ru.netology.estore.auth.AppAuth
import ru.netology.estore.databinding.FragmentSignInOutDialogBinding
import ru.netology.estore.dto.Data
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.OrderViewModel
import ru.netology.estore.viewmodel.SignInViewModel
import ru.netology.estore.viewmodel.TopTextViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SignInOutDialogFragment(
    val title: String,
    val text: String,
    val icon: Int,
    val textPosButton: String,
    val textNegButton: String,
    val flagSignIn: Boolean = true,
    val flagSignUp:Boolean = true
) : DialogFragment() {
    lateinit var binding: FragmentSignInOutDialogBinding

    private val topTextViewModel: TopTextViewModel by activityViewModels()
    private val orderViewModel:OrderViewModel by activityViewModels()

    private val authViewModel: AuthViewModel by activityViewModels()

    private val viewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var auth: AppAuth

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = FragmentSignInOutDialogBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())

        binding.fragmentDialog.text = text
        builder.setView(binding.root)
        return builder
            .setIcon(icon)
            .setTitle(title)
            .setCancelable(true)
            .setPositiveButton(textPosButton) { _, _ ->
                dialog?.cancel()
                if (flagSignIn) {
                    topTextViewModel.text.value = "SignIn"
                    findNavController()
                        .navigate(R.id.signInFragment)
                } else if(flagSignUp) {

                    topTextViewModel.text.value = Data.allGroup
                    findNavController()
                        .navigate(R.id.fragmentForCatalog)
                } else {
                    auth.removeAuth()
                    topTextViewModel.text.value = Data.allGroup
                    orderViewModel.cancelOrder()
                    viewModel.getHistoryOfOrders(authViewModel.data.value.login)
                    findNavController()
                        .navigate(R.id.fragmentForCatalog)
                }
            }
            .setNegativeButton(textNegButton) { _, _ ->
                dialog?.cancel()
                if (flagSignIn) {
                    topTextViewModel.text.value = Data.basketGroup
                    findNavController()
                        .navigate(R.id.fragmentForBasket)
                }
            }
            .create()
    }
}