package Cast.Party;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class PartyMembers implements CommandInterface
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Main.getCasters().get(player.getUniqueId());

			String message = "";

			for (Party party : Main.getParties())
			{
				if (party.getMembers().contains(caster))
				{
					message += Party.header + ChatColor.AQUA + " Leader: " + party.getLeader().getPlayer().getName() + "\nMembers: ";

					for (Caster member : party.getMembers())
					{
						if (member.equals(caster) && !caster.equals(party.getLeader()))
						{
							player.sendMessage(member.getPlayer().getName() + ", ");
						}
					}
					
					message.trim().substring(0, message.length() - 1);
					
					break;
				}
			}
			
			player.sendMessage(message);
		}

		return true;
	}
}
