package pl.karolpietrow.kp2

data class Product(
    val id: String,
    val name: String,
    val quantity: Int,
    val isPurchased: Boolean = false
)