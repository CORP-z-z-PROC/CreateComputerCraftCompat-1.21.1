package net.pcfractal.createcomputers.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.pcfractal.createcomputers.CCCC;
public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CCCC.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
    }
}
