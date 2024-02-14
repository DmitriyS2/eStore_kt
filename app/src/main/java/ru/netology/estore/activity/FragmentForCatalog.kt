package ru.netology.estore.activity

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ru.netology.estore.R
import ru.netology.estore.adapter.Listener
import ru.netology.estore.adapter.ProductAdapter
import ru.netology.estore.databinding.FragmentForCatalogBinding
import ru.netology.estore.dto.Product
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.TopTextViewModel


class FragmentForCatalog : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by activityViewModels()
    private val topTextViewModel: TopTextViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.shared_image)
            .setDuration(2000L)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentForCatalogBinding.inflate(inflater, container, false)

        val adapter = ProductAdapter(object : Listener {
            override fun like(product: Product) {
                if (authViewModel.authenticated) {
                    viewModel.like(product)
                    Log.d("MyLog", "priceN=${product.priceN}, 2*priceN=${product.priceN*product.oneUnitN}, " +
                            "oneUnit=${product.oneUnitN}, sum=${product.sumN}, weight=${product.weightN}")
                } else {
                    mustSignIn()
                }
            }

            override fun addToBasket(product: Product) {
                viewModel.addToBasket(product)
            }

            override fun addToBasketAgain(product: Product) {
            }

            override fun deleteFromBasket(product: Product) {
            }

            override fun weightPlus(product: Product) {
            }

            override fun weightMinus(product: Product) {
            }

            override fun deleteFromBasketWeightZero() {
            }

            override fun goToProduct(view: View, product: Product) {
                val action: NavDirections =
                    FragmentForCatalogDirections.actionFragmentForCatalogToFragmentCurrentProduct(
                        transitName = ViewCompat.getTransitionName(view)!!
                    )

                val extras = FragmentNavigator.Extras.Builder()
                    .addSharedElement(view, ViewCompat.getTransitionName(view)!!)
                    .build()

                findNavController().navigate(action, extras)

                topTextViewModel.text.value = ""
                viewModel.pointBottomMenu.value = -1
            }
        })

        binding.rwProducts.layoutManager = GridLayoutManager(activity, 2)
        binding.rwProducts.adapter = adapter

        viewModel.dataFull.observe(viewLifecycleOwner) { full ->
            val list = when (full.status) {
                getString(R.string.whole_range) -> viewModel.dataFull.value?.products

                getString(R.string.Fruits) -> viewModel.dataFull.value?.products?.filter { it.group == viewModel.dataLanguage.fruitGroup }
                    .orEmpty()

                getString(R.string.Vegetables) -> viewModel.dataFull.value?.products?.filter { it.group == viewModel.dataLanguage.vegetableGroup }
                    .orEmpty()

                getString(R.string.Bakery) -> viewModel.dataFull.value?.products?.filter { it.group == viewModel.dataLanguage.bakeryGroup }
                    .orEmpty()

                getString(R.string.Bestsellers) -> viewModel.dataFull.value?.products?.filter { it.isHit }
                    .orEmpty()

                getString(R.string.Discount) -> viewModel.dataFull.value?.products?.filter { it.isDiscount }
                    .orEmpty()

                getString(R.string.Favorite) -> viewModel.dataFull.value?.products?.filter { it.isFavorite }
                    .orEmpty()

                else -> emptyList()
            }
            adapter.submitList(list)
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentForCatalog()
    }

    private fun mustSignIn() {
        val menuDialog = SignInOutDialogFragment(
            title = getString(R.string.need_registration),
            text = getString(R.string.need_yo_signin),
            icon = R.drawable.info_24,
            textPosButton = getString(R.string.sign_in),
            textNegButton = getString(R.string.later),
            flagSignIn = true,
            flagOrder = false,
            navigateTo = R.id.fragmentForCatalog
        )
        val manager = childFragmentManager
        menuDialog.show(manager, "Sign in")
    }
}