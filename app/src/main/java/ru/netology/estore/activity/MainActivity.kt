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

//var idProduct = 0

//val allProduct = Data.fillAllProducts()

class MainActivity : AppCompatActivity() {

    private val model: MainViewModel by viewModels()

//    val adapterFruit = ProductAdapter()
//    val adapterVegetable = ProductAdapter()
//    val adapterBakery = ProductAdapter()
//    val adapterAllProducts = ProductAdapter()

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val allProducts = model.data.value.orEmpty() as ArrayList
//        val fruitProduct = model.data.value
//            ?.filter { it.group == Data.fruitGroup }.orEmpty() as ArrayList<Product>
//        val vegetableProduct = model.data.value
//            ?.filter { it.group == Data.vegetableGroup }.orEmpty() as ArrayList
//        val bakeryProduct = model.data.value
//            ?.filter { it.group == Data.bakeryGroup }.orEmpty() as ArrayList
//        val hitProduct = model.data.value
//            ?.filter { it.isHit }.orEmpty() as ArrayList
//        val discountProduct = model.data.value
//            ?.filter { it.isAction }.orEmpty() as ArrayList
//        val favoriteProduct = model.data.value
//            ?.filter { it.isFavorite }.orEmpty() as ArrayList

        binding.apply {
            nvMenu.setNavigationItemSelectedListener { it ->
                when (it.itemId) {
                    R.id.allProducts -> {
                        goToFragment(R.color.black, Data.allGroup)

                    }

                    R.id.fruit -> {
                        goToFragment(R.color.colorFruit, Data.fruitGroup)

                    }

                    R.id.vegetable -> {
                        goToFragment(R.color.colorVegetable, Data.vegetableGroup)
                    }

                    R.id.bakery -> {
                        goToFragment(R.color.colorBakery, Data.bakeryGroup)
                    }

                    R.id.hit -> {
                        goToFragment(R.color.blue, Data.hitGroup)
                    }

                    R.id.discount -> {
                        goToFragment(R.color.red, Data.discountGroup)
                    }

                    R.id.favorite -> {
                        goToFragment(R.color.black, Data.favoriteGroup)
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

    fun goToFragment(color: Int, status: String) {
        printTxCategory(color, status)

        model.dataFull.value?.status = status

        //model.data.value = arrayList
        supportFragmentManager.beginTransaction()
            .replace(R.id.placeHolder, FragmentForCatalog())
            .commit()
    }
}