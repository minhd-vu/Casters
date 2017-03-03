package Cast.Casters;

import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CastersStats implements CommandInterface
{
	private String header = ChatColor.DARK_GRAY + "[" + ChatColor.DARK_AQUA + "Casters Stats" + ChatColor.DARK_GRAY
			+ "] ";
	private String fill = "--------------------";
	private String bar = "-----------------------------------------------------";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length == 1)
			{
				player.sendMessage("\n" + ChatColor.DARK_GRAY + fill + header.substring(0, header.length() - 1) + fill
						+ ChatColor.DARK_AQUA + "\nPoints: " + ChatColor.GRAY + caster.getPoints() + ChatColor.DARK_AQUA
						+ "\nStrength: " + ChatColor.GRAY + caster.getStrength() + ChatColor.DARK_AQUA
						+ "\nConstituion: " + ChatColor.GRAY + caster.getConstitution() + ChatColor.DARK_AQUA
						+ "\nDexterity: " + ChatColor.GRAY + caster.getDexterity() + ChatColor.DARK_AQUA
						+ "\nIntellect: " + ChatColor.GRAY + caster.getIntellect() + ChatColor.DARK_AQUA + "\nWisdom: "
						+ ChatColor.GRAY + caster.getWisdom() + "\n" + ChatColor.DARK_GRAY + bar);
			}
			else if (args.length == 2 && args[1].equalsIgnoreCase("reset"))
			{
				caster.setPoints(caster.getPoints() + caster.getStrength() + caster.getConstitution()
						+ caster.getDexterity() + caster.getIntellect() + caster.getWisdom());
				caster.setStrength(0);
				caster.setConstitution(0);
				caster.setDexterity(0);
				caster.setIntellect(0);
				caster.setWisdom(0);
				player.sendMessage(header + ChatColor.GRAY + "Stats Successfully Reset!");
			}
			else if (args.length == 4 && args[1].equalsIgnoreCase("add"))
			{
				int value;

				try
				{
					value = Integer.parseInt(args[3]);
				}
				catch (NumberFormatException e)
				{
					player.sendMessage(header + ChatColor.GRAY + "You Must Input A Number!");
					return false;
				}

				if (value > caster.getPoints())
				{
					player.sendMessage(header + ChatColor.GRAY + "You Do Not Have Enough Points!");
					return false;
				}
				else if (args[2].equalsIgnoreCase("Strength"))
				{
					caster.setPoints(caster.getPoints() - value);
					caster.setStrength(caster.getStrength() + value);
					player.sendMessage(header + ChatColor.GRAY + "Successfully Increased Strength!");
				}
				else if (args[2].equalsIgnoreCase("Constitution"))
				{
					caster.setPoints(caster.getPoints() - value);
					caster.setConstitution(caster.getConstitution() + value);
					player.sendMessage(header + ChatColor.GRAY + "Successfully Increased Constitution!");
				}
				else if (args[2].equalsIgnoreCase("Dexterity"))
				{
					caster.setPoints(caster.getPoints() - value);
					caster.setDexterity(caster.getDexterity() + value);
					player.sendMessage(header + ChatColor.GRAY + "Successfully Increased Dexterity!");
				}
				else if (args[2].equalsIgnoreCase("Intellect"))
				{
					caster.setPoints(caster.getPoints() - value);
					caster.setIntellect(caster.getIntellect() + value);
					player.sendMessage(header + ChatColor.GRAY + "Successfully Increased Intellect!");
				}
				else if (args[2].equalsIgnoreCase("Wisdom"))
				{
					caster.setPoints(caster.getPoints() - value);
					caster.setWisdom(caster.getWisdom() + value);
					player.sendMessage(header + ChatColor.GRAY + "Successfully Increased Wisdom!");
				}
				else
				{
					player.sendMessage(header + ChatColor.GRAY + "You Must Input A Valid Stat!");
					return false;
				}
			}
		}

		return true;
	}

}
