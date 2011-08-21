package org.opatut.shiftinventory;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PluginCommands implements CommandExecutor {
	public final Plugin mPlugin;
	
	public PluginCommands(Plugin plugin) {
		mPlugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "ShiftInventory can only be controlled by in-game players.");
			return true;
		}
		
		Player player = (Player)sender;
		if(args.length == 0) {
			mPlugin.SetEnabledFor(player, !mPlugin.IsEnabledFor(player));
			return true;
		}
		
		String cmd = args[0].toLowerCase();
		
		if(cmd.equals("up") || cmd.equals("+")) {
			mPlugin.ShiftInventory(player, 1);
		} else if(cmd.equals("down") || cmd.equals("-")) {
			mPlugin.ShiftInventory(player, -1);
		} else if(cmd.equals("on")) {
			mPlugin.SetEnabledFor(player, true);
		} else if(cmd.equals("off")) {
			mPlugin.SetEnabledFor(player, false);
		}
	
		return true;
	}
}
