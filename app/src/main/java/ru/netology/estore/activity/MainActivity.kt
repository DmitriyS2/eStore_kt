package ru.netology.estore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.estore.R
import ru.netology.estore.databinding.ActivityMainBinding
import ru.netology.estore.dto.Data
import ru.netology.estore.model.FullProduct
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.TopTextViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val topTextViewModel:TopTextViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findNavController(R.id.nav_host_fragment)
            .navigate(R.id.blankFragment)

        binding.apply {
            nvMenu.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.allProducts -> {
                        goToFragment(Data.allGroup)
                    }
                    R.id.fruit -> {
                        goToFragment(Data.fruitGroup)
                    }
                    R.id.vegetable -> {
                        goToFragment(Data.vegetableGroup)
                    }
                    R.id.bakery -> {
                        goToFragment(Data.bakeryGroup)
                    }
                    R.id.hit -> {
                        goToFragment(Data.hitGroup, R.color.blue)
                    }
                    R.id.discount -> {
                        goToFragment(Data.discountGroup, R.color.red)
                    }
                    R.id.favorite -> {
                        goToFragment(Data.favoriteGroup)
                    }
                }

                drawer.closeDrawer(GravityCompat.START)
                true
            }

            bottomMenu.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.catalog -> {

                        viewModel.dataFull.value?.statusBasket = false
                        viewModel.dataFull.value?.statusCatalog = true
                        drawer.openDrawer(GravityCompat.START)
                    }

                    R.id.basket -> {
                      //  printTxCategory(Data.basketGroup)
                        topTextViewModel.text.value = Data.basketGroup
                        viewModel.dataFull.value?.statusBasket = true
                        viewModel.dataFull.value?.statusCatalog = false
                        findNavController(R.id.nav_host_fragment)
                            .navigate(R.id.fragmentForBasket)
//                        supportFragmentManager.beginTransaction()
//                            .replace(R.id.nav_host_fragment, FragmentForBasket())
//                            .commit()
                    }
                }
                true
            }
        }

        topTextViewModel.text.observe(this){
            binding.txCategory.text = it
        }

        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.main_menu)
                menu.setGroupVisible(R.id.unauthenticated, !authViewModel.authenticated)
                menu.setGroupVisible(R.id.authenticated, authViewModel.authenticated)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.signin -> {
                            topTextViewModel.text.value = Data.signInGroup
                            findNavController(R.id.nav_host_fragment)
                                .navigate(R.id.signInFragment)
//                            supportFragmentManager.beginTransaction()
//                                .replace(R.id.nav_host_fragment, SignInFragment())
//                                .commit()
                            true
                        }
                            R.id.signup -> {
                                topTextViewModel.text.value = Data.signUpGroup
//                                supportFragmentManager.beginTransaction()
//                                    .replace(R.id.nav_host_fragment, SignUpFragment())
//                                    .commit()
                        findNavController(R.id.nav_host_fragment)
                                   .navigate(R.id.signUpFragment)


                                //  AppAuth.getInstance().setAuth(5, "x-token")
                                true
                            }
                        R.id.signout -> {
                            areYouSureSignOut()
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }

//        addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.main_menu, menu)
//
//                menu.let {
//                    it.setGroupVisible(R.id.unauthenticated, !authViewModel.authenticated)
//                    it.setGroupVisible(R.id.authenticated, authViewModel.authenticated)
//                }
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
//                when (menuItem.itemId) {
//                    R.id.signin -> {
//                        supportFragmentManager
//                            .beginTransaction()
//                            .replace(R.id.nav_host_fragment, SignInFragment())
//                            .commit()
////                        findNavController(R.id.nav_host_fragment)
////                            .navigate(R.id.authenticationFragment)
//                        //   AppAuth.getInstance().setAuth(5, "x-token")
//                        true
//                    }
//
//                    R.id.signup -> {
////                        findNavController(R.id.nav_host_fragment)
////                            //       .navigate(R.id.signUpFragment)
////                            .navigate(R.id.signUpWithPhotoFragment)
//
//                        //  AppAuth.getInstance().setAuth(5, "x-token")
//                        true
//                    }
//
//                    R.id.signout -> {
//                        areYouSureSignOut()
//                        true
//                    }
//
//                    else -> false
//                }
//        })
    }

//    fun printTxCategory(text:String, color: Int = R.color.white) {
//        binding.txCategory.setTextColor(getColor(color))
//        topTextViewModel.text.value = text
//    }

    fun goToFragment(status:String, color: Int = R.color.white) {
    //    printTxCategory(status, color)
        topTextViewModel.text.value = status
        viewModel.dataFull.value = FullProduct(products = viewModel.deleteFromBasketWeightZeroFromRepo(),
            status = status, statusCatalog = true, statusBasket = false)

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.nav_host_fragment, FragmentForCatalog())
//            .commit()
        findNavController(R.id.nav_host_fragment)
            .navigate(R.id.fragmentForCatalog)
    }
    fun areYouSureSignOut() {
        val menuDialog = SignInOutDialogFragment("Выход из аккаунта","Вы уверены, что хотите выйти из системы?", R.drawable.warning_24, "Выйти", "Остаться", false)
        val manager = supportFragmentManager
        menuDialog.show(manager, "Sign out")
    }
}