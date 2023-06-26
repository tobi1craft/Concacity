package de.tobi1craft.concacity.util;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.RestartRequired;
import io.wispforest.owo.config.annotation.SectionHeader;

@Modmenu(modId = "concacity")
@Config(name = "concacity", wrapperName = "ConcacityConfig")
public class ConfigModel {
    @SectionHeader("functions")
    @RestartRequired
    public boolean elytra_durability_loss = true;
}
