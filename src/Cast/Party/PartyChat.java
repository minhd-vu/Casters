package Cast.Party;

import Cast.CommandInterface;
import Cast.Essentials.Caster;
import Cast.Main;
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
