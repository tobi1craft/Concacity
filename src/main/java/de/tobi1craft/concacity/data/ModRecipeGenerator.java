package de.tobi1craft.concacity.data;

import de.tobi1craft.concacity.block.ModBlocks;
import de.tobi1craft.concacity.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        //TODO: Rezept für Helper?!
        /*
                ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.CONCACITY_BLOCK)
                .pattern("###").pattern("#S#").pattern("###")
                .input('#', Items.CHERRY_PLANKS).input('S', Items.STONE)
                .criterion(FabricRecipeProvider.hasItem(Items.CHERRY_PLANKS), FabricRecipeProvider.conditionsFromItem(Items.CHERRY_PLANKS))
                .criterion(FabricRecipeProvider.hasItem(Items.STONE), FabricRecipeProvider.conditionsFromItem(Items.STONE))
                .offerTo(exporter, new Identifier("test_cb"));
         */
    }
}
