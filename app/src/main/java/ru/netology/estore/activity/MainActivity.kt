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
import ru.netology.estore.dto.Product
import ru.netology.estore.viewmodel.MainViewModel
import java.util.Locale.filter
import kotlin.math.roundToInt

var idProduct = 0

val adapter = ProductAdapter()

val allProduct = Data.fillAllProducts()

class MainActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModels()

    val adapterFruit = ProductAdapter()
    val adapterVegetable = ProductAdapter()
    val adapterBakery = ProductAdapter()
    val adapterAllProducts = ProductAdapter()

    val fruitProduct = allProduct
        .filter { it.group == Data.fruitGroup } as ArrayList
    val vegetableProduct = allProduct
        .filter { it.group == Data.vegetableGroup } as ArrayList
    val bakeryProduct = allProduct
        .filter { it.group == Data.bakeryGroup } as ArrayList
    val hitProduct = allProduct
        .filter { it.isHit } as ArrayList
    val discountProduct = allProduct
        .filter { it.isAction } as ArrayList
    val favoriteProduct = allProduct
        .filter { it.isFavorite } as ArrayList


    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            nvMenu.setNavigationItemSelectedListener { it ->
                when (it.itemId) {
                    R.id.allProducts -> {
                        goToFragment(R.color.black, "Весь ассортимент", allProduct)
//                        adapter.productList = allProduct
//                        model.data.value = allProduct
//                        supportFragmentManager.beginTransaction()
//                            .replace(R.id.placeHolder, FragmentForCatalog())
//                            .commit()
                    }

                    R.id.fruit -> {
                        goToFragment(R.color.colorFruit, Data.fruitGroup, fruitProduct)

//                        adapter.productList = fruitProduct
//                        model.data.value = fruitProduct
//                        supportFragmentManager.beginTransaction()
//                            .replace(R.id.placeHolder, FragmentForCatalog())
//                            .commit()
                    }

                    R.id.vegetable -> {
                        goToFragment(R.color.colorVegetable, Data.vegetableGroup, vegetableProduct)
                    }

                    R.id.bakery -> {
                        goToFragment(R.color.colorBakery, it.title.toString(), bakeryProduct)//Data.bakeryGroup)
                    }

                    R.id.hit -> {
                        goToFragment(R.color.blue, "Хиты продаж!", hitProduct)
                    }

                    R.id.discount -> {
                        goToFragment(R.color.red, "АКЦИЯ!!!", discountProduct)
                    }

                    R.id.favorite -> {
                        goToFragment(R.color.black, "Избранное", favoriteProduct)
                    }
                }

                drawer.closeDrawer(GravityCompat.START)
                true
            }

            bottomMenu.setOnItemSelectedListener {
                when (it.itemId) {
                    R.id.catalog -> {
                        drawer.openDrawer(GravityCompat.START)
                    }

                    R.id.basket -> {

                    }
                }
                true
            }
        }

    }

    fun printTxCategory(color: Int, text: String) {
        binding.txCategory.setTextColor(getColor(color))
        binding.txCategory.text = text
    }

    fun goToFragment(color: Int, text: String, arrayList: ArrayList<Product>) {
        printTxCategory(color, text)

        adapter.productList = arrayList
        model.data.value = arrayList
        supportFragmentManager.beginTransaction()
            .replace(R.id.placeHolder, FragmentForCatalog())
            .commit()
    }
}