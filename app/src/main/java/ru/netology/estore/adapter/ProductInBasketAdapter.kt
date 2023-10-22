package ru.netology.estore.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.estore.R
import ru.netology.estore.databinding.ItemForBasketProductBinding
import ru.netology.estore.dto.Product
import ru.netology.estore.dto.getSumWithTwoDecimal


class ProductInBasketAdapter (private val listener:Listener) : ListAdapter<Product, ProductInBasketAdapter.ProductInBasketHolder>(PostDiffCallback2()){

    //var productList = ArrayList<Product>()
    var productList = emptyList<Product>()

    class ProductInBasketHolder(item: View, val listener: Listener) : RecyclerView.ViewHolder(item) {
        val binding = ItemForBasketProductBinding.bind(item)


        @SuppressLint("ResourceAsColor")
        fun bind(product: Product) = with(binding) {
           avatar.setImageResource(product.picture)
            txItem.text = product.name

            if(product.isFavorite) {
                buttonLike.setImageResource(R.drawable.like_24)
            } else {
                buttonLike.setImageResource((R.drawable.un_like_24))
            }

            if(product.isDiscount) {
                txMinusPrice.visibility = View.VISIBLE
                txMinusPrice.text = "-${product.minusPercent}%"
                txPrice.setTextColor(R.color.red)
                txPrice.text = "${getSumWithTwoDecimal(product.price*(100-product.minusPercent)/100, 100.0).toString()} руб за 1 ${product.unitWeight}"
            } else {
                txMinusPrice.visibility = View.GONE
                txPrice.setTextColor(R.color.black)
                txPrice.text = "${product.price.toString()} руб за 1 ${product.unitWeight}"
            }

            if(product.weight>product.oneUnit) {
                groupWeight.visibility = View.VISIBLE
                buttonAdd.visibility = View.GONE
                buttonMinus.setImageResource(R.drawable.button_minus_24)
            } else if(product.weight==product.oneUnit) {
                groupWeight.visibility = View.VISIBLE
                buttonAdd.visibility = View.GONE
                buttonMinus.setImageResource(R.drawable.delete_green_24)
            } else if(product.weight==0.0) {
                groupWeight.visibility = View.GONE
                buttonAdd.visibility = View.VISIBLE
            }

            txSumma.text = "${getSumWithTwoDecimal(product.sum, 100.0)} руб"

            txWeight.text = "${getSumWithTwoDecimal(product.weight, 10.0)} ${product.unitWeight}"

            buttonLike.setOnClickListener {
                listener.like(product)
            }

            buttonDelete.setOnClickListener {
                listener.deleteFromBasket(product)
            }

            buttonPlus.setOnClickListener {
                listener.weightPlus(product)
            }
            buttonMinus.setOnClickListener {
                listener.weightMinus(product)
            }
            buttonAdd.setOnClickListener {
                listener.addToBasketAgain(product)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductInBasketHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_for_basket_product, parent, false)
        return ProductInBasketHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ProductInBasketHolder, position: Int) {
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
//        notifyDataSetChanged()
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

class PostDiffCallback2 : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }
}