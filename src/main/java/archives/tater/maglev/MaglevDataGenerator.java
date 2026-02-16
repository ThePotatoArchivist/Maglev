package archives.tater.maglev;

import archives.tater.maglev.datagen.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MaglevDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();
		pack.addProvider(ModelGenerator::new);
		pack.addProvider(LangGenerator::new);
        pack.addProvider(ItemTagGenerator.factory(
				pack.addProvider(BlockTagGenerator::new)
		));
		pack.addProvider(BlockLootTableGenerator::new);
		pack.addProvider(MaglevRecipeGenerator.Provider::new);
		pack.addProvider(RRVRecipeGenerator::new);
	}
}
