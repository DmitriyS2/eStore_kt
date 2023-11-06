package ru.netology.estore.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import ru.netology.estore.R
import ru.netology.estore.adapter.ProductAdapter
import ru.netology.estore.databinding.ActivityMainBinding
import ru.netology.estore.dto.Data
import ru.netology.estore.model.FullProduct
import ru.netology.estore.viewmodel.MainViewModel



class MainActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModels()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

                        model.dataFull.value?.statusBasket = false
                        model.dataFull.value?.statusCatalog = true
                        drawer.openDrawer(GravityCompat.START)
                    }

                    R.id.basket -> {
                        printTxCategory(Data.basketGroup)
                        model.dataFull.value?.statusBasket = true
                        model.dataFull.value?.statusCatalog = false
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.placeHolder, FragmentForBasket())
                            .commit()
                    }
                }
                true
            }
        }
    }

    fun printTxCategory(text:String, color: Int = R.color.white) {
        binding.txCategory.setTextColor(getColor(color))
        binding.txCategory.text = text
    }

    fun goToFragment(status:String, color: Int = R.color.white) {
        printTxCategory(status, color)

        model.dataFull.value = FullProduct(products = Data.deleteFromBasketWeightZero(), status = status, statusCatalog = true, statusBasket = false)

        supportFragmentManager.beginTransaction()
            .replace(R.id.placeHolder, FragmentForCatalog())
            .commit()
    }
}