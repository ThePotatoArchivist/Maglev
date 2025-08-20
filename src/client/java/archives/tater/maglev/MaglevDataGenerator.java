package archives.tater.maglev;

import archives.tater.maglev.datagen.BlockTagGenerator;
import archives.tater.maglev.datagen.ItemTagGenerator;
import archives.tater.maglev.datagen.LangGenerator;
import archives.tater.maglev.datagen.ModelGenerator;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class MaglevDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		var pack = fabricDataGenerator.createPack();
		pack.addProvider(ModelGenerator::new);
		pack.addProvider(LangGenerator::new);
		var blockTagGenerator = pack.addProvider(BlockTagGenerator::new);
		pack.addProvider((output, registries) -> new ItemTagGenerator(output, registries, blockTagGenerator));
	}
}
