import { 
    User, EmbedBuilder, ButtonBuilder, ButtonInteraction, ButtonStyle,
    ActionRowBuilder, MessageActionRowComponentBuilder,
    ApplicationCommandOptionType, CommandInteraction,
} from "discord.js";

import { Discord, Slash, SlashGroup, ButtonComponent, SlashOption } from "discordx";

@Discord()
@SlashGroup({ description: "Solicita y ofrece servicios/productos en el mercado", name: "servicio" })
@SlashGroup("servicio")
class ServiceGroup {

  private static createComponents(): ActionRowBuilder<MessageActionRowComponentBuilder> {
    return new ActionRowBuilder<MessageActionRowComponentBuilder>().addComponents(
      new ButtonBuilder()
        .setLabel("Me Interesa")
        .setStyle(ButtonStyle.Secondary)
        .setEmoji("✔️")
        .setCustomId("dbch-service-info"),

      new ButtonBuilder()
        .setLabel("Eliminar")
        .setStyle(ButtonStyle.Secondary)
        .setEmoji("❌")
        .setCustomId("dbch-service-delete")
    );
  }

  private static createEmbed(
    type: "ofreciendo" | "solicitando", 
    user: User, 
    title: string, 
    description: string, 
    details: string, 
    thumbnail: string, 
    color: number
  ): EmbedBuilder {
    return new EmbedBuilder({
      title: title,
      author: {
        name: `${user.displayName} está ${type} un servicio/producto`,
        icon_url: (user.avatarURL() ?? "https://imgur.com/O5HLgUe.png").toString(),
      },
      fields: [
        { name: "**Descripción**", value: description, inline: false},
        { name: "**Detalles**", value: details, inline: false}
      ],
      color: color,
      thumbnail: { url: thumbnail },
      footer: {
        text: `ID: ${user.id}`
      }
    });
  }

  private fetchId(interaction: ButtonInteraction): string {
    return interaction.message.embeds[0].footer?.text.substring(4) ?? "000000000000000";
  }

  @ButtonComponent({ id: "dbch-service-delete" })
  private async delete(interaction: ButtonInteraction): Promise<void> {
    if (this.fetchId(interaction) != interaction.user.id) {
      await interaction.reply({ content: "¡No eres dueño del servicio!", ephemeral: true });
      return;
    }

    await interaction.message.delete();
    await interaction.reply({ content: "¡Servicio eliminado correctamente!", ephemeral: true });
  }

  @ButtonComponent({ id: "dbch-service-info" })
  private async info(interaction: ButtonInteraction): Promise<void> {
    if (this.fetchId(interaction) == interaction.user.id) {
      await interaction.reply({ content: "¡Eres dueño del servicio!", ephemeral: true });
      return;
    }

    await interaction.reply({ 
        content: `Si deseas utilizar este servicio, por favor, comunícate con el proveedor: <@${interaction.user.id}>. Recuerda que cualquier actividad fraudulenta, ya sea por parte del proveedor o del cliente, puede ser reportada y será sancionada.`, 
        ephemeral: true  
    });
  }

  @Slash({ name: "ofrecer", description: "Ofrece en el mercado" })
  private async offer(
    @SlashOption({
      name: "title",
      description: "Título",
      required: true,
      type: ApplicationCommandOptionType.String,
    })
    title: string,
    @SlashOption({
      name: "description",
      description: "Descripción",
      required: true,
      type: ApplicationCommandOptionType.String,
    })
    description: string,
    @SlashOption({
      name: "details",
      description: "Detalles",
      required: true,
      type: ApplicationCommandOptionType.String
    })
    details: string,
    @SlashOption({
      name: "price",
      description: "Precio",
      required: true,
      type: ApplicationCommandOptionType.String,
    })
    price: string,
    interaction: CommandInteraction
  ): Promise<void> {
    const builder = ServiceGroup.createEmbed(
      "ofreciendo",
      interaction.user, 
      title, 
      description, 
      details, 
      "https://imgur.com/TFKvsf5.png", 
      16759879
    );
  
    builder.addFields({ name: "**Precio**", value: price, inline: false });
  
    await interaction.reply({ embeds: [builder], components: [ ServiceGroup.createComponents() ] });
  }
  
  @Slash({ name: "solicitar", description: "Solicita en el mercado" })
  private async request(
    @SlashOption({
      name: "title",
      description: "Título",
      required: true,
      type: ApplicationCommandOptionType.String,
    })
    title: string,
    @SlashOption({
      name: "description",
      description: "Descripción",
      required: true,
      type: ApplicationCommandOptionType.String,
    })
    description: string,
    @SlashOption({
      name: "details",
      description: "Detalles",
      required: true,
      type: ApplicationCommandOptionType.String
    })
    details: string,
    @SlashOption({
      name: "budget",
      description: "Presupuesto",
      required: true,
      type: ApplicationCommandOptionType.String,
    })
    budget: string,
    interaction: CommandInteraction
  ): Promise<void> {
    const builder = ServiceGroup.createEmbed(
      "solicitando",
      interaction.user, 
      title, 
      description, 
      details, 
      "https://imgur.com/5NkvmB1.png", 
      16728094
    );
  
    builder.addFields({ name: "**Presupuesto**", value: budget, inline: false });
  
    await interaction.reply({ embeds: [builder], components: [ ServiceGroup.createComponents() ] });
  }

}