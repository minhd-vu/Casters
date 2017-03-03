package Cast.Party;

import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
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
			Caster caster = Main.getCasters().get(player.getUniqueId());

			if (!caster.hasParty())
			{
				Party party = new Party(caster);
				Main.getParties().add(party);
				caster.setParty(party);
				player.sendMessage(Party.header + ChatColor.GREEN + " You Have Successfully Created A Party!");
			}
		}

		return true;
	}
}
