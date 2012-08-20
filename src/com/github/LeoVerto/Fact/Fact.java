package com.github.LeoVerto.Fact;

import java.util.Arrays;
import java.util.List;

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
		getLogger().info("Enabled " + this.getDescription().getName() + " v" + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors() + ".");
		autoFacts();
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		getLogger().info("Disabled " + this.getDescription().getName());
	}

	public void loadConfiguration() {
		final String path = "Fact.";
		reloadConfig();
		getConfig().addDefault(path + "Colors.PlayerFact.Fact", "'&7'");
		getConfig().addDefault(path + "Colors.PlayerFact.Text", "'&f'");
		getConfig().addDefault(path + "Colors.ConsoleFact.Fact", "'&6'");
		getConfig().addDefault(path + "Colors.ConsoleFact.Text", "'&f'");
		getConfig().addDefault(path + "Colors.AutoFact.Fact", "'&3'");
		getConfig().addDefault(path + "Colors.AutoFact.Text", "'&f'");
		getConfig().addDefault(path + "Messages.AutoFact.Delay", 5);
		getConfig().addDefault(path + "Messages.AutoFact.Facts",
				Arrays.asList("This is a default fact.", "You can change autofacts in /plugins/Fact/config.yml", "Default stuff is usually bad, so please change this!"));
		getConfig().addDefault(path + "Text.Fact", "Fact>");
		getConfig().addDefault(path + "Text.AutoFact", "AutoFact>");
		getConfig().options().copyDefaults(true);
		//Currently no header
		//getConfig().options().copyHeader(true);
		saveConfig();
	}

	public void autoFacts() {
		
		final long autoFactDelay = (getConfig().getLong("Fact.Messages.AutoFact.Delay") * 1200);
		final List<?>	messages		= getConfig().getList("Fact.Messages.AutoFact.Facts");
		final int		messageCount	= messages.size();
		final String	FactColor		= ChatColor.translateAlternateColorCodes('&', getConfig().getString("Fact.Colors.AutoFact.Fact").replace("'", ""));
		final String	TextColor		= ChatColor.translateAlternateColorCodes('&', getConfig().getString("Fact.Colors.AutoFact.Text").replace("'", ""));
		final String	AutoFactText	= getConfig().getString("Fact.Text.AutoFact");

		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			private int	messageNumber	= 0;

			@Override
			public void run() {
				if (messageNumber < (messageCount)) {
					Bukkit.getServer().broadcastMessage(FactColor + AutoFactText + " " + TextColor + messages.get(messageNumber));
					messageNumber++;
				} else {
					messageNumber = 0;
					Bukkit.getServer().broadcastMessage(FactColor + AutoFactText + " " + TextColor + messages.get(messageNumber));
					messageNumber++;
				}
			}
		}, 1200L, autoFactDelay);
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("fact")) {
			if(args[0].equals("reload")) {
				if ((sender instanceof Player)) {
					final Player player = (Player) sender;
					if (player.hasPermission("fact.reload")) {
						loadConfiguration();
						getLogger().info("Reload complete!");
					} else {
						player.sendMessage(this.getCommand("fact").getPermissionMessage());
						return false;
					}
				} else {
					loadConfiguration();
					getLogger().info("Reload complete!");
				}
			} else {
				if ((sender instanceof Player)) {
					final Player player = (Player) sender;
					if (player.hasPermission("fact.fact")) {
						final String FactColor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Fact.Colors.PlayerFact.Fact").replace("'", ""));
						final String TextColor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Fact.Colors.PlayerFact.Text").replace("'", ""));
						final String FactText  = getConfig().getString("Fact.Text.Fact");
						String message = "";
						for (int i = 0; i < args.length; i++) {
							message = (message + " " + args[i]);
						}
						Bukkit.getServer().broadcastMessage(FactColor + FactText + " " + TextColor + message);
						return true;
					} else {
						player.sendMessage(this.getCommand("fact").getPermissionMessage());
						return false;
					}
				} else {
					final String FactColor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Fact.Colors.ConsoleFact.Fact").replace("'", ""));
					final String TextColor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Fact.Colors.ConsoleFact.Text").replace("'", ""));
					final String FactText  = getConfig().getString("Fact.Text.Fact");
					String message = "";
					for (int i = 0; i < args.length; i++) {
						message = (message + " " + args[i]);
					}
					Bukkit.getServer().broadcastMessage(FactColor + FactText + " " + TextColor + message);
					return true;
				}
			}
		}
		return false;
	}
}
