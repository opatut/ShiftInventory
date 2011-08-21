package org.opatut.shiftinventory;

import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerListener;

public class PluginPlayerListener extends PlayerListener {
	public final Plugin mPlugin;
	
	public PluginPlayerListener(Plugin plugin) {
		mPlugin = plugin;
	}
	
	@Override
	public void onItemHeldChange(PlayerItemHeldEvent event) {
		int p = event.getPreviousSlot();
		int n = event.getNewSlot();
		int d = p - n;
		
		if(event.getPlayer().isSneaking() && mPlugin.IsEnabledFor(event.getPlayer())) {
			if(d == 1 || d == -8) {
				// scrolled up
				mPlugin.ShiftInventory(event.getPlayer(), 1);
			} else if (d == -1 || d == 8) {
				// scrolled down
				mPlugin.ShiftInventory(event.getPlayer(), -1);
			}
		}
		
		super.onItemHeldChange(event);
	}
}
