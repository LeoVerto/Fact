package io.github.LeoVerto.Fact;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Fact extends JavaPlugin {
	HashSet<UUID> playersIgnoring = new HashSet<UUID>();

	@Override
	public void onEnable() {
		loadConfiguration();
		autoFacts();
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		playersIgnoring.clear();
	}

	public void loadConfiguration() {
		reloadConfig();
		getConfig().addDefault("Colors.PlayerFact.Fact", "&7");
		getConfig().addDefault("Colors.PlayerFact.Text", "&f");
		getConfig().addDefault("Colors.ConsoleFact.Fact", "&6");
		getConfig().addDefault("Colors.ConsoleFact.Text", "&f");
		getConfig().addDefault("Colors.AutoFact.Fact", "&3");
		getConfig().addDefault("Colors.AutoFact.Text", "&f");
		getConfig().addDefault("Colors.Player", "&8");
		getConfig().addDefault("Messages.AutoFact.Delay", 5);
		getConfig().addDefault("Messages.AutoFact.Facts",
				Arrays.asList("This is a default autofact.", "Water is the leading cause of drowning!", 
						"You can change autofacts in /plugins/Fact/config.yml",	"All people exposed to water will die!"));
		getConfig().addDefault("Prefixes.Fact", "Fact>");
		getConfig().addDefault("Prefixes.AutoFact", "AutoFact>");
		getConfig().addDefault("Messages.Ignore.Ignoring", ("No longer displaying Fact messages!"));
		getConfig().addDefault("Messages.Ignore.NotIgnoring", ("Now displaying Fact messages!"));
		getConfig().addDefault("Messages.Reload", ("Reload complete!"));
		getConfig().options().copyDefaults(true);
		//Currently no header
		//getConfig().options().copyHeader(true);
		saveConfig();
	}

	public void autoFacts() {
		final long autoFactDelay = (getConfig().getLong("Messages.AutoFact.Delay") * 1200);
		final List<String> messages = getConfig().getStringList("Messages.AutoFact.Facts");
		final int messageCount = messages.size();
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			private int	messageNumber = 0;

			@Override
			public void run() {
				if (messageNumber < (messageCount)) {
					sendFact((String) messages.get(messageNumber), "auto", "");
					messageNumber++;
				} else {
					messageNumber = 0;
					sendFact((String) messages.get(messageNumber), "auto", "");
					messageNumber++;
				}
			}
		}, 1200L, autoFactDelay);
	}

	public <player> void sendFact(final String message, final String type, final String sender) {
		final String FactColor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Colors.PlayerFact.Fact").replace("'", ""));
		final String TextColor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Colors.PlayerFact.Text").replace("'", ""));
		final String ConsoleFactColor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Colors.ConsoleFact.Fact").replace("'", ""));
		final String ConsoleTextColor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Colors.ConsoleFact.Text").replace("'", ""));
		final String AutoFactColor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Colors.AutoFact.Fact").replace("'", ""));
		final String AutoTextColor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Colors.AutoFact.Text").replace("'", ""));
		final String PlayerColor = ChatColor.translateAlternateColorCodes('&', getConfig().getString("Colors.Player").replace("'", ""));
		final String FactPrefix = getConfig().getString("Prefixes.Fact");
		final String AutoFactPrefix = getConfig().getString("Prefixes.AutoFact");
		final Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers();
		
		for (Player player: onlinePlayers) {
			if (!playersIgnoring.contains(player.getUniqueId())) {
				if (player.hasPermission("fact.receive")) {
					if (type.equals("player")) {
						if (player.hasPermission("fact.spy") && !sender.equals("")) {
							player.sendMessage(PlayerColor + "(" + sender + ")" + FactColor + FactPrefix + " " + TextColor + message);
						} else {
							player.sendMessage(FactColor + FactPrefix + " " + TextColor + message);
						}
					} else if (type.equals("auto")) {
						player.sendMessage(AutoFactColor + AutoFactPrefix + " " + AutoTextColor + message);
					} else {
						player.sendMessage(ConsoleFactColor + FactPrefix + " " + ConsoleTextColor + message);
					}
				}
			}
		}
		if (!type.equals("auto")) {
			if (type.equals("player") && !sender.equals("")) {
				getLogger().info("(" + sender + ")" + FactPrefix + " " + message);
			} else {
				getLogger().info(FactPrefix + " " + message);
			}
		}
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("fact")) {
			if (args.length >= 1) {
				//Reload command
				if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
					if ((sender instanceof Player)) {
						final Player player = (Player) sender;
						if (player.hasPermission("fact.reload")) {
							loadConfiguration();
							getServer().getScheduler().cancelTasks(this);
							autoFacts();
							player.sendMessage(getConfig().getString("Messages.Reload"));
							getLogger().info(getConfig().getString("Messages.Reload"));
						} else {
							player.sendMessage(this.getCommand("fact").getPermissionMessage());
						}
					} else {
						loadConfiguration();
						getLogger().info(getConfig().getString("Messages.Reload"));
					}
					return true;
					
				//Ignore command
				} else if (args.length == 1 && args[0].equalsIgnoreCase("ignore")) {
					if ((sender instanceof Player)) {
						final Player player = (Player) sender;
						final UUID uuid = player.getUniqueId();
						if (player.hasPermission("fact.ignore")) {
							if (playersIgnoring.contains(uuid) == false) {
								playersIgnoring.add(uuid);
								player.sendMessage(getConfig().getString("Messages.Ignore.Ignoring"));
							} else {
								playersIgnoring.remove(uuid);
								player.sendMessage(getConfig().getString("Messages.Ignore.NotIgnoring"));
							}
						} else {
							player.sendMessage(this.getCommand("fact").getPermissionMessage());
						}
					} else {
						sender.sendMessage("You can only execute this command as a player!");
					}
					return true;
					
				//Normal facts
				} else {
					if ((sender instanceof Player)) {
						final Player player = (Player) sender;
						if (player.hasPermission("fact.fact")) {
							String message = "";
							for (int i = 0; i < args.length; i++) {
								message = (message + " " + args[i]);
							}
							sendFact(message, "player", player.getName());
						} else {
							player.sendMessage(this.getCommand("fact").getPermissionMessage());
						}
					} else {
						String message = "";
						for (int i = 0; i < args.length; i++) {
							message = (message + " " + args[i]);
						}
						sendFact(message, "nonplayer", "");
					}
					return true;
				}
			}
		}
		return false;
	}
}
