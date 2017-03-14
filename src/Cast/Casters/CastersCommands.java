package Cast.Casters;

import Cast.CommandInterface;
import Cast.Essentials.Chat.Pages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CastersCommands implements CommandInterface
{
	private Pages pages = new Pages();
	private List<String> commands = new ArrayList<String>();

	private String header = ChatColor.DARK_GRAY + "-[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "]";
	private String fill = ChatColor.DARK_GRAY + "----------------------";

	public CastersCommands()
	{
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " <page>" + ChatColor.GRAY
				+ " - Lists All Casters Commands.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " info <type>" + ChatColor.GRAY
				+ " - Shows Information.");
		commands.add(
				ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " level" + ChatColor.GRAY + " - Shows All Levels.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " stats" + ChatColor.GRAY + " - Shows Stats.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " stats reset" + ChatColor.GRAY
				+ " - Resets All Stats.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " stats add <stat> <amount>" + ChatColor.GRAY
				+ " - Adds Stat.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " classes <page>" + ChatColor.GRAY
				+ " - Lists All Classes.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " races <page>" + ChatColor.GRAY
				+ " - Lists All Races.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " jobs <page>" + ChatColor.GRAY
				+ " - Lists All Jobs.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " choose <type>" + ChatColor.GRAY
				+ " - Chooses A Class/Race/Job.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " armor <page>" + ChatColor.GRAY
				+ " - Shows All Availiable Armor.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " weapon <page>" + ChatColor.GRAY
				+ " - Shows All Availiable Weapons.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " recipes" + ChatColor.GRAY
				+ " - Shows All Custom Craft Recipes.");
		commands.add(ChatColor.DARK_AQUA + "/casters" + ChatColor.AQUA + " whois" + ChatColor.GRAY
				+ " - Shows A Player's Information.");

		pages.setHeader(fill + header + fill);
		pages.setError("Casters");
		pages.setCommand("casters");
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
