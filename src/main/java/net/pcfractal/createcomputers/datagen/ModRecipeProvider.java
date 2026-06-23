package net.pcfractal.createcomputers.datagen;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.pcfractal.createcomputers.block.CCCCBlocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CCCCBlocks.REDSTONE_LINK_BRIDGE.get())
                .pattern("LT")
                .pattern("BW")
                .define('L', AllBlocks.REDSTONE_LINK.get())
                .define('T', AllItems.TRANSMITTER.get())
                .define('B', AllBlocks.BRASS_CASING)
                .define('W', BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("computercraft", "wireless_modem_advanced")))
                .unlockedBy("has_wireless_modem_advanced", has(BuiltInRegistries.BLOCK.get(ResourceLocation.fromNamespaceAndPath("computercraft", "wireless_modem_advanced"))));
    }
}
