package ru.netology.estore.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.estore.R
import ru.netology.estore.databinding.ItemForCatalogProductBinding
import ru.netology.estore.dto.Product
import ru.netology.estore.dto.getSumWithTwoDecimal
import androidx.recyclerview.widget.ListAdapter

interface Listener {
    fun like(product: Product)
    fun addToBasket(product: Product)
    fun addToBasketAgain(product: Product)
    fun deleteFromBasket(product: Product)
    fun weightPlus(product: Product)
    fun weightMinus(product: Product)
    fun deleteFromBasketWeightZero()
}

//class ProductAdapter (private val listener: Listener) : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {

    class ProductAdapter (private val listener:Listener) : ListAdapter<Product, ProductAdapter.ProductHolder>(PostDiffCallback()){

    //var productList = ArrayList<Product>()
    var productList = emptyList<Product>()

    class ProductHolder(item: View, val listener: Listener) : RecyclerView.ViewHolder(item) {
        val binding = ItemForCatalogProductBinding.bind(item)


        fun bind(product: Product) = with(binding) {
            if(product.isHit) {
                hit.visibility = View.VISIBLE
            } else {
                hit.visibility = View.INVISIBLE
            }


            if(product.isDiscount) {
                groupDiscount.visibility = View.VISIBLE
                textDiscount.text = "-${product.minusPercent}%"
                Price.alpha = 0.3f
                newPrice.text = "${getSumWithTwoDecimal(product.price*(100-product.minusPercent)/100, 100.0)} руб за 1 ${product.unitWeight}"

            } else {
                Price.alpha = 1f
                groupDiscount.visibility = View.INVISIBLE
            }

            if(product.isFavorite) {
                buttonLike.setImageResource(R.drawable.like_24)
            } else {
                buttonLike.setImageResource((R.drawable.un_like_24))
            }

            avatar.setImageResource(product.picture)

            txItem.text = product.name

            Price.text = "${product.price.toString()} руб за 1 ${product.unitWeight}"

            if(product.inBasket) {
                buttonAddToBin.setBackgroundColor(Color.parseColor("#104021"))
                buttonAddToBin.text = "Убрать"
            } else {
                buttonAddToBin.setBackgroundColor(Color.BLUE)
                buttonAddToBin.text = "Добавить"
            }

            buttonLike.setOnClickListener {
                listener.like(product)
            }

            buttonAddToBin.setOnClickListener {
                listener.addToBasket(product)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_for_catalog_product, parent, false)
        return ProductHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }


//    fun addProduct(product: Product) {
//        productList.add(product)
//        notifyDataSetChanged()
//    }

    fun changeProduct(colorBack:String = "", colorText:String = "", text:String = "", position: Int) {
//        if(!colorBack.isNullOrEmpty()) cellList[position].backColor = colorBack
//        if(!text.isNullOrEmpty()) cellList[position].text = text
//        if(!colorText.isNullOrEmpty()) cellList[position].textColor = colorText
        notifyDataSetChanged()
    }

//    fun addAllProduct(list: List<Product>) {
//        productList.clear()
//        productList.addAll(list)
//        for (index in 0..120) {
//            Log.d("MyLog", "indexH=$index textCELLIST=${cellList[index].text}")
//            Log.d("MyLog", "indexH=$index textLIST=${list[index].text}")
//        }
        //notifyDataSetChanged()
//    }
}

class PostDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}