package com.facuu16.dbchbot;

import com.facuu16.dbchbot.commands.ShopCommands;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public class DBCHBot {
    private static String token;

    public DBCHBot() {
        token = "MTAyOTU3MjA5NjMxMDkxMTA0Nw.GP61OT._ecFjAn0RcxAnBYF5H8I9VEwXxOiXJEn-WmCWA";
        JDABuilder builder = JDABuilder.createDefault(token);

        builder
                .setStatus(OnlineStatus.IDLE)
                .setActivity(Activity.playing("DBCHispano 6.0"));

        builder.build();
    }

    public static void main(String[] args) {
        new DBCHBot();
        new ShopCommands(token);
    }
}
