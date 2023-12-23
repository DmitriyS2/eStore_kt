package ru.netology.estore.activity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.estore.R
import ru.netology.estore.auth.AppAuth
import ru.netology.estore.databinding.FragmentSignInOutDialogBinding
import javax.inject.Inject

@AndroidEntryPoint
class SignInOutDialogFragment(
    val title: String,
    val text: String,
    val icon: Int,
    val textPosButton: String,
    val textNegButton: String,
    val flagSignIn: Boolean = true
) : DialogFragment() {
    lateinit var binding: FragmentSignInOutDialogBinding

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
                    findNavController()
                        .navigate(R.id.signInFragment)
//                    childFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.nav_host_fragment, SignInFragment())
//                        .commit()
                    //     findNavController().navigate(R.id.authenticationFragment)
                } else {
                    auth.removeAuth()
                    //   AppAuth.getInstance().removeAuth()
                    //       findNavController().navigate(R.id.feedFragment)
                }
            }
            .setNegativeButton(textNegButton) { _, _ ->
                dialog?.cancel()
                if (flagSignIn) {
                    childFragmentManager
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, FragmentForBasket())
                        .commit()
                    //             findNavController().navigate(R.id.feedFragment)
                }
            }
            .create()
    }
}