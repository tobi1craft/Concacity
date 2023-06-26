package de.tobi1craft.concacity.util;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;

@Modmenu(modId = "concacity")
@Config(name = "concacity", wrapperName = "ConcacityConfig")
public class ConfigModel {
    @SectionHeader("functions")
    public boolean no_elytra_durability = true;
}
