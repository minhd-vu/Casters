package Casters.Party;

import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyDisband implements CommandInterface
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (args.length < 2)
			{
				if (caster.hasParty())
				{
					Party party = caster.getParty();

					if (party.getLeader().equals(caster))
					{
						for (Caster member : party.getMembers())
						{
							member.setParty(null);
							member.getPlayer()
									.sendMessage(Party.header + ChatColor.GRAY + " The Party Has Been Disbanded.");
						}

						Main.getParties().remove(party);
					}
					else
					{
						caster.getPlayer().sendMessage(
								Party.header + ChatColor.GRAY + " You Must Be The Party Leader To Disband.");
					}
				}
				else
				{
					caster.getPlayer()
							.sendMessage(Party.header + ChatColor.GRAY + " You Must Be In A Party To Leave One.");
				}
			}
		}

		return false;
	}
}
