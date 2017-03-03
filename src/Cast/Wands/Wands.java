package Cast.Wands;

import Cast.CommandInterface;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Wands implements CommandInterface
{
	protected String cheader = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Wand" + ChatColor.DARK_GRAY + "] ";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			player.sendMessage(cheader + ChatColor.AQUA + "Commands:\n" + ChatColor.DARK_AQUA + "/wand" + ChatColor.GRAY
					+ " - Lists All Wand Commands.\n" + ChatColor.DARK_AQUA + "/wand" + ChatColor.AQUA + " <class> info"
					+ ChatColor.GRAY + " - Shows Description And Values.\n" + ChatColor.DARK_AQUA + "/wand"
					+ ChatColor.AQUA + " list" + ChatColor.GRAY + " - Lists All Classes With Wands.\n");
		}

		return true;
	}
}
