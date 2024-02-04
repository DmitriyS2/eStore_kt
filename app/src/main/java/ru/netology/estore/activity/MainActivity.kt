package ru.netology.estore.activity

import android.content.res.Configuration
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
import androidx.core.view.forEach
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.netology.estore.R
import ru.netology.estore.databinding.ActivityMainBinding
import ru.netology.estore.model.FullProduct
import ru.netology.estore.viewmodel.AuthViewModel
import ru.netology.estore.viewmodel.MainViewModel
import ru.netology.estore.viewmodel.OrderViewModel
import ru.netology.estore.viewmodel.TopTextViewModel
import java.util.Locale

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

        showCountDrawerItem(counterHit, viewModel.counterHit)
        showCountDrawerItem(counterDiscount, viewModel.counterDiscount)

        binding.apply {
            nvMenu.setNavigationItemSelectedListener {
                if (viewModel.pointBottomMenu.value != -2) {
                    viewModel.pointBottomMenu.value = 0
                    when (it.itemId) {
                        R.id.allProducts -> {
                            goToFragment(getString(R.string.whole_range))
                        }
                        R.id.fruit -> {
                            goToFragment(getString(R.string.Fruits))
                        }
                        R.id.vegetable -> {
                            goToFragment(getString(R.string.Vegetables))
                        }
                        R.id.bakery -> {
                            goToFragment(getString(R.string.Bakery))
                        }
                        R.id.hit -> {
                            goToFragment(getString(R.string.Bestsellers))
                        }
                        R.id.discount -> {
                            goToFragment(getString(R.string.Discount))
                        }
                        R.id.favorite -> {
                            goToFragment(getString(R.string.Favorite))
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
                        topTextViewModel.text.value = getString(R.string.Basket)
                        viewModel.pointBottomMenu.value = 1
                        findNavController(R.id.nav_host_fragment)
                            .navigate(R.id.fragmentForBasket)
                    }
                    R.id.orderFragment -> {
                        viewModel.deleteFromBasketWeightZero()
                        if (authViewModel.authenticated) {
                            if (viewModel.dataFull.value?.emptyBasket == false) {
                                topTextViewModel.text.value = getString(R.string.order)
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
                                topTextViewModel.text.value = getString(R.string.order)
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
            showCountDrawerItem(counterFavorite, if (it == "0") "" else it)
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
                            topTextViewModel.text.value = getString(R.string.sign_in)
                            findNavController(R.id.nav_host_fragment)
                                .navigate(R.id.signInFragment)
                            true
                        }
                        R.id.signup -> {
                            topTextViewModel.text.value = getString(R.string.sign_up)
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
                            topTextViewModel.text.value = getString(R.string.history_of_orders)
                            viewModel.pointBottomMenu.value = -1
                            findNavController(R.id.nav_host_fragment)
                                .navigate(R.id.fragmentHistory)
                            true
                        }
                        R.id.language -> {
                            dialogLanguage()
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

        viewModel.language.observe(this) {
            it?.let {lang ->
                changeLanguage(lang)
            }
        }
    }


    private fun turnOnOffPointBottomMenuCheckable(flag: Boolean) {
        binding.bottomMenu.menu.forEach {
            it.isCheckable = flag
        }
    }

    private fun enabledPointBottomMenu(flag: Boolean) {
        binding.bottomMenu.menu.forEach {
            it.isEnabled = flag
        }
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
            title = getString(R.string.signout),
            text = getString(R.string.are_u_sure_signout),
            icon = R.drawable.warning_24,
            textPosButton = getString(R.string.log_out),
            textNegButton = getString(R.string.stay_in_system),
            flagSignIn = false,
            flagOrder = false,
            flagSignOut = true,
            navigateTo = 0
        )
        val manager = supportFragmentManager
        menuDialog.show(manager, "Sign out")
    }

    private fun mustSignIn(number: Int) {
        val menuDialog = SignInOutDialogFragment(
            title = getString(R.string.need_registration),
            text = getString(R.string.need_yo_signin),
            icon = R.drawable.info_24,
            textPosButton = getString(R.string.sign_in),
            textNegButton = getString(R.string.later),
            flagSignIn = true,
            flagOrder = false,
            navigateTo = number
        )
        val manager = supportFragmentManager
        menuDialog.show(manager, "Sign in")
    }

    private fun dialogLanguage() {
        val menuDialog = SignInOutDialogFragment(
            title = getString(R.string.language),
            text = getString(R.string.choose_language),
            icon = R.drawable.info_24,
            textPosButton = getString(R.string.rus),
            textNegButton = getString(R.string.engl),
            flagSignIn = false,
            flagOrder = false,
            navigateTo = 0
        )
        val manager = supportFragmentManager
        menuDialog.show(manager, "Language")
    }

    private fun showCountDrawerItem(view:TextView?, text:String) {
        view?.gravity = Gravity.CENTER_VERTICAL
        view?.setTypeface(null, Typeface.BOLD)
        view?.setTextColor(resources.getColor(R.color.red, null))
        view?.text = text
    }

    private fun changeLanguage(lang:String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val configuration = Configuration()
        configuration.locale = locale
        baseContext.resources.updateConfiguration(configuration, null)
        topTextViewModel.text.value = getString(R.string.app_name)
        viewModel.language.value = null
        this.recreate()
    }
}