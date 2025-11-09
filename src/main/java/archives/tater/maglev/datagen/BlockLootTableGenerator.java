package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevBlocks.CopperBlockSet;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import java.util.concurrent.CompletableFuture;

public class BlockLootTableGenerator extends FabricBlockLootTableProvider {
    public BlockLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    private void addDrops(CopperBlockSet blockSet) {
        for (var block : blockSet.getAll())
            dropSelf(block);
    }

    @Override
    public void generate() {
        addDrops(MaglevBlocks.MAGLEV_RAIL);
        addDrops(MaglevBlocks.POWERED_MAGLEV_RAIL);
        addDrops(MaglevBlocks.VARIABLE_MAGLEV_RAIL);
    }
}
