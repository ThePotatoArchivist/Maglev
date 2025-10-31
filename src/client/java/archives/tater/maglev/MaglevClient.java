package archives.tater.maglev;

import archives.tater.maglev.init.MaglevBlocks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;

import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

import java.util.function.Consumer;

public class MaglevClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		Consumer<Block> setCutout = block -> BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        MaglevBlocks.MAGLEV_RAIL.forEach(setCutout);
        MaglevBlocks.POWERED_MAGLEV_RAIL.forEach(setCutout);
        MaglevBlocks.VARIABLE_MAGLEV_RAIL.forEach(setCutout);
	}
}