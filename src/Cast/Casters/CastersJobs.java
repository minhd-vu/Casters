package Cast.Casters;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Type;
import Cast.Essentials.Chat.Pages;

public class CastersJobs implements CommandInterface
{
	private Pages pages = new Pages();
	private String fill = "--------------------";
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters Jobs" + ChatColor.DARK_GRAY
			+ "]";

	private List<String> jobs = new ArrayList<String>();

	public CastersJobs()
	{
		for (Type job : Main.getJobs())
		{
			jobs.add(ChatColor.DARK_AQUA + job.getName() + ChatColor.GRAY + " - " + job.getDescription() + ".");
		}

		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setError("Casters Jobs");
		pages.setCommand("casters jobs");
		pages.setPage(jobs);
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
