package ru.netology.estore.activity

import android.os.Bundle
import android.text.TextUtils.replace
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.netology.estore.R
import ru.netology.estore.activity.FragmentCurrentProduct.Companion.textArgument
import ru.netology.estore.adapter.Listener
import ru.netology.estore.adapter.ProductAdapter
import ru.netology.estore.databinding.ActivityMainBinding
import ru.netology.estore.databinding.FragmentForCatalogBinding
import ru.netology.estore.dto.Product
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.TopTextViewModel


class FragmentForCatalog : Fragment() {
 //   private var fragmentBinding: FragmentForCatalogBinding? = null

    lateinit var binding:FragmentForCatalogBinding

    private val viewModel: MainViewModel by activityViewModels()
    private val authViewModel:AuthViewModel by activityViewModels()
    private val topTextViewModel:TopTextViewModel by activityViewModels()



//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val inflater = TransitionInflater.from(requireContext())
//        exitTransition = inflater.inflateTransition(R.transition.fade)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    //     binding = FragmentForCatalogBinding.inflate(layoutInflater)
       // setContentView(binding.root)
   //     fragmentBinding = binding
     //   val itemImageView = view.findViewById<CardView>(R.id.cardViewItemCatalog)
    //    ViewCompat.setTransitionName(itemImageView, "item_image")
//        val itemImageView = view.findViewById<RecyclerView>(R.id.rwProducts)
//        ViewCompat.setTransitionName(itemImageView, "item_image")
//
//        itemImageView.setOnClickListener {
//            val fragment = FragmentCurrentProduct()
//            parentFragmentManager.commit {
//                //   setCustomAnimations(...)
//                setReorderingAllowed(true)
//                addSharedElement(itemImageView, "hero_image")
//                replace(R.id.nav_host_fragment, fragment)
//                addToBackStack(null)
//            }
//        }

//        val fragment = FragmentCurrentProduct()
//        childFragmentManager.commit {
//            //  setCustomAnimations(...)
//            view?.let { addSharedElement(it, "hero_image") }
//            replace(R.id.nav_host_fragment, fragment)
//            addToBackStack(null)
//        }
//        val itemImageView = view.findViewById<ImageView>(R.id.item_image)
//        ViewCompat.setTransitionName(itemImageView, “item_image”)

//        postponeEnterTransition()
//
//        // Wait for the data to load
//        viewModel.data.observe(viewLifecycleOwner) {
//            // Set the data on the RecyclerView adapter
//            adapter.setData(it)
//            // Start the transition once all views have been
//            // measured and laid out
//            (view.parent as? ViewGroup)?.doOnPreDraw {
//                startPostponedEnterTransition()
//            }
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

       binding = FragmentForCatalogBinding.inflate(inflater, container, false)

        val adapter = ProductAdapter(object : Listener {
            override fun like(product: Product) {
                if(authViewModel.authenticated) {
                    viewModel.like(product)
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

            //      override fun goToProduct(product: Product) {
            override fun goToProduct(view: View, product: Product) {
            //    val itemImageView = view.findViewById<View>(R.id.rwProducts)
            //    ViewCompat.setTransitionName(view, product.id.toString())
                val fragment = FragmentCurrentProduct.newInstance()
           //     val frag = findNavController().navigate(R.id.fragmentCurrentProduct)
              //  val action = R.id.action_fragmentForCatalog_to_fragmentCurrentProduct

//                val extras = FragmentNavigator.Extras.Builder()
//                    .addSharedElement(view, ViewCompat.getTransitionName(view)!!)
//                    .build()
//                findNavController()
////                    .navigate(R.id.fragmentCurrentProduct, Bundle().apply {
////                            textArgument = "hero_image"
////                        })
//                    .navigate(R.id.fragmentCurrentProduct, extras)

//                parentFragmentManager.commit {
//                    //   setCustomAnimations(...)
//                    setReorderingAllowed(true)
//                    addSharedElement(view, "hero_image")
//                    replace(R.id.nav_host_fragment, fragment)
//                    addToBackStack(null)
//                }

                findNavController()
                    .navigate(R.id.fragmentCurrentProduct,
                        Bundle().apply {
                            textArgument = product.id.toString()
                        })
                topTextViewModel.text.value = product.name
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

                getString(R.string.Bestsellers) -> viewModel.dataFull.value?.products?.filter { it.isHit }.orEmpty()

                getString(R.string.Discount) -> viewModel.dataFull.value?.products?.filter { it.isDiscount }
                    .orEmpty()

                getString(R.string.Favorite) -> viewModel.dataFull.value?.products?.filter { it.isFavorite }
                    .orEmpty()

                else -> emptyList()
            }
            adapter.productList = list.orEmpty()
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