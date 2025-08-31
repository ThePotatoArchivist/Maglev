package archives.tater.maglev.init;

import archives.tater.maglev.Maglev;
import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.text.Text;
import net.minecraft.world.GameRules;
import org.jetbrains.annotations.ApiStatus;

public class MaglevGamerules {
    @ApiStatus.Internal
    public static final String CATEGORY_TRANSLATION = "gamerule.category.maglev";

    public static final CustomGameRuleCategory CATEGORY = new CustomGameRuleCategory(Maglev.id("maglev"), Text.translatable(CATEGORY_TRANSLATION));

    public static final GameRules.Key<GameRules.BooleanRule> MINECART_COLLISION = GameRuleRegistry.register(Maglev.MOD_ID + ".minecart_collision", CATEGORY, GameRuleFactory.createBooleanRule(true));

    public static void init() {

    }
}
