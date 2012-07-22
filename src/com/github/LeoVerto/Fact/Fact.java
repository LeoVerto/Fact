package com.github.LeoVerto.Fact;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Fact extends JavaPlugin {
	@Override
	public void onEnable() {
		loadConfiguration();
		getLogger().info("Enabled " + this.getDescription().getName() + " version " + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors() + ".");
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled Fact");
	}

	public void loadConfiguration() {
		final String path = "Fact.";
		getConfig().addDefault(path + "Colors.PlayerFact.Fact", "'&7'");
		getConfig().addDefault(path + "Colors.PlayerFact.Text", "'&f'");
		getConfig().addDefault(path + "Colors.ConsoleFact.Fact", "'&6'");
		getConfig().addDefault(path + "Colors.ConsoleFact.Text", "'&f'");
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("fact")) {
			if ((sender instanceof Player)) {
				final Player player = (Player) sender;
				if (player.hasPermission("fact.fact")) {
					String FactColor = ChatColor.translateAlternateColorCodes('&',getConfig().getString("Fact.Colors.PlayerFact.Fact").replace("'", ""));
					String TextColor = ChatColor.translateAlternateColorCodes('&',getConfig().getString("Fact.Colors.PlayerFact.Text").replace("'", ""));
					String message = "";
					for (int i = 0; i < args.length; i++) {
						message = (message + " " + args[i]);
					}
					Bukkit.getServer().broadcastMessage(FactColor + "Fact> " + TextColor + message);
					return true;
				} else {
					player.sendMessage(this.getCommand("fact").getPermissionMessage());
				}
				return false;
			} else {
				final String FactColor = ChatColor.translateAlternateColorCodes('&',getConfig().getString("Fact.Colors.ConsoleFact.Fact").replace("'", ""));
				final String TextColor = ChatColor.translateAlternateColorCodes('&',getConfig().getString("Fact.Colors.ConsoleFact.Text").replace("'", ""));
				String message = "";
				for (int i = 0; i < args.length; i++) {
					message = (message + " " + args[i]);
				}
				Bukkit.getServer().broadcastMessage(FactColor + "Fact> " + TextColor + message);
				return true;
			}
		}
		return false;
	}
}
