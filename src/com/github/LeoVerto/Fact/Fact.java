package com.github.LeoVerto.Fact;

import org.bukkit.plugin.java.JavaPlugin;

public class Fact extends JavaPlugin {
public void onEnable(){ 
	getLogger().info("Enabled Fact by Leo Verto");
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		if ((sender instanceof Player)) {
    	if(cmd.getName().equalsIgnoreCase("fact")){ // If the player typed /basic then do the following...
    		String message = "";
    		for (int i = 0; i < args.length; i++) {
    			message = (message + args[i]);
    		}
    		player.sendMessage("Fact> "+ message);
    		return true;
    	} 
    	return false; 
		} else {
	           sender.sendMessage(ChatColor.RED + "You must be a player!");
	           return false;
	        }
	        Player player = (Player) sender;
	        return false;
	}
    

}
     
public void onDisable(){ 
	getLogger().info("Disabled Fact"); 
}


}
