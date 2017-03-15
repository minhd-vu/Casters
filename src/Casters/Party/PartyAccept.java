package Casters.Party;

import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyAccept implements CommandInterface
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (caster.hasInvite() && !caster.hasParty())
			{
				caster.setParty(caster.getInvite().getSender().getParty());

				for (Caster c : caster.getParty().getMembers())
				{
					c.getPlayer().sendMessage(Party.header + ChatColor.WHITE + " " + caster.getPlayer().getName()
							+ ChatColor.GRAY + " Has Joined The Party.");
				}

				caster.getInvite().getSender().getParty().getMembers().add(caster);
				caster.getPlayer()
						.sendMessage(Party.header + ChatColor.GREEN + " You Have Successfully Joined The Party.");
				caster.setInvite(null);
			}
			else
			{
				caster.getPlayer()
						.sendMessage(Party.header + ChatColor.GRAY + " You Have No Pending Party Invitations!");
			}
		}

		return false;
	}

}
