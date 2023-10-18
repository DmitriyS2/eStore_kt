package ru.netology.estore.model

import ru.netology.estore.dto.Data
import ru.netology.estore.dto.Product

data class FullProduct(
    var products:List<Product> = emptyList(),
    var status:String = Data.allGroup
)
