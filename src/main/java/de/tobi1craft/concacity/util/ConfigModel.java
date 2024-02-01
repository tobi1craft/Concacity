package de.tobi1craft.concacity.util;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = "concacity")
@Config(name = "concacity", wrapperName = "ConcacityConfig")
public class ConfigModel {
    @SectionHeader("functions")
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @ExcludeFromScreen
    @SuppressWarnings("unused")
    public boolean elytra_durability_loss = true;

    @SectionHeader("helper")
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @ExcludeFromScreen
    @SuppressWarnings("unused")
    public int helper_upgrade_searchRadius_0 = 20;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @ExcludeFromScreen
    @SuppressWarnings("unused")
    public int helper_upgrade_searchRadius_1 = 50;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @ExcludeFromScreen
    @SuppressWarnings("unused")
    public int helper_upgrade_searchRadius_2 = 100;
}


