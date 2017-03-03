package Cast.Casters;

import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Essentials.Type;
import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CastersChoose implements CommandInterface
{
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "] ";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length != 2)
			{
				player.sendMessage(header + ChatColor.GRAY + "Correct Usage: " + ChatColor.DARK_AQUA + "/caster"
						+ ChatColor.AQUA + " choose <class/race/job>");
				return true;
			}

			if (caster.getType().getName().equalsIgnoreCase(args[1]))
			{
				player.sendMessage(header + ChatColor.GRAY + "You Are Already The Class: " + ChatColor.WHITE
						+ caster.getType().getName() + ChatColor.GRAY + ".");
				return true;
			}
			else if (caster.getRace().getName().equalsIgnoreCase(args[1]))
			{
				player.sendMessage(header + ChatColor.GRAY + "You Are Already The Race: " + ChatColor.WHITE
						+ caster.getRace().getName() + ChatColor.GRAY + ".");
				return true;
			}
			else if (caster.getJob().getName().equalsIgnoreCase(args[1]))
			{
				player.sendMessage(header + ChatColor.GRAY + "You Are Already The Job: " + ChatColor.WHITE
						+ caster.getJob().getName() + ChatColor.GRAY + ".");
				return true;
			}

			for (Type type : Main.getClasses())
			{
				if (args[1].equalsIgnoreCase(type.getName()))
				{
					caster.setType(type);
					return true;
				}
			}

			for (Type race : Main.getRaces())
			{
				if (args[1].equalsIgnoreCase(race.getName()))
				{
					caster.setRace(race);
					return true;
				}
			}

			for (Type job : Main.getJobs())
			{
				if (args[1].equalsIgnoreCase(job.getName()))
				{
					caster.setJob(job);
					return true;
				}
			}

			player.sendMessage(header + ChatColor.GRAY + "That Class/Race/Job Does Not Exist!");

		}

		return true;
	}
}
