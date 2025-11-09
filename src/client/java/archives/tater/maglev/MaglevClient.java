package archives.tater.maglev;

import archives.tater.maglev.init.MaglevBlocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.world.level.block.Block;
import java.util.function.Consumer;

public class MaglevClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		Consumer<Block> setCutout = block -> BlockRenderLayerMap.putBlock(block, ChunkSectionLayer.CUTOUT);
        MaglevBlocks.MAGLEV_RAIL.forEach(setCutout);
        MaglevBlocks.POWERED_MAGLEV_RAIL.forEach(setCutout);
        MaglevBlocks.VARIABLE_MAGLEV_RAIL.forEach(setCutout);
	}
}