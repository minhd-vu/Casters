package Cast.Casters;

import Cast.CommandInterface;
import Cast.Essentials.Type;
import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CastersInfo implements CommandInterface
{
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters Info" + ChatColor.DARK_GRAY
			+ "] ";
	private String fill = "--------------------";
	private String bar = "-----------------------------------------------------";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;

			if (args.length != 2)
			{
				player.sendMessage(header + ChatColor.GRAY + "Correct Usage: " + ChatColor.DARK_AQUA + "/caster"
						+ ChatColor.AQUA + " info <type>");
				return false;
			}

			for (Type c : Main.getClasses())
			{
				if (c.getName().equalsIgnoreCase(args[1]))
				{
					player.sendMessage("\n" + ChatColor.DARK_GRAY + fill + "-"
							+ header.substring(0, header.length() - 1) + fill + "\n" + ChatColor.DARK_AQUA + c.getName()
							+ ChatColor.GRAY + " - " + c.getDescription() + "." + ChatColor.DARK_AQUA + "\nBase Stats:"
							+ ChatColor.DARK_AQUA + "\nStrength: " + ChatColor.GRAY + c.getStrength()
							+ ChatColor.DARK_AQUA + "\nConstitution: " + ChatColor.GRAY + c.getConstitution()
							+ ChatColor.DARK_AQUA + "\nDexterity: " + ChatColor.GRAY + c.getDexterity()
							+ ChatColor.DARK_AQUA + "\nIntellect: " + ChatColor.GRAY + c.getIntellect()
							+ ChatColor.DARK_AQUA + "\nWisdom: " + ChatColor.GRAY + c.getWisdom() + "\n"
							+ ChatColor.DARK_GRAY + bar);

					return true;
				}
			}

			player.sendMessage(header + ChatColor.GRAY + "That Class/Race/Job Does Not Exist!");
		}

		return false;
	}

}
