package ru.netology.estore.model

import ru.netology.estore.dto.Product

data class FullProduct(
    var products:List<Product> = emptyList(),
    var status:String,
) {
    val emptyBasket:Boolean
    get()= products.filter {
        it.inBasket
    }.isEmpty()
}
