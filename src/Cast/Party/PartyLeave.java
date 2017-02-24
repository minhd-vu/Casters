package Cast.Party;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Cast.CommandInterface;
import Cast.Main;
import Cast.Essentials.Caster;

public class PartyLeave implements CommandInterface
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
					if (caster.getParty().getLeader().equals(caster))
					{
						caster.getPlayer().performCommand("party leader " + caster.getParty().getMembers()
								.get((int) Math.round((Math.random() * (caster.getParty().getMembers().size() - 1))))
								.getPlayer().getName());
					}

					caster.getParty().getMembers().remove(caster);

					if (caster.getParty().getMembers().size() <= 0)
					{
						Main.getParties().remove(caster.getParty());
					}

					caster.setParty(null);

					if (caster.getChannel().equals("Party"))
					{
						caster.getPlayer().performCommand("chat channel global");
					}

					caster.getPlayer().sendMessage(Party.header + ChatColor.RED + " You Have Left The Party.");
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
