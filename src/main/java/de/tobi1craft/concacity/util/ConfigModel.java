package de.tobi1craft.concacity.util;

import io.wispforest.owo.config.Option;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = "concacity")
@Config(name = "concacity", wrapperName = "ConcacityConfig")
public class ConfigModel {
    @SectionHeader("functions")
    @Sync(Option.SyncMode.OVERRIDE_CLIENT)
    @ExcludeFromScreen
    public boolean elytra_durability_loss = true;

    @SectionHeader("discord")
    public boolean discord_enabled = false;
    public String token = "";
}


