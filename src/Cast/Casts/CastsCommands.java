package Cast.Casts;

import Cast.CommandInterface;
import Cast.Essentials.Chat.Pages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CastsCommands implements CommandInterface
{
	private Pages pages = new Pages();
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast" + ChatColor.DARK_GRAY + "]";
	private String fill = "------------------------";
	private List<String> commands = new ArrayList<String>();

	public CastsCommands()
	{
		commands.add(ChatColor.DARK_AQUA + "/cast" + ChatColor.AQUA + " <page>" + ChatColor.GRAY
				+ " - Lists All Cast Commands.");
		commands.add(ChatColor.DARK_AQUA + "/casts" + ChatColor.GRAY
				+ " - Displays Casts Inventory.");
		commands.add(ChatColor.DARK_AQUA + "/casts" + ChatColor.AQUA + "info <page>" + ChatColor.GRAY
				+ " - Instructions For How To Use Binded Casts.");
		commands.add(ChatColor.DARK_AQUA + "/cast" + ChatColor.AQUA + " list <page>" + ChatColor.GRAY
				+ " - List Availiable Casts");
		commands.add(ChatColor.DARK_AQUA + "/cast" + ChatColor.AQUA + " list <class> <page>" + ChatColor.GRAY
				+ " - Shows All Casts For That Class.");
		commands.add(ChatColor.DARK_AQUA + "/cast" + ChatColor.AQUA + " <cast> info" + ChatColor.GRAY
				+ " - Shows Description And Values.");

		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setError("Cast");
		pages.setCommand("cast");
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
