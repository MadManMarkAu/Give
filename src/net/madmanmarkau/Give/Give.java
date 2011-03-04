package net.madmanmarkau.Give;

import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

// Permissions:
// if (iStick.Permissions.has(player, "foo.bar")) {}

public class Give extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");
    public PermissionHandler Permissions;
	public Configuration Config;
    public PluginDescriptionFile pdfFile;
	
    private GivePlayerListener playerListener = new GivePlayerListener(this);

	@Override
	public void onDisable() {
	    log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " unloaded");
	}

	@Override
	public void onEnable() {
		this.pdfFile = this.getDescription();

		setupPermissions();
		registerEvents();
		
		log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + " loaded");
	}

	public void setupPermissions() {
		Plugin perm = this.getServer().getPluginManager().getPlugin("Permissions");
			
		if (this.Permissions == null) {
			if (perm!= null) {
				this.getServer().getPluginManager().enablePlugin(perm);
				this.Permissions = ((Permissions) perm).getHandler();
			}
			else {
				log.info(pdfFile.getName() + " version " + pdfFile.getVersion() + "not enabled. Permissions not detected");
				this.getServer().getPluginManager().disablePlugin(this);
			}
		}
	}

	private void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_COMMAND, this.playerListener, Event.Priority.High, this);
	}
	
    public static Material getMaterial(String m) throws IllegalArgumentException {
        try {
        	int itemId = Integer.decode(m);
        	
            Material material = Material.getMaterial(itemId);
            if (material == null) {
            	throw new IllegalArgumentException("Unknown material " + m);
            }
            return material;
        } catch (NumberFormatException ex) {
            Material material = Material.getMaterial(m.toUpperCase());
            if (material == null) {
            	throw new IllegalArgumentException("Unknown material " + m);
            }
            return material;
        }
    }

	public void onPlayerCommand(Player player, String[] params) {
		Material material = null;
		int quantity = 1;
		
		if (params.length == 1) {
			// Print usage
		    Messaging.send(player, "&c/give <item> [<amount>]:");
		    return;
		}
		
		if (params.length >= 2) {
			try {
				material = getMaterial(params[1]);
			} catch (IllegalArgumentException ex) {
				Messaging.send(player, ex.getMessage());
				return;
			}

			if (material == null) {
				Messaging.send(player, "Unknown material " + params[1]);
				return;
			}
		}

//		log.info("> > Material: " + material.getId());

		if (params.length >= 3) {
			try {
	        	quantity = Integer.decode(params[2]);
	        	
				material = getMaterial(params[1]);
			} catch (NumberFormatException ex) {
				Messaging.send(player, "Illegal quantity!");
				return;
			}
		}

//		log.info("> > Quantity: " + quantity);
		
		if (material.getId() == -1 || material.getId() == 0) {
			Messaging.send(player, "Unknown or invalid material " + params[1]);
			return;
		}
		
		if (Permissions.has(player, "give." + material.getId())) {
//			log.info("> > Done!");
			PlayerInventory inventory = player.getInventory();

			inventory.addItem(new ItemStack[] { new ItemStack(material, quantity) });

			Messaging.send(player, "Enjoy c:");
		} else {
//			log.info("> > Failed!");
			Messaging.send(player, "You may not have that item!");
		}
	}
}
