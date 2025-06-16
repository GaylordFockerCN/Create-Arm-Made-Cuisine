package com.p1nero.create_cuisine.recipe;

import com.p1nero.create_cuisine.CreateCuisineMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class CompatRecipeProvider extends RecipeProvider {

    public CompatRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        Item tomato = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("farmersdelight", "tomato"));

        // 番茄炒蛋
        if (tomato != Items.AIR) {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, getItem("scrambled_egg_and_tomato"))
                    .requires(Items.EGG)
                    .requires(tomato)
                    .unlockedBy("has_egg", has(Items.EGG))
                    .save(recipeOutput, compatId("scrambled_egg_and_tomato"));
        }

        // 蔬菜拼盘
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, getItem("vegetable_platter"))
                .requires(Items.CARROT).requires(Items.POTATO).requires(Items.BEETROOT)
                .unlockedBy("has_carrot", has(Items.CARROT))
                .save(recipeOutput, compatId("vegetable_platter"));

        // 肉类拼盘
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, getItem("meat_platter"))
                .requires(Items.BEEF).requires(Items.PORKCHOP).requires(Items.CHICKEN)
                .unlockedBy("has_beef", has(Items.BEEF))
                .save(recipeOutput, compatId("meat_platter"));

        //肉酱意面
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, getItem("meat_pasta"))
                .requires(Items.WHEAT)
                .requires(Items.COOKED_BEEF)
                .unlockedBy("has_wheat", has(Items.WHEAT))
                .save(recipeOutput, compatId("meat_pasta"));

        //在这里继续添加
        // ...

    }

    private static ItemLike getItem(String path) {
        Item item = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("cuisinedelight", path));
        if (item == Items.AIR) {
            throw new IllegalStateException("Could not find Cuisine Delight item: " + path);
        }
        return item;
    }


    private static ResourceLocation compatId(String path) {
        return ResourceLocation.fromNamespaceAndPath(CreateCuisineMod.MODID, "compat/" + path);
    }
}

