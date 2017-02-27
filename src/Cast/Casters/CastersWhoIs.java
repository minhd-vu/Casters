package Cast.Casters;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class CastersWhoIs implements CommandInterface
{
	private String header = ChatColor.DARK_GRAY + "-[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY + "]";
	private String fill = ChatColor.DARK_GRAY + "----------------------";
	private String bar = "-----------------------------------------------------";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			if (args.length != 2)
			{
				return false;
			}

			if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(args[1])))
			{
				player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters" + ChatColor.DARK_GRAY
						+ "]" + ChatColor.GRAY + "That Player Is Not Online!");
			}

			else
			{
				Caster caster = Main.getCasters().get(Bukkit.getPlayer(args[1]).getUniqueId());
				player.sendMessage("\n" + fill + header + fill + "\n" + ChatColor.DARK_AQUA + args[1] + "\n"
						+ ChatColor.DARK_AQUA + "Class: " + ChatColor.AQUA + caster.getType() + " - "
						+ caster.getTypeLevel() + "/" + caster.getRaceMaxLevel() + "\n" + ChatColor.DARK_AQUA + "Race: "
						+ ChatColor.AQUA + caster.getRace() + " - " + caster.getRaceLevel() + "/"
						+ caster.getRaceMaxLevel() + "\n" + ChatColor.DARK_AQUA + "Job: " + ChatColor.AQUA
						+ caster.getJob() + " - " + caster.getJobLevel() + "/" + caster.getJobMaxLevel() + "\n"
						+ ChatColor.DARK_GRAY + bar);
			}
		}

		return true;
	}

}
