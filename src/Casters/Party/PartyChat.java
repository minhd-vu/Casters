package Casters.Party;

import Casters.CommandInterface;
import Casters.Essentials.Caster;
import Casters.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PartyChat implements CommandInterface
{
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (sender instanceof Player)
		{
			Player player = (Player) sender;
			@SuppressWarnings("unused")
			Caster caster = Main.getCasters().get(player.getUniqueId());

			player.performCommand("chat channel party");
		}

		return false;
	}

}
