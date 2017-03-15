package Casters.Commands;

import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Essentials.Type;
import Casters.Casters;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CastersChoose implements CommandInterface
{
	private final long timer = 0 /*TimeUnit.MILLISECONDS.convert(1L, TimeUnit.DAYS)*/;

	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "] ";
	private HashMap<UUID, Long> cooldowns = new HashMap<UUID, Long>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length != 2)
			{
				player.sendMessage(header + ChatColor.GRAY + "Correct Usage: " + ChatColor.DARK_AQUA + "/caster"
						+ ChatColor.AQUA + " choose <class/race/job>");
				return true;
			}

			if (!cooldowns.containsKey(player.getUniqueId()) || System.currentTimeMillis() - cooldowns.get(player.getUniqueId()) > timer)
			{
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

				for (Type type : Casters.getClasses())
				{
					if (args[1].equalsIgnoreCase(type.getName()))
					{
						caster.setType(type);

						cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

						return true;
					}
				}

				for (Type race : Casters.getRaces())
				{
					if (args[1].equalsIgnoreCase(race.getName()))
					{
						caster.setRace(race);

						cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

						return true;
					}
				}

				for (Type job : Casters.getJobs())
				{
					if (args[1].equalsIgnoreCase(job.getName()))
					{
						caster.setJob(job);

						cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

						return true;
					}
				}

				player.sendMessage(header + ChatColor.GRAY + "That Class/Race/Job Does Not Exist!");
			}

			else
			{
				player.sendMessage(
						header + ChatColor.GRAY + "You Have " + ChatColor.AQUA +
								TimeUnit.HOURS.convert(timer - (System.currentTimeMillis() - cooldowns.get(player.getUniqueId())), TimeUnit.MILLISECONDS) + " Hours " +
								ChatColor.GRAY + "And " + ChatColor.AQUA +
								TimeUnit.MINUTES.convert(timer - (System.currentTimeMillis() - cooldowns.get(player.getUniqueId())), TimeUnit.MILLISECONDS) % 60 +
								" Minutes " + ChatColor.GRAY + "Left!");
			}
		}

		return true;
	}
}
