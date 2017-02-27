package Cast.Casters;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;
import Cast.Essentials.Type;

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
				return false;
			}

			for (Type type : Main.getClasses())
			{
				if (args[1].equalsIgnoreCase(type.getName()))
				{
					caster.setType(type.getName());
					return true;
				}
			}

			for (Type race : Main.getRaces())
			{
				if (args[1].equalsIgnoreCase(race.getName()))
				{
					caster.setRace(race.getName());
					return true;
				}
			}

			for (Type job : Main.getJobs())
			{
				if (args[1].equalsIgnoreCase(job.getName()))
				{
					caster.setJob(job.getName());
					return true;
				}
			}

			player.sendMessage(header + ChatColor.GRAY + "That Class/Race/Job Does Not Exist!");

		}

		return true;
	}
}
