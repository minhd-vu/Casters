package Cast.Casters;

import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

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

			message += caster.getType().getName() + ": " + ChatColor.GRAY + caster.getTypeLevel() + "/" + caster.getTypeMaxLevel() + " - ";

			if (caster.getTypeLevel() != caster.getTypeMaxLevel())
			{
				message += Double.parseDouble(new DecimalFormat("##.#").format(caster.getTypeExp() / caster.getTypeMaxExp() * 100)) + "%";
			}

			else
			{
				message += "100%";
			}

			message += ChatColor.DARK_AQUA + "\n" + caster.getRace().getName() + ": " + ChatColor.GRAY + caster.getRaceLevel()
					+ "/" + caster.getRaceMaxLevel() + " - ";

			if (caster.getRaceLevel() != caster.getRaceMaxLevel())
			{
				message += Double.parseDouble(new DecimalFormat("##.#").format(caster.getRaceExp() / caster.getRaceMaxExp() * 100)) + "%";
			}

			else
			{
				message += "100%";
			}

			message += ChatColor.DARK_AQUA + "\n" + caster.getJob().getName() + ": " + ChatColor.GRAY + caster.getJobLevel() + "/" + caster.getJobMaxLevel() + " - ";

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
