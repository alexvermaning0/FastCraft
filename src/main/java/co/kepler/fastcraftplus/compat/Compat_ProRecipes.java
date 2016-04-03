package co.kepler.fastcraftplus.compat;

import co.kepler.fastcraftplus.recipes.FastRecipe;
import co.kepler.fastcraftplus.recipes.Ingredient;
import mc.mcgrizzz.prorecipes.ProRecipes;
import mc.mcgrizzz.prorecipes.RecipeAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * Recipe compatibility for ProRecipes.
 * <p>
 * Plugin: https://www.spigotmc.org/resources/9039/
 * API: https://www.spigotmc.org/wiki/prorecipes-recipeapi/
 */
public class Compat_ProRecipes extends Compat {
    private RecipeAPI api;
    private RecipeAPI.RecipeType[] recipeTypes;

    @Override
    public boolean init() {
        api = ProRecipes.getAPI();
        recipeTypes = new RecipeAPI.RecipeType[]{
                RecipeAPI.RecipeType.SHAPED,
                RecipeAPI.RecipeType.SHAPELESS,
                RecipeAPI.RecipeType.MULTI
        };
        return true;
    }

    @Override
    public String dependsOnPlugin() {
        return "ProRecipes";
    }

    @Override
    public Set<FastRecipe> getRecipes(Player player) {
        Set<FastRecipe> recipes = new HashSet<>();
        // Loop through all recipe types
        for (RecipeAPI.RecipeType type : recipeTypes) {
            // Loop through recipes of this type
            int count = api.recipeCount(type);
            for (int id = 0; id < count; id++) {
                // Get the recipe of this type and id
                RecipeAPI.RecipeContainer recipe = api.getRecipe(type, id);
                if (!recipe.hasPermission() || player.hasPermission(recipe.getPermission())) {
                    // If player has permission to craft
                    recipes.add(new FastRecipeCompat(recipe));
                }
            }
        }
        return recipes;
    }

    public static class FastRecipeCompat extends FastRecipe {
        private final Map<Ingredient, Integer> ingredients = new HashMap<>();
        private final List<ItemStack> results = new ArrayList<>();

        public FastRecipeCompat(RecipeAPI.RecipeContainer recipe) {
            Collections.addAll(results, recipe.getResult());
            for (ItemStack is : recipe.getIngredients()) {
                Ingredient ingredient = new Ingredient(is);
                Integer amount = ingredients.get(ingredient);
                ingredients.put(ingredient, (amount == null ? 0 : amount) + is.getAmount());
            }
        }

        @Override
        public Map<Ingredient, Integer> getIngredients() {
            return ingredients;
        }

        @Override
        public List<ItemStack> getResults() {
            return results;
        }

        @Override
        public ItemStack[] getMatrix() {
            return null; // TODO
        }
    }
}
