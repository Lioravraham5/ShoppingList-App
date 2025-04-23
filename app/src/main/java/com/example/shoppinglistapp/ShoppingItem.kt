package com.example.shoppinglistapp

data class ShoppingItem(val id: Int,
                        var name: String,
                        var  quantity: Int,
                        var isEditing: Boolean = false) {
}
