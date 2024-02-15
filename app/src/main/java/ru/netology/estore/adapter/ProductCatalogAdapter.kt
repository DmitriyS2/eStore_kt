package ru.netology.estore.adapter

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.netology.estore.R
import ru.netology.estore.databinding.ItemForCatalogProductBinding
import ru.netology.estore.dto.Product
import androidx.recyclerview.widget.ListAdapter
import java.math.BigDecimal
import java.math.RoundingMode

interface Listener {
    fun like(product: Product)
    fun addToBasket(product: Product)
    fun addToBasketAgain(product: Product)
    fun deleteFromBasket(product: Product)
    fun weightPlus(product: Product)
    fun weightMinus(product: Product)
    fun deleteFromBasketWeightZero()
    fun goToProduct(view: View, product: Product)
}

class ProductAdapter(private val listener: Listener) :
    ListAdapter<Product, ProductAdapter.ProductHolder>(ProductDiffCallback()) {

    class ProductHolder(item: View, private val listener: Listener) :
        RecyclerView.ViewHolder(item) {

        val binding = ItemForCatalogProductBinding.bind(item)
        private val image = binding.avatar

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) = with(binding) {

            ViewCompat.setTransitionName(image, product.id.toString())

            if (product.isHit) {
                hit.visibility = View.VISIBLE
                ObjectAnimator.ofFloat(hit, View.ROTATION, 0F, 360F).apply {
                    duration = 1500
                    repeatCount = 100
                }.start()
            } else {
                hit.visibility = View.INVISIBLE
            }

            if (product.isDiscount) {
                groupDiscount.visibility = View.VISIBLE
                val scalePictureX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1F, 1.3F, 1F)
                val scalePictureY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1F, 1.3F, 1F)
                ObjectAnimator.ofPropertyValuesHolder(pictureDiscount, scalePictureX, scalePictureY)
                    .apply {
                        duration = 1000
                        repeatCount = 100
                    }.start()
                textDiscount.text = "-${product.minusPercent}%"
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1F, 1.3F, 1F)
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1F, 1.3F, 1F)
                ObjectAnimator.ofPropertyValuesHolder(textDiscount, scaleX, scaleY).apply {
                    duration = 1000
                    repeatCount = 150
                }.start()
                price.alpha = 0.3f
                newPrice.text = "${
                    (product.priceN * BigDecimal(1.0 - product.minusPercent.toDouble() / 100.0)).setScale(
                        2,
                        RoundingMode.HALF_UP
                    )
                }" +
                        " ${itemView.context.getString(R.string.rub)}/${product.unitWeight}"

            } else {
                price.alpha = 1f
                groupDiscount.visibility = View.INVISIBLE
            }

            if (product.isFavorite) {
                buttonLike.setImageResource(R.drawable.like_24)
            } else {
                buttonLike.setImageResource((R.drawable.un_like_24))
            }

            avatar.setImageResource(product.picture)

            txItem.text = product.name

            price.text =
                "${product.priceN} ${itemView.context.getString(R.string.rub)}/${product.unitWeight}"

            if (product.inBasket) {
                buttonAddToBin.setBackgroundColor(itemView.context.getColor(R.color.colorVegetable))
                buttonAddToBin.text = itemView.context.getString(R.string.Delete)
            } else {
                buttonAddToBin.setBackgroundColor(itemView.context.getColor(R.color.orange))
                buttonAddToBin.text = itemView.context.getString(R.string.add)
            }

            buttonLike.setOnClickListener {
                ObjectAnimator.ofPropertyValuesHolder(
                    buttonLike,
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.3F, 1.0F),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.3F, 1.0F)
                ).start()
                listener.like(product)
            }

            buttonAddToBin.setOnClickListener {
                ObjectAnimator.ofPropertyValuesHolder(
                    buttonAddToBin,
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.1F, 1.0F),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.1F, 1.0F)
                ).start()
                listener.addToBasket(product)
            }

            cardViewItemCatalog.setOnClickListener {
                listener.goToProduct(image, product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_for_catalog_product, parent, false)
        return ProductHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}

class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Product, newItem: Product): Any =
        Payload()
}

data class Payload(
    val id: Int? = null
)