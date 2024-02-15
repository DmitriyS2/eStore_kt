package ru.netology.estore.adapter

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.estore.R
import ru.netology.estore.databinding.ItemForBasketProductBinding
import ru.netology.estore.dto.DataLang.Companion.bigDecZero
import ru.netology.estore.dto.Product
import java.math.BigDecimal
import java.math.RoundingMode

class ProductInBasketAdapter(private val listener: Listener) :
    ListAdapter<Product, ProductInBasketAdapter.ProductInBasketHolder>(ProductDiffCallback()) {

    class ProductInBasketHolder(item: View, private val listener: Listener) :
        RecyclerView.ViewHolder(item) {
        val binding = ItemForBasketProductBinding.bind(item)

        @SuppressLint("SetTextI18n")
        fun bind(product: Product) = with(binding) {
            avatar.setImageResource(product.picture)
            txItem.text = product.name

            if (product.isFavorite) {
                buttonLike.setImageResource(R.drawable.like_24)
            } else {
                buttonLike.setImageResource((R.drawable.un_like_24))
            }

            if (product.isDiscount) {
                txMinusPrice.visibility = View.VISIBLE
                txMinusPrice.text = "-${product.minusPercent}%"
                txPrice.setTextColor(itemView.context.getColor(R.color.red))
                txPrice.text = "${
                    (product.priceN * BigDecimal((100 - product.minusPercent) / 100.0)).setScale(
                        2,
                        RoundingMode.HALF_UP
                    )
                }" +
                        " ${itemView.context.getString(R.string.rub)}/${product.unitWeight}"
            } else {
                txMinusPrice.visibility = View.GONE
                txPrice.setTextColor(itemView.context.getColor(R.color.black))
                txPrice.text =
                    "${product.priceN} ${itemView.context.getString(R.string.rub)}/${product.unitWeight}"
            }

            if (product.weightN > product.oneUnitN) {
                groupWeight.visibility = View.VISIBLE
                buttonAdd.visibility = View.GONE
                buttonMinus.setImageResource(R.drawable.button_minus_24)
            } else if (product.weightN == product.oneUnitN) {
                groupWeight.visibility = View.VISIBLE
                buttonAdd.visibility = View.GONE
                buttonMinus.setImageResource(R.drawable.delete_green_24)
            } else if (product.weightN == bigDecZero) {
                groupWeight.visibility = View.GONE
                buttonAdd.visibility = View.VISIBLE
            }

            txSumma.text = "${
                product.sumN.setScale(
                    2,
                    RoundingMode.HALF_UP
                )
            } ${itemView.context.getString(R.string.rub)}"

            txWeight.text = "${product.weightN} ${product.unitWeight}"

            buttonLike.setOnClickListener {
                showAnimationView(it)
                listener.like(product)
            }

            buttonDelete.setOnClickListener {
                showAnimationView(it)
                listener.deleteFromBasket(product)
            }

            buttonPlus.setOnClickListener {
                showAnimationView(it)
                listener.weightPlus(product)
            }
            buttonMinus.setOnClickListener {
                showAnimationView(it)
                listener.weightMinus(product)
            }
            buttonAdd.setOnClickListener {
                showAnimationView(it)
                listener.addToBasketAgain(product)
            }

            cardViewItemBasket.setOnClickListener {
                listener.goToProduct(it, product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductInBasketHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_for_basket_product, parent, false)
        return ProductInBasketHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ProductInBasketHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }
}

fun showAnimationView(view: View) {
    ObjectAnimator.ofPropertyValuesHolder(
        view,
        PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0F, 1.3F, 1.0F),
        PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0F, 1.3F, 1.0F)
    ).start()
}