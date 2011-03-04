package net.madmanmarkau.Give;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;

public class GivePlayerListener extends PlayerListener{
	public static Give plugin;
	
	public GivePlayerListener(Give instance) {
		plugin = instance;
	}
	
	// TODO: This will need to be updated to the latest Bukkit command method.
	public void onPlayerCommand(PlayerChatEvent event) {
		if (event.getEventName().equals("PLAYER_COMMAND")) {
			String[] params = event.getMessage().split(" ");
			
//			plugin.log.info("> Message: " + event.getMessage());
//			plugin.log.info("> Command: " + params[0]);
//			plugin.log.info("> Perms: " + plugin.Permissions.has(event.getPlayer(), "give"));
			
			if (params[0].equals("/give") && plugin.Permissions.has(event.getPlayer(), "give")) {
				plugin.onPlayerCommand(event.getPlayer(), params);

//				plugin.log.info("> Done!");

				event.setCancelled(true);
			}
		}
	}
}
