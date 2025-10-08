package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;

import net.minecraft.block.CopperBlockSet;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BlockLootTableGenerator extends FabricBlockLootTableProvider {
    public BlockLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    private void addDrops(CopperBlockSet blockSet) {
        for (var block : blockSet.getAll())
            addDrop(block);
    }

    @Override
    public void generate() {
        addDrops(MaglevBlocks.MAGLEV_RAIL);
        addDrops(MaglevBlocks.POWERED_MAGLEV_RAIL);
        addDrops(MaglevBlocks.VARIABLE_MAGLEV_RAIL);
    }
}
