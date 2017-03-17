package Casters.Essentials.Chat;

import Casters.CommandInterface;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatTitles extends Chat implements CommandInterface
{
	private String fill = "---------------------";

	public ChatTitles()
	{
		titles.put("Guardian", ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Paladin" + ChatColor.DARK_GRAY + "]");
		titles.put("Cavalier", ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Cavalier" + ChatColor.DARK_GRAY + "]");
		titles.put("Barbarian", ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Barbarian" + ChatColor.DARK_GRAY + "]");
		titles.put("Blackguard", ChatColor.DARK_GRAY + "[" + ChatColor.RED + "Blackguard" + ChatColor.DARK_GRAY + "]");
		titles.put("Assassin", ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Assassin" + ChatColor.DARK_GRAY + "]");
		titles.put("Duelist", ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Duelist" + ChatColor.DARK_GRAY + "]");
		titles.put("Fletcher", ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Fletcher" + ChatColor.DARK_GRAY + "]");
		titles.put("Musketeer", ChatColor.DARK_GRAY + "[" + ChatColor.GREEN + "Musketeer" + ChatColor.DARK_GRAY + "]");
		titles.put("Distorter", ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Distorter" + ChatColor.DARK_GRAY + "]");
		titles.put("Inferno", ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Inferno" + ChatColor.DARK_GRAY + "]");
		titles.put("Shaman", ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Shaman" + ChatColor.DARK_GRAY + "]");
		titles.put("Warlock", ChatColor.DARK_GRAY + "[" + ChatColor.BLUE + "Warlock" + ChatColor.DARK_GRAY + "]");
		titles.put("Oracle", ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Oracle" + ChatColor.DARK_GRAY + "]");
		titles.put("Bloodmage", ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Bloodmage" + ChatColor.DARK_GRAY + "]");
		titles.put("Monk", ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Monk" + ChatColor.DARK_GRAY + "]");
		titles.put("Templar", ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW + "Cleric" + ChatColor.DARK_GRAY + "]");

		pages.setHeader(ChatColor.DARK_GRAY + fill + "-[" + ChatColor.DARK_AQUA + "Chat Titles" + ChatColor.DARK_GRAY
				+ "]" + fill);
		pages.setError("Chat Titles");
		pages.setCommand("chat titles");
		pages.setPage(titles);
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
