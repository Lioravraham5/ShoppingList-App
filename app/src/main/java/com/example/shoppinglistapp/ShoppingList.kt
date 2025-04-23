package com.example.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ShoppingListApp() {
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {

        // add item button
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add Item")
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            items(sItems){
                item ->
                if(item.isEditing){
                    ShoppingItemEditor(
                        item = item,
                        onEditComplete = {
                            editedName, editedQuantity ->
                            sItems = sItems.map {it.copy(isEditing = false)} // Set all items to not editing
                            val editedItem =  sItems.find {it.id == item.id} // Find the item that was edited
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity
                            }
                        })
                }
                else{
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                            // finding the item that was clicked for editing and setting it isEditing parameter to true
                            sItems = sItems.map {it.copy(isEditing = it.id == item.id)}
                        },
                        onDeleteClick = {
                            sItems = sItems - item // remove the item from the list
                        }
                    )
                }
            }
        }

    }

    if(showDialog) {
        // Show dialog
        AlertDialog(onDismissRequest = { showDialog = false },
                    confirmButton = {
                        Row(modifier = Modifier.fillMaxWidth().padding(8.dp),
                            /* Place the first element on the left side,
                            the last on the right side,
                            and all the rest - divide the spaces between them equally*/
                            horizontalArrangement = Arrangement.SpaceBetween) {

                            // Add button
                            Button(onClick = {
                                if(itemName.isNotBlank()){

                                    // Create a new ShoppingItem object
                                    val newItem = ShoppingItem(
                                        id = sItems.size + 1,
                                        name = itemName,
                                        quantity = itemQuantity.toInt()
                                    )

                                    sItems = sItems + newItem // Add the new item to the list
                                    showDialog = false // Close the dialog
                                    itemName = "" // Reset the item name
                                    itemQuantity = "1" // Reset the item quantity

                                }
                            }) {
                                Text(text = "Add")
                            }

                            // Cancel button
                            Button(onClick = { showDialog = false }) {
                                Text(text = "Cancel")
                            }

                        }


                    },

                    title = { Text(text = "Add Shopping Item") },
                    text = {
                        Column {
                            // OutlinedTextField for item name
                            OutlinedTextField(
                                value = itemName,
                                onValueChange = {
                                    // Here goes what should happen, when the value of our OutlinedTextField changes
                                    itemName = it // Update the itemName value to be the new value
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                label = { Text("Enter item name") })

                            // OutlinedTextField for item `
                            OutlinedTextField(
                                value = itemQuantity,
                                onValueChange = {
                                    // Here goes what should happen, when the value of our OutlinedTextField changes
                                    itemQuantity = it // Update the itemName value to be the new value
                                },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                label = { Text("Enter item quantity") })

                        }

                    })
    }

}

@Composable
fun ShoppingListItem(
    item: ShoppingItem,
    onEditClick: () -> Unit, // onEditClick is a lambda function that takes no parameters and returns Unit = no output
    onDeleteClick: () -> Unit, // onDeleteClick is a lambda function that takes no parameters and returns Unit = no output
){
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp).border(
            border = BorderStroke(2.dp, Color(0xFF018786)),
            shape = RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))

        Row(modifier = Modifier.padding(8.dp)) {
            // Edit IconButton
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Item")
            }
            // Delete IconButton
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Item")
            }

        }

    }

}

@Composable
fun ShoppingItemEditor(item: ShoppingItem,
                       onEditComplete: (String, Int) -> Unit){
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) }
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(modifier = Modifier.fillMaxWidth().background(Color.White).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        Column {
            // TextField for item name
            BasicTextField(
                modifier = Modifier.wrapContentSize().padding(8.dp),
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true)

            // TextField for item quantity
            BasicTextField(
                modifier = Modifier.wrapContentSize().padding(8.dp),
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                singleLine = true)
        }

        Button(
            onClick = {
                isEditing = false
                onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
            }) {
            Text("Save")
        }
    }

}
