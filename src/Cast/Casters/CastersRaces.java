package Cast.Casters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Essentials.Chat.Pages;

public class CastersRaces implements CommandInterface
{
	private Pages pages = new Pages();
	private String fill = "-------------------";
	private String header = ChatColor.DARK_GRAY + "-[" + ChatColor.DARK_AQUA + "Casters Races" + ChatColor.DARK_GRAY
			+ "]";

	private List<String> races = new ArrayList<String>();

	public CastersRaces()
	{
		races.add(ChatColor.DARK_AQUA + "Dwarf" + ChatColor.GRAY + " - Drinker Of Drinks.");
		races.add(ChatColor.DARK_AQUA + "Elf" + ChatColor.GRAY + " - Grower Of Ears.");
		races.add(ChatColor.DARK_AQUA + "Giant" + ChatColor.GRAY + " - Much Tall Of Very Large.");
		races.add(ChatColor.DARK_AQUA + "Goblin" + ChatColor.GRAY + " - Nicer Of Skin Tone.");
		races.add(ChatColor.DARK_AQUA + "Human" + ChatColor.GRAY + " - Becommer Of Basic.");
		races.add(ChatColor.DARK_AQUA + "Orc" + ChatColor.GRAY + " - Bearer Of Muscles.");
		races.add(ChatColor.DARK_AQUA + "Troll" + ChatColor.GRAY + " - Needer Of Braces.");

		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setError("Casters Races");
		pages.setCommand("casters races");
		pages.setPage(races);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			pages.display(player, args, 1);
		}

		return true;
	}

}
