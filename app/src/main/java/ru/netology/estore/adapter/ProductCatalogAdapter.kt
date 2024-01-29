package ru.netology.estore.adapter

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
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

class ProductAdapter(private val listener: Listener) :
    ListAdapter<Product, ProductAdapter.ProductHolder>(ProductDiffCallback()) {

    var productList = emptyList<Product>()

    class ProductHolder(item: View, private val listener: Listener) : RecyclerView.ViewHolder(item) {
        val binding = ItemForCatalogProductBinding.bind(item)

        fun bind(payload: Payload) {
//            payload.liked?.also { liked ->
//                binding.buttonLike.setImageResource(
//                    if (liked) R.drawable.like_24 else R.drawable.un_like_24
//                )
//                if (liked) {
//                    ObjectAnimator.ofPropertyValuesHolder(
//                        binding.buttonLike,
//                        PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.2F, 1.0F),
//                        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.2F, 1.0F)
//                    ).start()
//                } else {
//                    ObjectAnimator.ofFloat(
//                        binding.buttonLike,
//                        View.ROTATION,
//                        0F, 360F
//                    ).start()
//                }
//            }
//
//            payload.pushButtonAdd?.let {
//                if (it) {
//                    binding.buttonAddToBin.setBackgroundColor(Color.parseColor("#104021"))
//                    binding.buttonAddToBin.text = "Убрать"
//                } else {
//                    binding.buttonAddToBin.setBackgroundColor(Color.BLUE)
//                    binding.buttonAddToBin.text = "Добавить"
//                }
//            }
        }


        fun bind(product: Product) = with(binding) {
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
                ObjectAnimator.ofPropertyValuesHolder(pictureDiscount, scalePictureX, scalePictureY).apply {
                    duration = 1000
                    repeatCount = 100
                }.start()
                textDiscount.text = "-${product.minusPercent}%"
                val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1F, 1.3F, 1F)
                val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1F, 1.3F, 1F)
                ObjectAnimator.ofPropertyValuesHolder(textDiscount, scaleX, scaleY).apply {
                    duration = 1000
                    repeatCount = 100
                }.start()
                Price.alpha = 0.3f
                newPrice.text = "${
                    getSumWithTwoDecimal(
                        product.price * (100 - product.minusPercent) / 100,
                        100.0
                    )
                } руб за 1 ${product.unitWeight}"

            } else {
                Price.alpha = 1f
                groupDiscount.visibility = View.INVISIBLE
            }

            if (product.isFavorite) {
                buttonLike.setImageResource(R.drawable.like_24)
            } else {
                buttonLike.setImageResource((R.drawable.un_like_24))
            }

            avatar.setImageResource(product.picture)

            txItem.text = product.name

            Price.text = "${product.price.toString()} руб за 1 ${product.unitWeight}"

            if (product.inBasket) {
                buttonAddToBin.setBackgroundColor(Color.parseColor("#104021"))
                buttonAddToBin.text = "Убрать"
            } else {
                buttonAddToBin.setBackgroundColor(Color.parseColor("#f2570f"))
                buttonAddToBin.text = "Добавить"
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

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_for_catalog_product, parent, false)
        return ProductHolder(view, listener)
    }

    override fun onBindViewHolder(
        holder: ProductHolder,
        position: Int,
        payloads: List<Any>
    ) {
     //   if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
//        } else {
//            payloads.forEach {
//                if (it is Payload) {
//                    holder.bind(it)
//                }
//            }
//        }
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return productList.size
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
        Payload(
//            liked = newItem.isFavorite.takeIf { oldItem.isFavorite != it },
//            pushButtonAdd = newItem.inBasket.takeIf { oldItem.inBasket != it },
        )
}

data class Payload(
    val id:Int? = null
//    val liked: Boolean? = null,
//    val pushButtonAdd: Boolean? = null,
)