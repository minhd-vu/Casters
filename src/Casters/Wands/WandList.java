package Casters.Wands;

import Casters.CommandInterface;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WandList implements CommandInterface
{
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Wand" + ChatColor.DARK_GRAY + "]"
			+ ChatColor.WHITE + " ";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			if (args.length == 1)
			{
				player.sendMessage(header + ChatColor.AQUA + "Classes With Wands:\n" + ChatColor.DARK_BLUE
						+ "Distorter\n" + ChatColor.DARK_RED + "Inferno\n" + ChatColor.AQUA + "Shaman\n"
						+ ChatColor.DARK_PURPLE + "Warlock\n");
			}
			else
			{
				return false;
			}
		}

		return true;
	}
}
