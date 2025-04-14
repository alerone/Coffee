package com.alvarobrivaro.coffee.data.recipe

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.alvarobrivaro.coffee.data.ingredient.IngredientEntity
import com.alvarobrivaro.coffee.data.ingredient.IngredientWithQuantityDb
import com.alvarobrivaro.coffee.domain.models.Recipe

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false) val id: Long = 0,
    val name: String,
    val description: String,
)

@Entity(
    tableName = "recipe_ingredients",
    foreignKeys = [
        ForeignKey(entity = RecipeEntity::class, parentColumns = ["id"], childColumns = ["recipeId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = IngredientEntity::class, parentColumns = ["id"], childColumns = ["ingredientId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("recipeId"), Index("ingredientId")]
)
data class RecipeIngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipeId: Long,
    val ingredientId: Long,
    val quantity: Double,
    val unit: String // unidad: "gr, "ml"
)

data class RecipeWithIngredients(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        entity = RecipeIngredientEntity::class,
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<IngredientWithQuantityDb>
) {
    fun toDomain(): Recipe {
        return Recipe(
            id = recipe.id.toInt(),
            name = recipe.name,
            description = recipe.description,
            ingredients = ingredients.map { ingredient -> ingredient.toDomain() }
        )
    }
}
