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
import ru.netology.estore.dto.DataEng
import ru.netology.estore.dto.DataRus
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
    val flagSignOut: Boolean = false,
    val navigateTo: Int
) : DialogFragment() {
    private val topTextViewModel: TopTextViewModel by activityViewModels()
    private val orderViewModel: OrderViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val viewModel: MainViewModel by activityViewModels()

    @Inject
    lateinit var auth: AppAuth

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = FragmentSignInOutDialogBinding.inflate(layoutInflater)
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
                    topTextViewModel.text.value = getString(R.string.sign_in)
                    viewModel.pointBottomMenu.value = -1
                    findNavController()
                        .navigate(R.id.signInFragment)
                } else if (flagOrder) {
                    topTextViewModel.text.value = viewModel.dataLanguage.allGroup
                    orderViewModel.cancelOrder(viewModel.dataLanguage)
                    viewModel.pointBottomMenu.value = 0
                    findNavController()
                        .navigate(R.id.fragmentForCatalog)
                } else if (flagSignOut) {
                    auth.removeAuth()
                    viewModel.reNewDataFull()
                    topTextViewModel.text.value = viewModel.dataLanguage.allGroup
                    orderViewModel.cancelOrder(viewModel.dataLanguage)
                    viewModel.getHistory(authViewModel.data.value.username)
                    viewModel.pointBottomMenu.value = 0
                    findNavController()
                        .navigate(R.id.fragmentForCatalog)
                } else {
                    viewModel.dataLanguage = DataRus
                    orderViewModel.cancelOrder(viewModel.dataLanguage)
                    viewModel.language.value = "ru"
                }
            }
            .setNegativeButton(textNegButton) { _, _ ->
                dialog?.cancel()
                if (flagSignIn) {
                    if (navigateTo == R.id.fragmentForCatalog) {
                        viewModel.pointBottomMenu.value = 0
                        topTextViewModel.text.value = viewModel.dataLanguage.allGroup

                    } else if (navigateTo == R.id.fragmentForBasket) {
                        viewModel.pointBottomMenu.value = 1
                        topTextViewModel.text.value = viewModel.dataLanguage.basketGroup
                    }
                    findNavController()
                        .navigate(navigateTo)
                } else if (!flagSignOut && !flagOrder) {
                    viewModel.dataLanguage = DataEng
                    orderViewModel.cancelOrder(viewModel.dataLanguage)
                    viewModel.language.value = "en"
                }
            }.create()
    }
}