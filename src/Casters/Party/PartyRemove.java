package Casters.Party;

import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyRemove implements CommandInterface
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length != 2)
			{
				caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " Correct Usage: " + ChatColor.DARK_AQUA
						+ "/party" + ChatColor.AQUA + " remove <name>");
				return false;
			}

			if (caster.hasParty())
			{
				if (caster.getParty().getLeader().equals(caster))
				{
					if (caster.getPlayer().getName().equals(args[1]))
					{
						caster.getPlayer().performCommand("party leave");
						return false;
					}

					for (Caster c : Main.getCasters().values())
					{
						if (!c.equals(caster) && c.getPlayer().getName().equals(args[1]))
						{
							if (caster.getParty().getMembers().contains(c))
							{
								caster.getParty().getMembers().remove(c);

								c.setParty(null);

								if (c.getChannel().equals("Party"))
								{
									c.getPlayer().performCommand("chat channel global");
								}

								c.getPlayer().sendMessage(Party.header + ChatColor.WHITE + " You" + ChatColor.RED
										+ " Have Been Removed From The Party.");

								for (Caster member : caster.getParty().getMembers())
								{
									member.getPlayer()
											.sendMessage(Party.header + ChatColor.WHITE + " " + c.getPlayer().getName()
													+ ChatColor.RED + " Has Been Removed From The Party.");
								}
							}
							else
							{
								caster.getPlayer().sendMessage(Party.header + ChatColor.WHITE + " "
										+ c.getPlayer().getName() + ChatColor.GRAY + " Is Not In The Party.");
							}

							return true;
						}
					}

					caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " That Player Is Not Online!");
				}
				else
				{
					caster.getPlayer()
							.sendMessage(Party.header + ChatColor.GRAY + " You Must Be The Leader To Remove People!");
				}
			}
			else
			{
				caster.getPlayer()
						.sendMessage(Party.header + ChatColor.GRAY + " You Must Be In A Party To Remove People!");
			}
		}

		return true;
	}

}
