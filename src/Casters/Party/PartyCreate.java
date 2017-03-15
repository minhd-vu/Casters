package Casters.Party;

import Casters.Casters;
import Casters.CommandInterface;
import Casters.Essentials.Caster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyCreate implements CommandInterface
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			Caster caster = Casters.getCasters().get(player.getUniqueId());

			if (!caster.hasParty())
			{
				Party party = new Party(caster);
				Casters.getParties().add(party);
				caster.setParty(party);
				player.sendMessage(Party.header + ChatColor.GREEN + " You Have Successfully Created A Party!");
			}
		}

		return true;
	}
}
