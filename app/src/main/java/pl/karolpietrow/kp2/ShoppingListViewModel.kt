package pl.karolpietrow.kp2

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShoppingListViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("list_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val _productList = MutableLiveData<List<Product>>(loadProductList())
    val productList: LiveData<List<Product>> get() = _productList

    private fun loadProductList() : List<Product> {
        val jsonString = sharedPreferences.getString("product_list", null)
        return if (jsonString != null) {
            val type = object: TypeToken<List<Product>>(){}.type
            gson.fromJson(jsonString, type)
        } else {
            emptyList()
        }
    }

    private fun saveProductList() {
        val editor = sharedPreferences.edit()
        val jsonString = gson.toJson(_productList.value)
        editor.putString("product_list", jsonString)
        editor.apply()
    }

    fun addProduct(product: Product) {
        val updatedList = _productList.value.orEmpty() + product
        _productList.value = updatedList
        saveProductList()
    }

    fun toggleProductPurchased(id: String) {
        _productList.value = _productList.value?.map {
            if (it.id == id) it.copy(isPurchased = !it.isPurchased) else it
        }
        saveProductList()
    }

    fun deleteProduct(id: String) {
        _productList.value = _productList.value?.filter { it.id != id }
        saveProductList()
    }

}