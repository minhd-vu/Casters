package Cast.Party;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Essentials.Chat.Pages;

public class Parties implements CommandInterface
{
	private Pages pages = new Pages();
	private List<String> commands = new ArrayList<String>();

	private String header = ChatColor.DARK_GRAY + "-[" + ChatColor.DARK_AQUA + "Party" + ChatColor.DARK_GRAY + "]";
	private String fill = ChatColor.DARK_GRAY + "-----------------------";

	public Parties()
	{
		commands.add(ChatColor.DARK_AQUA + "/party" + ChatColor.AQUA + " <page>" + ChatColor.GRAY + " - Lists All Party Commands.");
		commands.add(ChatColor.DARK_AQUA + "/party" + ChatColor.AQUA + " create" + ChatColor.GRAY + " - Creates A Party.");
		commands.add(ChatColor.DARK_AQUA + "/party" + ChatColor.AQUA + " members" + ChatColor.GRAY + " - Shows People In Party.");
		commands.add(ChatColor.DARK_AQUA + "/party" + ChatColor.AQUA + " invite <name>" + ChatColor.GRAY + " - Invites A Player To The Party.");
		commands.add(ChatColor.DARK_AQUA + "/party" + ChatColor.AQUA + " remove <name>" + ChatColor.GRAY + " - Removes A Player From The Party.");
		commands.add(ChatColor.DARK_AQUA + "/party" + ChatColor.AQUA + " leader <name>" + ChatColor.GRAY + " - Sets The Party Leader.");
		commands.add(ChatColor.DARK_AQUA + "/party" + ChatColor.AQUA + " disband" + ChatColor.GRAY + " - Disbands The Party.");
		commands.add(ChatColor.DARK_AQUA + "/party" + ChatColor.AQUA + " accept" + ChatColor.GRAY + " - Accepts The Party Invite.");
		commands.add(ChatColor.DARK_AQUA + "/party" + ChatColor.AQUA + " decline" + ChatColor.GRAY + " - Declines The Party Invite.");

		pages.setHeader(fill + header + fill);
		pages.setError("Party");
		pages.setCommand("party");
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
