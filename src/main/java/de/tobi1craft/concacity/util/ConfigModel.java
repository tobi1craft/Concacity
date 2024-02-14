package de.tobi1craft.concacity.util;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = "concacity")
@Config(name = "concacity", wrapperName = "ConcacityConfig")
@SuppressWarnings("unused")
public class ConfigModel {
    @SectionHeader("reworks")
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @ExcludeFromScreen
    public boolean elytra = false;

    @SectionHeader("helper")
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @ExcludeFromScreen

    public int helper_upgrade_searchRadius_0 = 20;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @ExcludeFromScreen
    public int helper_upgrade_searchRadius_1 = 50;
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @ExcludeFromScreen
    public int helper_upgrade_searchRadius_2 = 100;
}


