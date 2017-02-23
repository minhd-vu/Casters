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

			if (caster.hasParty())
			{
				String message = Party.header + ChatColor.AQUA + " Leader: " + ChatColor.GRAY + caster.getParty().getLeader().getPlayer().getName() + "\n" + Party.header + ChatColor.AQUA + " Members: " + ChatColor.GRAY;

				for (Caster member : caster.getParty().getMembers())
				{
					if (member.equals(caster) && !caster.equals(caster.getParty().getLeader()))
					{
						player.sendMessage(member.getPlayer().getName() + ", ");
					}
				}

				message.trim().substring(0, message.length() - 1);

				player.sendMessage(message);
			}

			else
			{
				caster.getPlayer().sendMessage(Party.header + ChatColor.GRAY + " You Are Not Party Of A Party!");
			}
		}

		return true;
	}
}
