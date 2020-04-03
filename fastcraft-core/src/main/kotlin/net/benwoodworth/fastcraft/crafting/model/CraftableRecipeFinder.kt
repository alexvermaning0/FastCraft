package net.benwoodworth.fastcraft.crafting.model

import net.benwoodworth.fastcraft.platform.item.FcItem
import net.benwoodworth.fastcraft.platform.player.FcPlayer
import net.benwoodworth.fastcraft.platform.recipe.FcCraftingRecipe
import net.benwoodworth.fastcraft.platform.recipe.FcCraftingRecipePrepared
import net.benwoodworth.fastcraft.platform.recipe.FcRecipeProvider
import net.benwoodworth.fastcraft.util.CancellableResult
import net.benwoodworth.fastcraft.util.getPermutations
import javax.inject.Inject
import javax.inject.Provider

class CraftableRecipeFinder @Inject constructor(
    private val recipeProvider: FcRecipeProvider,
    private val itemAmountsProvider: Provider<ItemAmounts>,
) {
    private companion object {
        val ingredientComparator = compareBy<Map.Entry<FcItem, Int>>(
            { (item, _) -> item.hasMetadata }, // Items with meta last
            { (_, amount) -> -amount }, // Greatest amount first
        )
    }

    fun getCraftableRecipes(
        player: FcPlayer,
        availableItems: ItemAmounts,
    ): Sequence<FcCraftingRecipePrepared> {
        return recipeProvider.getCraftingRecipes()
            .flatMap { prepareCraftableRecipes(player, availableItems, it) }
    }

    private fun prepareCraftableRecipes(
        player: FcPlayer,
        availableItems: ItemAmounts,
        recipe: FcCraftingRecipe,
    ): Sequence<FcCraftingRecipePrepared> = sequence {
        val results = mutableSetOf<List<FcItem>>()

        val ingredients = recipe.ingredients

        val possibleIngredientItems = ingredients.map { ingredient ->
            availableItems
                .asMap().entries
                .filter { (item, _) -> ingredient.matches(item) }
                .sortedWith(ingredientComparator)
                .map { (item, _) -> item }
        }

        val itemsUsed = itemAmountsProvider.get()
        possibleIngredientItems.getPermutations().forEach { permutation ->
            itemsUsed.clear()
            permutation.forEach { itemsUsed += it }

            val enoughItems = itemsUsed.asMap().all { (item, amount) ->
                availableItems[item] >= amount
            }

            if (enoughItems) {
                val ingredientItems = ingredients
                    .mapIndexed { i, ingredient -> ingredient to permutation[i] }
                    .toMap()

                val prepared = recipe.prepare(player, ingredientItems)

                if (prepared is CancellableResult.Result) {
                    val resultPreview = prepared.result.resultsPreview
                    if (resultPreview !in results) {
                        results += resultPreview
                        yield(prepared.result)
                    }
                }
            }
        }
    }
}
