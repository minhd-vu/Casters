package Casters.Party;

import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Casters;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyMembers implements CommandInterface
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (caster.hasParty())
			{
				String message = Party.header + ChatColor.AQUA + " Leader: " + ChatColor.GRAY
						+ caster.getParty().getLeader().getPlayer().getName() + "\n" + Party.header + ChatColor.AQUA
						+ " Members: " + ChatColor.GRAY;

				for (Caster member : caster.getParty().getMembers())
				{
					if (!member.equals(caster.getParty().getLeader()))
					{
						message += member.getPlayer().getName() + ", ";
					}
				}

				player.sendMessage(message.substring(0, message.length() - 2));
			}
			else
			{
				caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " You Are Not Party Of A Party!");
			}
		}

		return true;
	}
}
