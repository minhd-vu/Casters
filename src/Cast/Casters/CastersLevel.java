package Cast.Casters;

import java.text.DecimalFormat;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class CastersLevel implements CommandInterface
{
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters Level" + ChatColor.DARK_GRAY
			+ "]";
	private String fill = "--------------------";
	private String bar = "-----------------------------------------------------";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length > 1)
			{
				return false;
			}
			
			String message = "\n" + ChatColor.DARK_GRAY + fill + header + fill + ChatColor.DARK_AQUA;
			
			/*
			player.sendMessage("\n" + ChatColor.DARK_GRAY + fill + header + fill + ChatColor.DARK_AQUA + "\n"
					+ caster.getType() + ": " + ChatColor.GRAY + caster.getTypeLevel() + "/" + caster.getTypeMaxLevel()
					+ " - "
					+ Double.parseDouble(new DecimalFormat("##.#").format(caster.getTypeExp() / caster.getTypeMaxExp() * 100))
					+ "%" + ChatColor.DARK_AQUA + "\n" + caster.getRace() + ": " + ChatColor.GRAY
					+ caster.getRaceLevel() + "/" + caster.getRaceMaxLevel() + " - "
					+ Double.parseDouble(new DecimalFormat("##.#").format(caster.getRaceExp() / caster.getRaceMaxExp() * 100))
					+ "%" + ChatColor.DARK_AQUA + "\n" + caster.getJob() + ": " + ChatColor.GRAY + caster.getJobLevel()
					+ "/" + caster.getJobMaxLevel() + " - "
					+ Double.parseDouble(new DecimalFormat("##.#").format(caster.getJobExp() / caster.getJobMaxExp() * 100))
					+ "%\n" + ChatColor.DARK_GRAY + bar);
			*/
			
			message += caster.getType() + ": " + ChatColor.GRAY + caster.getTypeLevel() + "/"
					+ caster.getTypeMaxLevel() + " - ";

			if (caster.getTypeLevel() != caster.getTypeMaxLevel())
			{
				message += Double.parseDouble(
						new DecimalFormat("##.#").format(caster.getTypeExp() / caster.getTypeMaxExp() * 100)) + "%";
			}

			else
			{
				message += "100%";
			}

			message += ChatColor.DARK_AQUA + "\n" + caster.getRace() + ": " + ChatColor.GRAY + caster.getRaceLevel()
					+ "/" + caster.getRaceMaxLevel() + " - ";

			if (caster.getRaceLevel() != caster.getRaceMaxLevel())
			{
				message += Double.parseDouble(
						new DecimalFormat("##.#").format(caster.getRaceExp() / caster.getRaceMaxExp() * 100)) + "%";
			}

			else 
			{
				message += "100%";
			}
			
			message += ChatColor.DARK_AQUA + "\n" + caster.getJob() + ": " + ChatColor.GRAY + caster.getJobLevel()
			+ "/" + caster.getJobMaxLevel() + " - ";
			
			if (caster.getJobLevel() != caster.getJobMaxLevel())
			{
				message += Double.parseDouble(new DecimalFormat("##.#").format(caster.getJobExp() / caster.getJobMaxExp() * 100)) + "%";
			}
			
			else
			{
				message += "100%";
			}
			
			message += "\n" + ChatColor.DARK_GRAY + bar;
			
			player.sendMessage(message);
		}

		return true;
	}

}
