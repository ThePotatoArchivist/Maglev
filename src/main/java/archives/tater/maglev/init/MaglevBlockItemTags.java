package archives.tater.maglev.init;

import archives.tater.maglev.Maglev;

import net.minecraft.tags.BlockItemTagId;

public class MaglevBlockItemTags {

    private static BlockItemTagId create(String path) {
        var id = Maglev.id(path);
        return BlockItemTagId.create(id, id);
    }

    public static final BlockItemTagId MAGLEV_RAILS = create("maglev_rails");
    public static final BlockItemTagId POWERED_MAGLEV_RAILS = create("powered_maglev_rails");
    public static final BlockItemTagId VARIABLE_MAGLEV_RAILS = create("variable_maglev_rails");
    public static final BlockItemTagId HOVERABLE_RAILS = create("hoverable_rails");
    public static final BlockItemTagId MANUALLY_OXIDIZABLE = create("manually_oxidizable");
}
