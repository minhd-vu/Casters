package Cast.Casts;

import Cast.Casts.Actives.Active;
import Cast.Casts.Passives.Passive;
import Cast.Casts.Targetted.Targetted;
import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Essentials.Chat.Pages;
import Cast.Essentials.Type;
import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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

				for (Cast cast : Main.getCasts().values())
				{
					String casttype = "";

					if (cast instanceof Active)
					{
						casttype = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Active" + ChatColor.DARK_GRAY
								+ "]";
					}
					else if (cast instanceof Targetted)
					{
						casttype = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Targetted" + ChatColor.DARK_GRAY
								+ "]";
					}
					else if (cast instanceof Passive)
					{
						casttype = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Passive" + ChatColor.DARK_GRAY
								+ "]";
					}

					commands.add(casttype + ChatColor.AQUA + " " + cast.getName() + ChatColor.GRAY + " - "
							+ cast.getDescription());
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
			    /*-for (String cast : c.getCasts().keySet())
				{
					commands.add(ChatColor.DARK_AQUA + "Level: " + ChatColor.GRAY + c.getCasts().get(cast) + " - "
							+ ChatColor.DARK_AQUA + "/cast" + ChatColor.AQUA + " " + cast.toLowerCase() + ChatColor.GRAY
							+ " - " + Main.getCasts().get(cast).getDescription() + ".");
				}*/

				for (Cast cast : Main.getCasts().values())
				{
					if (c.getCasts().containsKey(cast.getName()))
					{
						String casttype = "";

						if (cast instanceof Active)
						{
							casttype = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Active" + ChatColor.DARK_GRAY
									+ "]";
						}
						else if (cast instanceof Targetted)
						{
							casttype = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Targetted"
									+ ChatColor.DARK_GRAY + "]";
						}
						else if (cast instanceof Passive)
						{
							casttype = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Passive" + ChatColor.DARK_GRAY
									+ "]";
						}

						commands.add(casttype + ChatColor.AQUA + " " + cast.getName() + ChatColor.GRAY + " - "
								+ ChatColor.DARK_AQUA + "Level: " + ChatColor.GRAY + c.getCasts().get(cast.getName())
								+ " - " + cast.getDescription());
					}
				}

				break;
			}
		}

		pages.setPage(commands);
	}
}
