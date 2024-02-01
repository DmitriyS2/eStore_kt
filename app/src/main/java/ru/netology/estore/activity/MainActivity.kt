package ru.netology.estore.activity

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MenuItemCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
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
    private val orderViewModel: OrderViewModel by viewModels()

    lateinit var binding: ActivityMainBinding

    private var counterHit: TextView? = null
    private var counterDiscount: TextView? = null
    private var counterFavorite: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findNavController(R.id.nav_host_fragment)
            .navigate(R.id.blankFragment)

        counterHit = MenuItemCompat.getActionView(
            binding.nvMenu.menu.findItem(R.id.hit)
        ) as TextView
        counterDiscount = MenuItemCompat.getActionView(
            binding.nvMenu.menu.findItem(R.id.discount)
        ) as TextView
        counterFavorite = MenuItemCompat.getActionView(
            binding.nvMenu.menu.findItem(R.id.favorite)
        ) as TextView
        initializeCountDrawerHitDiscount()

        binding.apply {
            nvMenu.setNavigationItemSelectedListener {
                if (viewModel.pointBottomMenu.value != -2) {
                    viewModel.pointBottomMenu.value = 0
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
                }
                drawer.closeDrawer(GravityCompat.START)
                true
            }



            bottomMenu.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.fragmentForCatalog -> {
                        viewModel.pointBottomMenu.value = 0
                        drawer.openDrawer(GravityCompat.START)
                    }

                    R.id.fragmentForBasket -> {
                        topTextViewModel.text.value = Data.basketGroup
                        viewModel.pointBottomMenu.value = 1
                        findNavController(R.id.nav_host_fragment)
                            .navigate(R.id.fragmentForBasket)
                    }

                    R.id.orderFragment -> {
                        viewModel.deleteFromBasketWeightZero()

                        if (authViewModel.authenticated) {
                            if (viewModel.dataFull.value?.emptyBasket == false) {
                                topTextViewModel.text.value = Data.orderGroup
                                val list =
                                    viewModel.dataFull.value?.products?.filter { it.inBasket }
                                        .orEmpty()
                                viewModel.amountOrder.value = viewModel.countOrder(list)

                                if (orderViewModel.showPoint2.value != 0) {
                                    orderViewModel.showPoint1.value = 2
                                } else {
                                    orderViewModel.showPoint1.value = 1
                                }
                                viewModel.pointBottomMenu.value = 1
                                findNavController(R.id.nav_host_fragment)
                                    .navigate(R.id.orderFragment)
                            } else {
                                topTextViewModel.text.value = Data.orderGroup
                                viewModel.pointBottomMenu.value = 1
                                findNavController(R.id.nav_host_fragment)
                                    .navigate(R.id.orderFragment)
                            }
                        } else {
                            mustSignIn(if (viewModel.pointBottomMenu.value == 0) R.id.fragmentForCatalog else R.id.fragmentForBasket)
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

        viewModel.counterFavorite.observe(this) {
            initializeCountDrawerFavorite(if (it == "0") "" else it)
        }

        binding.menu.setOnClickListener {
            if (viewModel.pointBottomMenu.value == -2) return@setOnClickListener
            PopupMenu(it.context, it).apply {
                inflate(R.menu.main_menu)
                menu.setGroupVisible(R.id.unauthenticated, !authViewModel.authenticated)
                menu.setGroupVisible(R.id.authenticated, authViewModel.authenticated)
                setOnMenuItemClickListener { item ->
                    viewModel.pointBottomMenu.value = -1
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
                            viewModel.getHistory(authViewModel.data.value.username)
                            viewModel.pointBottomMenu.value = -1
                            findNavController(R.id.nav_host_fragment)
                                .navigate(R.id.fragmentHistory)
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
                    viewModel.getHistory(authViewModel.data.value.username)
                    Log.d("MyLog", "MainActivity data.collectLatest, login=${it.username}")
                    //     invalidateMenu()
                }
            }
        }

        viewModel.pointBottomMenu.observe(this) {
            when (it) {
                -2 -> enabledPointBottomMenu(false) //заблокировать bottom_menu, main_menu и drawer_menu
                -1 -> {
                    enabledPointBottomMenu(true)
                    turnOnOffPointBottomMenuCheckable(false) //сбросить все нажатые кнопки bottom menu
                }

                in 0..2 -> {
                    enabledPointBottomMenu(true)
                    turnOnOffPointBottomMenuCheckable(true)
                    binding.bottomMenu.menu.getItem(it).isChecked = true
                }
            }
        }
    }

    private fun turnOnOffPointBottomMenuCheckable(flag: Boolean) {
        binding.bottomMenu.menu.getItem(0).isCheckable = flag
        binding.bottomMenu.menu.getItem(1).isCheckable = flag
        binding.bottomMenu.menu.getItem(2).isCheckable = flag
    }

    private fun enabledPointBottomMenu(flag: Boolean) {
        binding.bottomMenu.menu.getItem(0).isEnabled = flag
        binding.bottomMenu.menu.getItem(1).isEnabled = flag
        binding.bottomMenu.menu.getItem(2).isEnabled = flag
    }

    private fun goToFragment(status: String) {
        topTextViewModel.text.value = status
        viewModel.dataFull.value = FullProduct(
            products = viewModel.deleteFromBasketWeightZeroFromRepo(),
            status = status,
        )
        findNavController(R.id.nav_host_fragment)
            .navigate(R.id.fragmentForCatalog)
    }

    private fun areYouSureSignOut() {
        val menuDialog = SignInOutDialogFragment(
            title = "Выход из аккаунта",
            text = "Вы уверены, что хотите выйти из системы?",
            icon = R.drawable.warning_24,
            textPosButton = "Выйти",
            textNegButton = "Остаться",
            flagSignIn = false,
            flagOrder = false,
            navigateTo = 0
        )
        val manager = supportFragmentManager
        menuDialog.show(manager, "Sign out")
    }

    private fun mustSignIn(number: Int) {
        val menuDialog = SignInOutDialogFragment(
            title = "Нужна регистрация",
            text = "Для этого действия необходимо войти в систему",
            icon = R.drawable.info_24,
            textPosButton = "Sign In",
            textNegButton = "Позже",
            flagSignIn = true,
            flagOrder = false,
            navigateTo = number
        )
        val manager = supportFragmentManager
        menuDialog.show(manager, "Sign in")
    }

    private fun initializeCountDrawerHitDiscount() {
        counterHit?.gravity = Gravity.CENTER_VERTICAL
        counterHit?.setTypeface(null, Typeface.BOLD)
        counterHit?.setTextColor(resources.getColor(R.color.red))
        counterHit?.text = viewModel.counterHit
        counterDiscount?.setGravity(Gravity.CENTER_VERTICAL)
        counterDiscount?.setTypeface(null, Typeface.BOLD)
        counterDiscount?.setTextColor(resources.getColor(R.color.red))
        counterDiscount?.setText(viewModel.counterDiscount)
    }

    private fun initializeCountDrawerFavorite(favorite: String) {
        counterFavorite?.setGravity(Gravity.CENTER_VERTICAL)
        counterFavorite?.setTypeface(null, Typeface.BOLD)
        counterFavorite?.setTextColor(resources.getColor(R.color.red, null))
        counterFavorite?.setText(favorite)
    }
}