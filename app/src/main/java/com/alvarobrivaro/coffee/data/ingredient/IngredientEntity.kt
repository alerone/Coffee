package com.alvarobrivaro.coffee.data.ingredient

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.alvarobrivaro.coffee.data.recipe.RecipeIngredientEntity
import com.alvarobrivaro.coffee.domain.models.Ingredient
import com.alvarobrivaro.coffee.domain.models.IngredientWithQuantity

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
) {
    fun toDomain(): Ingredient {
        return Ingredient(id = id, name = name)
    }
}

data class IngredientWithQuantityDb(
    @Embedded val join: RecipeIngredientEntity,
    @Relation(
        parentColumn = "ingredientId",
        entityColumn = "id"
    )
    val ingredient: IngredientEntity
) {
    fun toDomain(): IngredientWithQuantity {
        return IngredientWithQuantity(
            ingredient = ingredient.toDomain(),
            quantity = join.quantity,
            unit = join.unit
        )
    }
}
