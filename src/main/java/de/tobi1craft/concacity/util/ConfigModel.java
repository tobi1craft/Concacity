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

    @SectionHeader("discord")
    @SuppressWarnings("unused")
    public boolean discord_enabled = false;
    @SuppressWarnings("unused")
    public String discord_token = "";
}


