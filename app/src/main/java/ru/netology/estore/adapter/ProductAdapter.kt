package ru.netology.estore.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.estore.R
import ru.netology.estore.databinding.ItemProductBinding
import ru.netology.estore.dto.Product
import ru.netology.estore.dto.getSumWithTwoDecimal
import androidx.recyclerview.widget.ListAdapter

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductHolder>() {


    //class ProductAdapter  : ListAdapter<Product, ProductHolder>(PostDiffCallback())

    var productList = ArrayList<Product>()

    class ProductHolder(item: View) : RecyclerView.ViewHolder(item) {
        val binding = ItemProductBinding.bind(item)

        fun bind(product: Product) = with(binding) {
            hit.isVisible = product.isHit

            if(product.isAction) {
                groupDiscount.isVisible = product.isAction
                textDiscount.text = "-${product.minusPercent}%"
                Price.alpha = 0.3f
                newPrice.text = "${getSumWithTwoDecimal(product.price*(100-product.minusPercent)/100).toString()} руб за 1 ${product.unitWeight}"

            } else {
                Price.alpha = 1f
                groupDiscount.visibility = View.GONE
            }

            if(product.isFavorite) {
                buttonLike.setImageResource(R.drawable.like_24)
            } else {
                buttonLike.setImageResource((R.drawable.un_like_24))
            }

            avatar.setImageResource(product.picture)

            txItem.text = product.name

            Price.text = "${product.price.toString()} руб за 1 ${product.unitWeight}"

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductHolder(view)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        //holder.bind(MainActivity.Service.cellArray[position])
        //Log.d("MyLog", "position=$position text=${cellList[position].text}")
        holder.bind(productList[position])

    }

    override fun getItemCount(): Int {
        return productList.size
    }


    fun addProduct(product: Product) {
        productList.add(product)
        notifyDataSetChanged()
    }

    fun changeProduct(colorBack:String = "", colorText:String = "", text:String = "", position: Int) {
//        if(!colorBack.isNullOrEmpty()) cellList[position].backColor = colorBack
//        if(!text.isNullOrEmpty()) cellList[position].text = text
//        if(!colorText.isNullOrEmpty()) cellList[position].textColor = colorText
        notifyDataSetChanged()
    }

    fun addAllProduct(list: List<Product>) {
        productList.clear()
        productList.addAll(list)
//        for (index in 0..120) {
//            Log.d("MyLog", "indexH=$index textCELLIST=${cellList[index].text}")
//            Log.d("MyLog", "indexH=$index textLIST=${list[index].text}")
//        }
        //notifyDataSetChanged()
    }
}
class PostDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}