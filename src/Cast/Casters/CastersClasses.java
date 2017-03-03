package Cast.Casters;

import Cast.CommandInterface;
import Cast.Essentials.Chat.Pages;
import Cast.Essentials.Type;
import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CastersClasses implements CommandInterface
{
	private Pages pages = new Pages();
	private String fill = "-------------------";
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters Classes" + ChatColor.DARK_GRAY
			+ "]";
	private List<String> classes = new ArrayList<String>();

	public CastersClasses()
	{
		for (Type type : Main.getClasses())
		{
			classes.add(ChatColor.DARK_AQUA + type.getName() + ChatColor.GRAY + " - " + type.getDescription() + ".");
		}

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
