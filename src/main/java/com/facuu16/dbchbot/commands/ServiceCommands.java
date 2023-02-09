package com.facuu16.dbchbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.awt.*;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Random;

public class ServiceCommands extends ListenerAdapter {
    public ServiceCommands(String token) {
        JDA bot = JDABuilder.createLight(token, Collections.emptyList())
                .addEventListeners(this)
                .build();

        bot.updateCommands().addCommands(
                Commands.slash("ofrecer-servicio", "Ofrece un servicio/producto en el mercado.")
                        .setGuildOnly(true)
                        .addOption(OptionType.STRING, "title", "Título", true)
                        .addOption(OptionType.STRING, "description", "Descripción", true)
                        .addOption(OptionType.STRING, "details", "Detalles", true)
                        .addOption(OptionType.STRING, "price", "Precio", true),

                Commands.slash("solicitar-servicio", "Solicita un servicio/producto en el mercado.")
                        .setGuildOnly(true)
                        .addOption(OptionType.STRING, "title", "Título", true)
                        .addOption(OptionType.STRING, "description", "Descripción", true)
                        .addOption(OptionType.STRING, "details", "Detalles", true)
                        .addOption(OptionType.STRING, "budget", "Presupuesto", true),

                Commands.slash("eliminar-servicio", "Elimina un servicio/producto del mercado.")
                        .setGuildOnly(true)
                        .addOption(OptionType.STRING, "id", "La ID del mensaje del servicio", true)
        ).queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        User user = event.getUser();
        String command = event.getName();

        switch (command) {
            case "ofrecer-servicio": {
                String title = event.getOption("title").getAsString();
                String description = event.getOption("description").getAsString();
                String details = event.getOption("details").getAsString();
                String price = event.getOption("price").getAsString();

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle(title)
                        .setAuthor(user.getName() + " está ofreciendo un servicio/producto", null, user.getAvatarUrl())
                        .addField("**Descripción**", description, false)
                        .addField("**Detalles**", details, false)
                        .addField("**Precio**", price, false)
                        .setColor(new Color(255, 188, 71 ))
                        .setThumbnail("https://imgur.com/TFKvsf5.png")
                        .setFooter("ID: " + getId());

                event.getChannel().sendMessageEmbeds(builder.build()).queue();
                event.reply("¡Añadido correctamente al mercado!").setEphemeral(true).queue();
                break;
            }

            case "solicitar-servicio": {
                String title = event.getOption("title").getAsString();
                String description = event.getOption("description").getAsString();
                String details = event.getOption("details").getAsString();
                String price = event.getOption("budget").getAsString();

                EmbedBuilder builder = new EmbedBuilder()
                        .setTitle(title)
                        .setAuthor(user.getName() + " está solicitando un servicio/producto", null, user.getAvatarUrl())
                        .addField("**Descripción**", description, false)
                        .addField("**Detalles**", details, false)
                        .addField("**Presupuesto**", price, false)
                        .setColor(new Color(255, 64, 30))
                        .setThumbnail("https://imgur.com/5NkvmB1.png")
                        .setFooter("ID: " + getId());

                event.getChannel().sendMessageEmbeds(builder.build()).queue();
                event.reply("¡Añadido correctamente al mercado!").setEphemeral(true).queue();
                break;
            }

            case "eliminar-servicio": {
                String id = event.getOption("id").getAsString();

                event.getChannel().retrieveMessageById(id).queue(
                        message -> {
                            User author = message.getAuthor();

                            if (!author.getId().equals("1029572096310911047")) {
                                event.reply("¡La ID proporcionada no pertenece al DBCH Bot!").setEphemeral(true).queue();
                                return;
                            }

                            MessageEmbed embed = message.getEmbeds().get(0);
                            MessageEmbed.AuthorInfo authorInfo = embed.getAuthor();
                            String authorName = authorInfo.getName();

                            if (!authorName.startsWith(event.getUser().getName())) {
                                event.reply("¡No eres el dueño del servicio!").setEphemeral(true).queue();
                                return;
                            }

                            event.getChannel().deleteMessageById(message.getId()).queue();
                            event.reply("¡Servicio eliminado correctamente!").setEphemeral(true).queue();
                        },

                        error -> event.reply("¡No fue posible eliminar el servicio!").setEphemeral(true).queue()
                );
            }
        }
    }

    public BigInteger getId() {
        BigInteger min = new BigInteger("000000000000000000");
        BigInteger max = new BigInteger("999999999999999999");
        BigInteger randomBigInt = min.add(new BigInteger(max.bitLength(), new Random()));

        return randomBigInt.mod(max.subtract(min).add(BigInteger.ONE)).add(min);
    }
}