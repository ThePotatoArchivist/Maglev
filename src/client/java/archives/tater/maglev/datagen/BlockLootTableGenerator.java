package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.WeatheringCopperBlocks;
import java.util.concurrent.CompletableFuture;

public class BlockLootTableGenerator extends FabricBlockLootTableProvider {
    public BlockLootTableGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, registryLookup);
    }

    private void addDrops(WeatheringCopperBlocks blockSet) {
        for (var block : blockSet.asList())
            dropSelf(block);
    }

    @Override
    public void generate() {
        addDrops(MaglevBlocks.MAGLEV_RAIL);
        addDrops(MaglevBlocks.POWERED_MAGLEV_RAIL);
        addDrops(MaglevBlocks.VARIABLE_MAGLEV_RAIL);
    }
}
