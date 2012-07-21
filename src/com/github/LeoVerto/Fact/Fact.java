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
		getLogger().info("Enabled " + this.getDescription().getName() + " version " + this.getDescription().getVersion() + " by " + this.getDescription().getAuthors() + ".");
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled Fact");
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if (cmd.getName().equalsIgnoreCase("fact")) {
			if ((sender instanceof Player)) {
				final Player player = (Player) sender;
				if (player.hasPermission("fact.fact")) {
					String message = "";
					for (int i = 0; i < args.length; i++) {
						message = (message + " " + args[i]);
					}
					Bukkit.getServer().broadcastMessage(ChatColor.GRAY + "Fact> " + ChatColor.WHITE + message);
					return true;
				} else {
					player.sendMessage(this.getCommand("fact").getPermissionMessage());
				}
				return false;
			} else {
				String message = "";
				for (int i = 0; i < args.length; i++) {
					message = (message + " " + args[i]);
				}
				Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "Fact> " + ChatColor.WHITE + message);
				return true;
			}
		}
		return false;
	}
}
