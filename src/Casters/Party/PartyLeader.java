package Casters.Party;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyLeader implements CommandInterface
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (args.length != 2)
			{
				caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " Correct Usage: " + ChatColor.DARK_AQUA
						+ "/party" + ChatColor.AQUA + " leader <name>");
				return false;
			}

			if (caster.hasParty())
			{
				if (caster.getParty().getLeader().equals(caster))
				{
					if (caster.getPlayer().getName().equals(args[1]))
					{
						caster.getPlayer()
								.sendMessage(Party.header + ChatColor.GRAY + " You Are Already The Party Leader.");
						return false;
					}

					for (Caster c : Casters.getCasters().values())
					{
						if (!c.equals(caster) && c.getPlayer().getName().equals(args[1])
								&& caster.getParty().getMembers().contains(c))
						{
							caster.getParty().setLeader(c);

							for (Caster i : caster.getParty().getMembers())
							{
								i.getPlayer().sendMessage(Party.header + " " + ChatColor.WHITE + i.getPlayer().getName()
										+ ChatColor.GRAY + " Is Now The Party Leader");
								return true;
							}
						}
					}

					caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " That Player Is Not In The Party!");
				}
				else
				{
					player.sendMessage(
							Party.header + ChatColor.GRAY + " You Must Be The Leader To Set The Party Leader.");
					return false;
				}
			}
			else
			{
				caster.getPlayer()
						.sendMessage(Party.header + ChatColor.GRAY + " You Must Be In A Party To Set The Leader.");
			}
		}

		return false;
	}
}
