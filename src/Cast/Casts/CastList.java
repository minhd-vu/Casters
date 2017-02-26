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

			/*-if (args.length <= 2)
			{
				try
				{
					if (args.length == 2)
					{
						Integer.parseInt(args[1]);
					}
			
					setCommands(caster.getTypeConfig(), caster.getType());
					setCommands(caster.getRaceConfig(), caster.getRace());
					setCommands(caster.getJobConfig(), caster.getJob());
			
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
							+ ChatColor.GRAY + " - " + Main.getCasts().get(cast));
				}
			
				pages.setCommand("cast list all");
				pages.setPage(commands);
				pages.display(player, args, 2);
			
				return true;
			}
			
			for (String type : Caster.types)
			{
				if (args[1].equalsIgnoreCase(type))
				{
					setCommands(caster.getTypeConfig(), type);
					pages.display(player, args, 2);
			
					return true;
				}
			}
			
			for (String race : Caster.races)
			{
				if (args[1].equalsIgnoreCase(race))
				{
					setCommands(caster.getRaceConfig(), race);
					pages.display(player, args, 2);
			
					return true;
				}
			}
			
			for (String job : Caster.jobs)
			{
				if (args[1].equalsIgnoreCase(job))
				{
					setCommands(caster.getJobConfig(), job);
					pages.display(player, args, 2);
			
					return true;
				}
			}*/
		}

		return true;

	}

	private void setCommands(Caster caster)
	{
		/*-pages.setCommand("cast list " + name.toLowerCase());
		
		commands.add(ChatColor.DARK_AQUA + name + " Casts:");
		commands.add(ChatColor.DARK_AQUA + "Level: " + ChatColor.GRAY + config.getInt(name + ".Level." + cast) + " - "
				+ ChatColor.DARK_AQUA + "/cast" + ChatColor.AQUA + " " + cast.toLowerCase() + ChatColor.GRAY + " - "
				+ Main.getCasts().get(cast) + ".");
		
		pages.setPage(commands);*/
	}
}
