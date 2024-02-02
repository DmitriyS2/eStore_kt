package ru.netology.estore.activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
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
    val flagOrder: Boolean = true,
    val navigateTo: Int
) : DialogFragment() {
    lateinit var binding: FragmentSignInOutDialogBinding

    private val topTextViewModel: TopTextViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()
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
                    topTextViewModel.text.value = Data.signInGroup
                    viewModel.pointBottomMenu.value = -1
                    findNavController()
                        .navigate(R.id.signInFragment)
                } else if (flagOrder) {
                    topTextViewModel.text.value = Data.allGroup
                    orderViewModel.cancelOrder()
                    viewModel.pointBottomMenu.value = 0
                    findNavController()
                        .navigate(R.id.fragmentForCatalog)
                } else {
                    auth.removeAuth()
                    viewModel.reNewDataFull()
                    topTextViewModel.text.value = Data.allGroup
                    orderViewModel.cancelOrder()
                    viewModel.getHistory(authViewModel.data.value.username)
                    viewModel.pointBottomMenu.value = 0
                    findNavController()
                        .navigate(R.id.fragmentForCatalog)
                }
            }
            .setNegativeButton(textNegButton) { _, _ ->
                dialog?.cancel()
                if (flagSignIn) {
                    if (navigateTo == R.id.fragmentForCatalog) {
                        viewModel.pointBottomMenu.value = 0
                        topTextViewModel.text.value = Data.allGroup

                    } else if (navigateTo == R.id.fragmentForBasket) {
                        viewModel.pointBottomMenu.value = 1
                        topTextViewModel.text.value = Data.basketGroup
                    }
                    findNavController()
                        .navigate(navigateTo)
                }
            }
            .create()
    }
}