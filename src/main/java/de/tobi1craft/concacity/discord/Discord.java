package de.tobi1craft.concacity.discord;

import de.tobi1craft.concacity.Concacity;
import de.tobi1craft.concacity.util.ConcacityConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class Discord implements EventListener {
    private static final ConcacityConfig CONFIG = Concacity.CONFIG;
    private static final Logger LOGGER = Concacity.LOGGER;

    public static void registerDiscord() {
        JDABuilder builder = JDABuilder.createDefault(CONFIG.token());
        builder.setActivity(Activity.playing("Concacity Minecraft Mod"));
        builder.addEventListeners(new Discord());
        JDA jda = builder.build();
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEvent(@NotNull GenericEvent event) {
        if (event instanceof ReadyEvent) LOGGER.info("Discord API is ready!");
    }
}
