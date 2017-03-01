package Cast.Casts;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;
import Cast.Essentials.Type;
import Cast.Essentials.Chat.Pages;

public class CastList implements CommandInterface
{
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Cast List" + ChatColor.DARK_GRAY + "]";
	private String fill = "----------------------";
	private Pages pages = new Pages();
	private List<String> commands = new ArrayList<String>();

	public CastList()
	{
		pages.setHeader(ChatColor.DARK_GRAY + fill + header + fill);
		pages.setError("Cast List");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			commands.clear();

			if (args.length <= 2)
			{
				try
				{
					if (args.length == 2)
					{
						Integer.parseInt(args[1]);
					}

					setCommands(caster.getType().getName());
					setCommands(caster.getRace().getName());
					setCommands(caster.getJob().getName());

					pages.setCommand("cast list");
					pages.display(player, args, 1);

					return true;
				}

				catch (NumberFormatException e)
				{

				}
			}

			if (args[1].equalsIgnoreCase("all"))
			{
				commands.add(ChatColor.DARK_AQUA + "All Collective Casts:");

				for (String cast : Main.getCasts().keySet())
				{
					commands.add(ChatColor.DARK_AQUA + "/cast" + ChatColor.AQUA + " " + cast.toLowerCase()
							+ ChatColor.GRAY + " - " + Main.getCasts().get(cast).getDescription());
				}

				pages.setCommand("cast list all");
				pages.setPage(commands);
				pages.display(player, args, 2);

				return true;
			}

			for (Type type : Main.getClasses())
			{
				if (args[1].equalsIgnoreCase(type.getName()))
				{
					setCommands(type.getName());
					pages.display(player, args, 2);

					return true;
				}
			}

			for (Type race : Main.getRaces())
			{
				if (args[1].equalsIgnoreCase(race.getName()))
				{
					setCommands(race.getName());
					pages.display(player, args, 2);

					return true;
				}
			}

			for (Type job : Main.getJobs())
			{
				if (args[1].equalsIgnoreCase(job.getName()))
				{
					setCommands(job.getName());
					pages.display(player, args, 2);

					return true;
				}
			}
		}

		return true;

	}

	private void setCommands(String name)
	{
		pages.setCommand("cast list " + name.toLowerCase());

		commands.add(ChatColor.DARK_AQUA + name + " Casts:");

		for (Type c : Main.getTypes())
		{
			if (c.getName().equalsIgnoreCase(name))
			{
				for (String cast : c.getCasts().keySet())
				{
					commands.add(ChatColor.DARK_AQUA + "Level: " + ChatColor.GRAY + c.getCasts().get(cast) + " - "
							+ ChatColor.DARK_AQUA + "/cast" + ChatColor.AQUA + " " + cast.toLowerCase() + ChatColor.GRAY
							+ " - " + Main.getCasts().get(cast).getDescription() + ".");
				}

				break;
			}
		}

		pages.setPage(commands);
	}
}
