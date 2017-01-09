package Cast.Casters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Essentials.Chat.Pages;

public class CastersClasses implements CommandInterface
{
	private Pages pages = new Pages();
	private String fill = "-------------------";
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters Classes" + ChatColor.DARK_GRAY
			+ "]";
	private List<String> classes = new ArrayList<String>();

	public CastersClasses()
	{
		classes.add(ChatColor.GREEN + "Assassin" + ChatColor.GRAY + " - Dealer Of Damage.");
		classes.add(ChatColor.RED + "Barbarian" + ChatColor.GRAY + " - Swinger Of Axes.");
		classes.add(ChatColor.RED + "Blackguard" + ChatColor.GRAY + " - Bringer Of Darkness.");
		classes.add(ChatColor.YELLOW + "Bloodmage" + ChatColor.GRAY + " - Controller Of Blood.");
		classes.add(ChatColor.RED + "Cavalier" + ChatColor.GRAY + " - Rider Of Horses.");
		classes.add(ChatColor.BLUE + "Distorter" + ChatColor.GRAY + " - Manipulator Of Reality.");
		classes.add(ChatColor.GREEN + "Duelist" + ChatColor.GRAY + " - Wielder Of Weapons.");
		classes.add(ChatColor.GREEN + "Fletcher" + ChatColor.GRAY + " - Shooter Of Arrows.");
		classes.add(ChatColor.RED + "Guardian" + ChatColor.GRAY + " - Protector Of All.");
		classes.add(ChatColor.BLUE + "Inferno" + ChatColor.GRAY + " - Master Of Fire");
		classes.add(ChatColor.YELLOW + "Monk" + ChatColor.GRAY + " - Fighter Of Values.");
		classes.add(ChatColor.GREEN + "Musketeer" + ChatColor.GRAY + " - Firer Of Firearms.");
		classes.add(ChatColor.YELLOW + "Oracle" + ChatColor.GRAY + " - Predictor Of Future.");
		classes.add(ChatColor.BLUE + "Shaman" + ChatColor.GRAY + " - Influencer Of Dark Spirits.");
		classes.add(ChatColor.BLUE + "Warlock" + ChatColor.GRAY + " - Conductor Of Spirits");
		classes.add(ChatColor.YELLOW + "Templar" + ChatColor.GRAY + " - Doer Of Holy Works.");

		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setError("Casters Classes");
		pages.setCommand("casters classes");
		pages.setPage(classes);
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
