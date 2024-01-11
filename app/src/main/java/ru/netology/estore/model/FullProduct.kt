package ru.netology.estore.model

import ru.netology.estore.dto.Data
import ru.netology.estore.dto.Product

data class FullProduct(
    var products:List<Product> = emptyList(),
    var status:String = Data.allGroup,
  //  var isEmptyBasket:Boolean = false,
  //  var statusBasket:Boolean = false,
  //  var statusCatalog:Boolean = false,


) {
    val emptyBasket:Boolean
    get()= products.filter {
        it.inBasket
    }.isEmpty()
}
