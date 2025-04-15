package com.alvarobrivaro.coffee.data.inventory

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.alvarobrivaro.coffee.data.ingredient.IngredientEntity
import com.alvarobrivaro.coffee.domain.models.IngredientWithQuantity

@Entity(
    tableName = "inventory",
    foreignKeys = [
        ForeignKey(
            entity = IngredientEntity::class,
            parentColumns = ["id"],
            childColumns = ["ingredientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ingredientId")]
)
data class InventoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ingredientId: Long,
    val quantity: Double,
    val unit: String // unidad: "gr", "ml", etc.
) {
    fun toDomain(ingredient: IngredientEntity): IngredientWithQuantity {
        return IngredientWithQuantity(
            ingredient = ingredient.toDomain(),
            quantity = quantity,
            unit = unit
        )
    }
}

data class InventoryWithIngredient(
    @Embedded val inventory: InventoryEntity,
    @Relation(
        parentColumn = "ingredientId",
        entityColumn = "id"
    )
    val ingredient: IngredientEntity
) {
    fun toDomain(): IngredientWithQuantity {
        return inventory.toDomain(ingredient)
    }
}