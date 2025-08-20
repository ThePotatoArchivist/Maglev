package archives.tater.maglev.datagen;

import archives.tater.maglev.init.MaglevBlocks;
import archives.tater.maglev.init.MaglevItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {

    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture, @Nullable BlockTagProvider blockTagProvider) {
        super(output, registriesFuture, blockTagProvider);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        copy(MaglevBlocks.MAGLEV_RAILS, MaglevItems.MAGLEV_RAILS);
        copy(MaglevBlocks.POWERED_MAGLEV_RAILS, MaglevItems.POWERED_MAGLEV_RAILS);
        copy(MaglevBlocks.VARIABLE_MAGLEV_RAILS, MaglevItems.VARIABLE_MAGLEV_RAILS);
        copy(MaglevBlocks.HOVERABLE_RAILS, MaglevItems.HOVERABLE_RAILS);
    }
}
