package ru.netology.estore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.estore.R
import ru.netology.estore.databinding.ActivityMainBinding
import ru.netology.estore.dto.Data
import ru.netology.estore.model.FullProduct
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.OrderViewModel
import ru.netology.estore.viewmodel.TopTextViewModel


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val topTextViewModel: TopTextViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    private val orderViewModel:OrderViewModel by viewModels()

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
                        goToFragment(Data.hitGroup)
                    }

                    R.id.discount -> {
                        goToFragment(Data.discountGroup)
                    }

                    R.id.favorite -> {
                        goToFragment(Data.favoriteGroup)
                    }
                }
                drawer.closeDrawer(GravityCompat.START)
                true
            }


            bottomMenu.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.catalog -> {
//                        viewModel.dataFull.value?.statusBasket = false
//                        viewModel.dataFull.value?.statusCatalog = true
                        drawer.openDrawer(GravityCompat.START)
                    }

                    R.id.basket -> {
                        topTextViewModel.text.value = Data.basketGroup
//                        viewModel.dataFull.value?.statusBasket = true
//                        viewModel.dataFull.value?.statusCatalog = false
                        findNavController(R.id.nav_host_fragment)
                            .navigate(R.id.fragmentForBasket)
                    }

                    R.id.order -> {
                        viewModel.deleteFromBasketWeightZero()

                        if(viewModel.dataFull.value?.emptyBasket == false) {
                            if (authViewModel.authenticated) {
                                topTextViewModel.text.value = Data.orderGroup

                                val list = viewModel.dataFull.value?.products?.filter { it.inBasket }.orEmpty()
                                viewModel.amountOrder.value = viewModel.countOrder(list)

                                if (orderViewModel.showPoint2.value != 0) {
                                    orderViewModel.showPoint1.value = 2
                                } else {
                                    orderViewModel.showPoint1.value = 1
                                }

                                findNavController(R.id.nav_host_fragment)
                                    .navigate(R.id.orderFragment)
                            } else {
                                mustSignIn()
                            }
                        } else {
                       //     binding.bottomMenu.selectedItemId = R.id.basket
                            findNavController(R.id.nav_host_fragment)
                                .navigate(R.id.fragmentForBasket)
                        }

                        Log.d("MyLog", "emptyBasket = ${viewModel.dataFull.value?.emptyBasket}")
                    }
                }
                true
            }
        }

        topTextViewModel.text.observe(this) {
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
                            true
                        }

                        R.id.signup -> {
                            topTextViewModel.text.value = Data.signUpGroup
                            findNavController(R.id.nav_host_fragment)
                                .navigate(R.id.signUpFragment)
                            true
                        }

                        R.id.signout -> {
                            areYouSureSignOut()
                            true
                        }

                        R.id.historyOfOrders -> {
                            viewModel.getHistoryOfOrders(authViewModel.data.value.login)
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                authViewModel.data.collectLatest {
                        viewModel.getHistoryOfOrders(authViewModel.data.value.login)
                        Log.d("MyLog", "MainActivity data.collectLatest, login=${it.login}")
               //     invalidateMenu()
                }
            }
        }
    }

//    fun printTxCategory(text:String, color: Int = R.color.white) {
//        binding.txCategory.setTextColor(getColor(color))
//        topTextViewModel.text.value = text
//    }

    fun goToFragment(status: String) {
        topTextViewModel.text.value = status
        viewModel.dataFull.value = FullProduct(
            products = viewModel.deleteFromBasketWeightZeroFromRepo(),
            status = status,
        )
        findNavController(R.id.nav_host_fragment)
            .navigate(R.id.fragmentForCatalog)
    }

    fun areYouSureSignOut() {
        val menuDialog = SignInOutDialogFragment(
            title = "Выход из аккаунта",
            text = "Вы уверены, что хотите выйти из системы?",
            icon = R.drawable.warning_24,
            textPosButton = "Выйти",
            textNegButton = "Остаться",
            flagSignIn = false,
            flagSignUp = false
        )
        val manager = supportFragmentManager
        menuDialog.show(manager, "Sign out")
    }

    private fun mustSignIn() {
        val menuDialog = SignInOutDialogFragment(
            title = "Нужна регистрация",
            text = "Для этого действия необходимо войти в систему",
            icon = R.drawable.info_24,
            textPosButton = "Sign In",
            textNegButton = "Позже",
            flagSignIn = true,
            flagSignUp = false
        )
        val manager = supportFragmentManager
        menuDialog.show(manager, "Sign in")
    }
}