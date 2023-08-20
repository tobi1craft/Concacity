package de.tobi1craft.concacity.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;

import java.util.function.Consumer;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
/*

        offerReversibleCompactingRecipes(exporter, RecipeCategory.BUILDING_BLOCKS, ModItems.CONCACITY, RecipeCategory.MISC, ModBlocks.CONCACITY_BLOCK);

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.CONCACITY_BLOCK)
                .pattern("###").pattern("#S#").pattern("###")
                .input('#', Items.CHERRY_PLANKS).input('S', Items.STONE)
                .criterion(FabricRecipeProvider.hasItem(Items.CHERRY_PLANKS), FabricRecipeProvider.conditionsFromItem(Items.CHERRY_PLANKS))
                .criterion(FabricRecipeProvider.hasItem(Items.STONE), FabricRecipeProvider.conditionsFromItem(Items.STONE))
                .offerTo(exporter, new Identifier("test_cb"));
*/

    }
}
