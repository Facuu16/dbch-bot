package com.facuu16.dbchbot;

import com.facuu16.dbchbot.commands.ServiceCommands;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class DBCHBot {
    private static String token;

    public DBCHBot() {
        token = System.getenv("DISCORD_BOT_TOKEN");
        JDABuilder builder = JDABuilder.createDefault(token);

        builder
                .setStatus(OnlineStatus.IDLE)
                .setActivity(Activity.playing("DBCHispano 6.0"));

        builder.build();
    }

    public static void main(String[] args) {
        new DBCHBot();
        new ServiceCommands(token);
    }
}
