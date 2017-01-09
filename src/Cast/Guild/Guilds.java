package Cast.Guild;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Essentials.Chat.Pages;

public class Guilds implements CommandInterface
{
	private Pages pages = new Pages();
	private List<String> commands = new ArrayList<String>();

	public static String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Guild" + ChatColor.DARK_GRAY + "]";
	public static String fill = ChatColor.DARK_GRAY + "-----------------------";

	public Guilds()
	{
		commands.add(ChatColor.DARK_AQUA + "/guild" + ChatColor.AQUA + " <page>" + ChatColor.GRAY
				+ " - Lists All Guild Commands.");
		commands.add(ChatColor.DARK_AQUA + "/guild" + ChatColor.AQUA + " create <name>" + ChatColor.GRAY
				+ " - Creates A Guild.");
		commands.add(ChatColor.DARK_AQUA + "/guild" + ChatColor.AQUA + " description <description>" + ChatColor.GRAY
				+ " - Set Guild Description.");
		commands.add(
				ChatColor.DARK_AQUA + "/guild" + ChatColor.AQUA + " disband" + ChatColor.GRAY + " - Disbands A Guild.");
		commands.add(ChatColor.DARK_AQUA + "/guild" + ChatColor.AQUA + " invite <name>" + ChatColor.GRAY
				+ " - Add A Guild Member.");
		commands.add(ChatColor.DARK_AQUA + "/guild" + ChatColor.AQUA + " remove <name>" + ChatColor.GRAY
				+ " - Remove A Guild Member.");
		commands.add(ChatColor.DARK_AQUA + "/guild" + ChatColor.AQUA + " leave" + ChatColor.GRAY + " - Leave A Guild.");
		commands.add(ChatColor.DARK_AQUA + "/guild" + ChatColor.AQUA + " toggle pvp <on/off>" + ChatColor.GRAY
				+ " - Toggles PVP.");
		commands.add(ChatColor.DARK_AQUA + "/guild" + ChatColor.AQUA + " info <name> <page>" + ChatColor.GRAY
				+ " - List Members In Guild.");
		commands.add(ChatColor.DARK_AQUA + "/guild" + ChatColor.AQUA + " who <name>" + ChatColor.GRAY
				+ " - List A Player's Guild.");
		commands.add(ChatColor.DARK_AQUA + "/guild" + ChatColor.AQUA + " setrank <rank>" + ChatColor.GRAY
				+ " - Set A Player's Rank.");

		pages.setHeader(fill + "-" + header + fill);
		pages.setError("Guild");
		pages.setCommand("guild");
		pages.setPage(commands);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			pages.display(player, args, 0);
		}

		return true;
	}
}
