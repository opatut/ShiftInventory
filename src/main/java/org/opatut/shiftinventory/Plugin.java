
package org.opatut.shiftinventory;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;

/**
 * Plugin for bukkit.
 *
 * @author opatut
 */
public class Plugin extends JavaPlugin {
    private PluginPlayerListener mPluginPlayerListener = new PluginPlayerListener(this);
	private PluginCommands mCommands = new PluginCommands(this);
	
	public HashMap<String, Boolean> PlayersEnabled = new HashMap<String, Boolean>();
	
	public boolean IsEnabledFor(Player player) {
		return PlayersEnabled.containsKey(player.getName())
				? PlayersEnabled.get(player.getName()) : false;
	}
	
	public void SetEnabledFor(Player player, boolean on) {
		PlayersEnabled.put(player.getName(), on);
		if(on) {
			player.sendMessage(ChatColor.GREEN + "Enabled inventory shifting. Sneak and scroll to shift inventory.");
		} else {
			player.sendMessage(ChatColor.GREEN + "Disabled inventory shifting.");
		}
		
	}
	
    public void onDisable() {
    	for(String k: PlayersEnabled.keySet()) {
    		getConfiguration().setProperty("players." + k, PlayersEnabled.get(k));
    	}
    	getConfiguration().save();
    }
    
    public void onEnable() {
        // load saved settings
    	getConfiguration().load();
    	if(getConfiguration().getKeys().contains("players")) {
	    	for(String k: getConfiguration().getKeys("players")) {
	    		PlayersEnabled.put(k, getConfiguration().getBoolean("players." + k, false));
	    	}
    	}
    	System.out.println("ShiftInventory: Loaded " + PlayersEnabled.size() + " player settings.");

        // Register our events
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.PLAYER_ITEM_HELD, mPluginPlayerListener, Priority.Normal, this);
        
        // Register our commands
        getCommand("shiftinventory").setExecutor(mCommands);

        // EXAMPLE: Custom code, here we just output some info so we can check all is well
        PluginDescriptionFile pdfFile = this.getDescription();
        System.out.println( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled!" );
    }
    
    public void ShiftInventory(Player player, int d) {
    	PlayerInventory inv = player.getInventory();
    	
    	// shifting backwards = shifting forwards around
    	while(d < 0) 
    		d += 4;
    	
    	// shift d times
    	for(int x = 0; x < d; ++x) {
	    	// columns
	    	for(int i = 0; i < 9; ++i) {
	    		
	    		// Inventory slot IDs:
	    		// 09 10 11 12 13 14 15 16 17
	    		// 18 19 20 21 22 23 24 25 26
	    		// 27 28 29 30 31 32 33 34 35
	    		// 00 01 02 03 04 05 06 07 08
	    		
	    		// remember first slot
	    		ItemStack first = inv.getItem(i + 0);
	    		MoveSlot(inv, i + 9, i + 0);
	    		MoveSlot(inv, i + 18, i + 9);
	    		MoveSlot(inv, i + 27, i + 18);
	    		if(first.getTypeId() != 0)
	    			inv.setItem(i + 27, first);
	        	else
	        		inv.clear(i + 27);
	    	}
    	}
    }
    
    public void MoveSlot(PlayerInventory inv, int from, int to) {
    	if(inv.getItem(from).getTypeId() != 0) 
    		inv.setItem(to, inv.getItem(from));
    	else
    		inv.clear(to);
    	
    }

}
